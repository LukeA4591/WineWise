package seng202.team7.services;

import java.util.*;

public class ImportPreviewService {
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
            if (headers[i].isEmpty()) {
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

    public String checkHeaders(List<String> headerArray) {
        for (String header : headerArray) {
            if (header == null) {
                return "Not all headers are selected.";
            }
        }
        Set<String> headerSet = new HashSet<String>(headerArray);
        if (headerSet.size() != headerArray.size()) {
            return "Duplicate headers are selected.";
        }
        return "";
    }

    public List<Integer> getHeaderIndexes (List<String> fileOrder, List<String> headerOrder) {
        List<Integer> headerIndexes = new ArrayList<>();
        for (String header : headerOrder) {
            headerIndexes.add(fileOrder.indexOf(header));
        }
        return headerIndexes;
    }

}
