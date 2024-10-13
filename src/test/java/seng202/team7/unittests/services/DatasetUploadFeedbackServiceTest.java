package seng202.team7.unittests.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team7.services.DatasetUploadFeedbackService;

public class DatasetUploadFeedbackServiceTest {

    @Test
    public void getUploadMessageNoErrors() {
        String message = DatasetUploadFeedbackService.getUploadMessage();
        String expectedMessage = "Wines uploaded";
        Assertions.assertEquals(expectedMessage, message);
    }

    @Test
    public void getUploadMessageWithOneError() {
        DatasetUploadFeedbackService.setUploadMessage(0);
        String message = DatasetUploadFeedbackService.getUploadMessage();
        String expectedMessage = """
                Partial dataset upload:
                Some wines were not added
                Name, Winery and/or Vintage is null""";
        Assertions.assertEquals(expectedMessage, message);
    }

    @Test
    public void getUploadMessageWithAllErrors() {
        for (int i = 0; i < 4; i++) {
            DatasetUploadFeedbackService.setUploadMessage(i);
        }
        String message = DatasetUploadFeedbackService.getUploadMessage();
        String expectedMessage = """
                Partial dataset upload:
                Some wines were not added
                Name, Winery and/or Vintage is null
                Year is not between 0 and current year
                Score is not between 0 and 100
                Vintage and/or score is not an integer""";
        Assertions.assertEquals(expectedMessage, message);
    }
}
