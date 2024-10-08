package seng202.team0.services;

import org.apache.xmlbeans.impl.xb.xsdschema.ImportDocument;
import seng202.team0.io.Importable;
import seng202.team0.io.WineCSVImporter;
import seng202.team0.models.Wine;

import java.util.*;

public class ImportPreviewService {

    Importable<Wine> importer;

    public ImportPreviewService() {
        this.importer = new WineCSVImporter();
    }

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

    private String[] turnEmptyToTemp(String[] headers) {
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].isBlank()) {
                headers[i] = "Temp";
            }
        }
        return headers;
    }

    private String containsHeader(String[] headersNoBlanks, String[] newHeaders, int i) {
        while (Arrays.asList(newHeaders).contains(headersNoBlanks[i])) {
            headersNoBlanks[i] = headersNoBlanks[i] + "#";
        }
        return headersNoBlanks[i];
    }

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
            return "Winery and vintage fields must contain integers.";
        }
        if (!validMandatoryAttributes(headerIndexes.subList(1, 4), data)) {
            return "Name, winery, and vintage fields must have values.";
        }
        return "";
    }

    public List<Integer> getHeaderIndexes(List<String> fileOrder, List<String> headerOrder) {
        List<Integer> headerIndexes = new ArrayList<>();
        for (String header : headerOrder) {
            headerIndexes.add(fileOrder.indexOf(header));
        }
        return headerIndexes;
    }

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
