package seng202.team7.services;

import org.apache.poi.ss.formula.atp.Switch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DatasetUploadFeedbackService {

    private static Set<Integer> uploadMessageCodes = new HashSet<>();
    public DatasetUploadFeedbackService(){}

    public static void setUploadMessage(int uploadedStatusCode) {
        uploadMessageCodes.add(uploadedStatusCode);
    }

    public static String getUploadMessage() {
        if (uploadMessageCodes.isEmpty()) {
            return "Wines uploaded";
        }
        return getSpecificErrors();
    }

    /**
     * Maps first error of in line to error message.
     * @return
     */
    private static String getSpecificErrors() {
        String errorMessage = "Incomplete wine upload:";
        for (Integer messageCode: uploadMessageCodes) {
            switch(messageCode) {
                case 0:
                    errorMessage += "\nName, Winery and/or Vintage is null";
                    break;
                case 1:
                    errorMessage += "\nYear is not between 0 and current year";
                    break;
                case 2:
                    errorMessage += "\nScore is not between 0 and 100";
                    break;
                case 3:
                    errorMessage += "\nVintage and/or score is not an integer";
                    break;
            }
        }
        uploadMessageCodes.clear();
        return errorMessage;
    }

}
