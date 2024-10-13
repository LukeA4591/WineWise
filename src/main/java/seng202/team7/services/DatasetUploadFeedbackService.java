package seng202.team7.services;

import org.apache.poi.ss.formula.atp.Switch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This service manages feedback messages during the dataset upload process.
 * It stores error codes and returns appropriate messages based on those codes.
 */
public class DatasetUploadFeedbackService {

    private static Set<Integer> uploadMessageCodes = new HashSet<>();

    /**
     *  constructor for the DatasetUploadFeedbackService.
     */
    public DatasetUploadFeedbackService(){}

    /**
     * stores the status code representing the result of the wine upload.
     * @param uploadedStatusCode the integer status code representing an upload error
     */
    public static void setUploadMessage(int uploadedStatusCode) {
        uploadMessageCodes.add(uploadedStatusCode);
        System.out.println(uploadMessageCodes);
    }

    /**
     * Returns the error feedback message.
     * @return feedback message detailing what errors were found if any.
     */
    public static String getUploadMessage() {
        if (uploadMessageCodes.isEmpty()) {
            return "Wines uploaded";
        }
        return getSpecificErrors();
    }

    /**
     * Maps the stored error codes to detailed error messages.
     * @return a string with all the error messages concatenated into one message.
     */
    private static String getSpecificErrors() {
        String errorMessage = "Partial dataset upload:\nSome wines were not added";
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
