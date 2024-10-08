package seng202.team7.services;

import java.util.*;

/**
 * A service class for ImportPreviewController that allows the controller to check
 * whether the headers that the users selected can be mapped to each wine attribute
 * and returns indexes so that the wine data can be added from the CSV file to the
 * database.
 */
public class ImportPreviewService {

    /**
     * Modifies the headers from the CSV file to make sure there are no duplicate
     * or blank headers. This is so that when the user has to select a header
     * for each wine attribute, they won't get confused if a header is named the
     * same as another or has no name.
     * @param headers A list of the headers (first line) from the imported CSV file
     * @return A list of the new modified headers that the users can select from
     */
    public String[] modifyHeaders(String[] headers) {
        Map<String, Integer> headerCount = new HashMap<>();
        String[] newHeaders = new String[headers.length];
        String[] headersNoBlanks = turnEmptyToTemp(headers);
        for (int i = 0; i < headersNoBlanks.length; i++) {
            if (headerCount.containsKey(headersNoBlanks[i])) {
                headerCount.put(headersNoBlanks[i], headerCount.get(headersNoBlanks[i]) + 1);
                headersNoBlanks[i] = headersNoBlanks[i] + "_" + headerCount.get(headersNoBlanks[i]).toString();
                headersNoBlanks[i] = containsHeader(headersNoBlanks, newHeaders, i);
                newHeaders[i] = headersNoBlanks[i];
            } else {
                headerCount.put(headersNoBlanks[i], 0);
                headersNoBlanks[i] = containsHeader(headersNoBlanks, newHeaders, i);
                newHeaders[i] = headersNoBlanks[i];
            }
        }
        return newHeaders;
    }

    /**
     * Turns any blank headers in a list into "Temp".
     * @param headers A list of the headers from the imported CSV file
     * @return A list of the headers without the blank headers
     */
    private String[] turnEmptyToTemp(String[] headers) {
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].isBlank()) {
                headers[i] = "Temp";
            }
        }
        return headers;
    }

    /**
     * Adds a hash to the end of the header if the header name already exists
     * within the headers that the user can select from. This prevents any
     * duplicate headers that can be caused if a header ends with "_{integer}"
     * or "#".
     * @param headersNoBlanks A list of the headers without any blank headers
     * @param newHeaders A list of headers which the user will select from
     * @param index Index of the header being checked
     * @return The modified header
     */
    private String containsHeader(String[] headersNoBlanks, String[] newHeaders, int index) {
        while (Arrays.asList(newHeaders).contains(headersNoBlanks[index])) {
            headersNoBlanks[index] = headersNoBlanks[index] + "#";
        }
        return headersNoBlanks[index];
    }

    /**
     * Checks whether the CSV headers selected for each attribute are valid and
     * whether all the attributes have been to a header. Returns an error message
     * if something was wrong with the selected headers.
     * @param headerArray A list of the headers from the CSV file
     * @param data A list of each line of the CSV file excluding the header
     * @param headerIndexes A list of indexes to show which CSV header column each
     *                      wine attribute maps to
     * @return A message to show if the headers were mapped to a wine attribute
     */
    public String checkHeaders(List<String> headerArray, List<String[]> data, List<Integer> headerIndexes) {
        for (String header : headerArray) {
            if (header == null) {
                return "Not all headers are selected.";
            }
        }
        Set<String> headerSet = new HashSet<>(headerArray);
        if (headerSet.size() != headerArray.size()) {
            return "Duplicate headers are selected.";
        }
        if (!validIntegerConversion(headerIndexes.subList(3, 5), data)) {
            return "Vintage and score fields must contain integers.";
        }
        if (!validMandatoryAttributes(headerIndexes.subList(1, 4), data)) {
            return "Name, winery, and vintage fields must have values.";
        }
        return "";
    }

    /**
     * Returns a list of header indexes so that each wine attribute can be connected
     * to a column in the CSV file.
     * @param fileOrder A list of the headers from the CSV file
     * @param headerOrder A list of the headers selected by the user
     * @return A list of indexes of the headers from the CSV file with its index
     * within the list correlating to a particular wine attribute
     */
    public List<Integer> getHeaderIndexes(List<String> fileOrder, List<String> headerOrder) {
        List<Integer> headerIndexes = new ArrayList<>();
        for (String header : headerOrder) {
            headerIndexes.add(fileOrder.indexOf(header));
        }
        return headerIndexes;
    }

    /**
     * Checks whether the values correlating to the headers selected to be the
     * vintage and score fields can be turned into integers.
     * @param vintageScoreIndexes The column indexes for the vintage and score values
     * @param data A list of each line of the CSV file excluding the header
     * @return Boolean whether the vintage and score fields can be turned into
     * an integer
     */
    public boolean validIntegerConversion(List<Integer> vintageScoreIndexes, List<String[]> data) {
        for (Integer index : vintageScoreIndexes) {
            for (String[] line : data) {
                try {
                    int convertToInt = Integer.parseInt(line[index]);
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks whether there are no null values in the data selected to be the wine
     * name, winery, vintage values.
     * @param nameWineryVintageIndexes The column indexes for the wine name, winery,
     *                                 vintage values
     * @param data A list of each line of the CSV file excluding the header
     * @return Boolean whether there are no null values in the wine name, winery,
     * and vintage fields
     */
    public boolean validMandatoryAttributes(List<Integer> nameWineryVintageIndexes, List<String[]> data) {
        for (Integer index : nameWineryVintageIndexes) {
            for (String[] line : data) {
                if (line[index].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }
}
