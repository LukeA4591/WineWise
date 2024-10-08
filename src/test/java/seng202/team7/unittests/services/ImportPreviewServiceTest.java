package seng202.team7.unittests.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team0.services.ImportPreviewService;

public class ImportPreviewServiceTest {

    private ImportPreviewService importPreviewService;

    @BeforeEach
    public void setup() {
        importPreviewService = new ImportPreviewService();
    }

    private String[] createData() {
        return new String[] {};
    }

    @Test
    public void testAllValidModifyHeaders() {
        String[] headers = new String[]{"First", "Second", "Third", "Fourth"};
        String[] expectedHeaders = new String[]{"First", "Second", "Third", "Fourth"};
        String[] modifiedHeaders = importPreviewService.modifyHeaders(headers);
        Assertions.assertArrayEquals(expectedHeaders, modifiedHeaders);
    }

    @Test
    public void testDuplicateOrEmptyModifyHeaders() {
        String[] headers = new String[]{"First", "First", "Third", " "};
        String[] expectedHeaders = new String[]{"First", "First_1", "Third", "Temp"};
        String[] modifiedHeaders = importPreviewService.modifyHeaders(headers);
        Assertions.assertArrayEquals(expectedHeaders, modifiedHeaders);
    }

    @Test
    public void testDuplicateWithEmptyModifyHeaders() {
        String[] headers = new String[]{"First", "First", "First_1", "", "Temp", "Temp_1"};
        String[] expectedHeaders = new String[]{"First", "First_1", "First_1#", "Temp", "Temp_1", "Temp_1#"};
        String[] modifiedHeaders = importPreviewService.modifyHeaders(headers);
        Assertions.assertArrayEquals(expectedHeaders, modifiedHeaders);
    }

    @Test
    public void testAllValidCheckHeaders() {

    }

}
