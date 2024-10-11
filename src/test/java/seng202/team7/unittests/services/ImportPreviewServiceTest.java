package seng202.team7.unittests.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team7.services.ImportPreviewService;

import java.util.Arrays;
import java.util.List;

public class ImportPreviewServiceTest {

    private ImportPreviewService importPreviewService;

    @BeforeEach
    public void setup() {
        importPreviewService = new ImportPreviewService();
    }

    private List<String[]> createData() {
        return List.of(
                new String[]{"First", "Second", "Third", "1", "2", "Hello", "Bye"},
                new String[]{"Fourth", "Fifth", "Sixth", "4", "5", "Hello", ""},
                new String[]{"Seven", "Eighth", "Ninth", "7", "8", "", "Bye"}
        );
    }

    @Test
    public void testAllDifferentHeadersModifyHeaders() {
        String[] headers = new String[]{"First", "Second", "Third", "Fourth"};
        String[] expectedHeaders = new String[]{"First", "Second", "Third", "Fourth"};
        String[] modifiedHeaders = importPreviewService.modifyHeaders(headers);
        Assertions.assertArrayEquals(expectedHeaders, modifiedHeaders);
    }

    @Test
    public void testDuplicateOrEmptyHeadersModifyHeaders() {
        String[] headers = new String[]{"First", "First", "Third", " "};
        String[] expectedHeaders = new String[]{"First", "First_1", "Third", "Temp"};
        String[] modifiedHeaders = importPreviewService.modifyHeaders(headers);
        Assertions.assertArrayEquals(expectedHeaders, modifiedHeaders);
    }

    @Test
    public void testDuplicateWithEmptyHeadersModifyHeaders() {
        String[] headers = new String[]{"First", "First", "First_1", "", "Temp", "Temp_1"};
        String[] expectedHeaders = new String[]{"First", "First_1", "First_1#", "Temp", "Temp_1", "Temp_1#"};
        String[] modifiedHeaders = importPreviewService.modifyHeaders(headers);
        Assertions.assertArrayEquals(expectedHeaders, modifiedHeaders);
    }

    @Test
    public void testAllValidHeadersCheckHeaders() {
        List<String> headers = Arrays.asList("Type", "Name", "Winery", "Vintage", "Score", "Region", "Description");
        List<String[]> data = createData();
        List<Integer> headerIndex = List.of(0, 1, 2, 3, 4, 5, 6);
        String headerMessage = importPreviewService.checkHeaders(headers, data, headerIndex);
        Assertions.assertEquals("", headerMessage);
    }

    @Test
    public void testNotAllHeadersSelectedCheckHeaders() {
        List<String> headers = Arrays.asList("Type", "Name", "Winery", "Vintage", "Score", "Region", null);
        List<String[]> data = createData();
        List<Integer> headerIndex = List.of(0, 1, 2, 3, 4, 5, 6);
        String headerMessage = importPreviewService.checkHeaders(headers, data, headerIndex);
        Assertions.assertEquals("Not all headers are selected.", headerMessage);
    }

    @Test
    public void testDuplicateHeadersCheckHeaders() {
        List<String> headers = Arrays.asList("Type", "Name", "Winery", "Vintage", "Score", "Region", "Region");
        List<String[]> data = createData();
        List<Integer> headerIndex = List.of(0, 1, 2, 3, 4, 5, 6);
        String headerMessage = importPreviewService.checkHeaders(headers, data, headerIndex);
        Assertions.assertEquals("Duplicate headers are selected.", headerMessage);
    }

    @Test
    public void testInvalidIntegerConversionCheckHeaders() {
        List<String> headers = Arrays.asList("Type", "Name", "Winery", "Vintage", "Score", "Region", "Description");
        List<String[]> data = createData();
        List<Integer> headerIndex = List.of(0, 1, 2, 3, 5, 4, 6);
        String headerMessage = importPreviewService.checkHeaders(headers, data, headerIndex);
        Assertions.assertEquals("Vintage and score fields must contain integers.", headerMessage);
    }

    @Test
    public void testInvalidNullValuesCheckHeaders() {
        List<String> headers = Arrays.asList("Type", "Name", "Winery", "Vintage", "Score", "Region", "Description");
        List<String[]> data = createData();
        List<Integer> headerIndex = List.of(5, 6, 2, 3, 4, 0, 1);
        String headerMessage = importPreviewService.checkHeaders(headers, data, headerIndex);
        Assertions.assertEquals("Name, winery, and vintage fields must have values.", headerMessage);
    }

    @Test
    public void testNoNullHeadersGetHeaderIndexes() {
        List<String> fileOrder = Arrays.asList("Type", "Name", "Winery", "Vintage", "Score", "Region", "Description");
        List<String> headerOrder = Arrays.asList("Winery", "Score", "Description");
        List<Integer> expectedHeadersIndexes = Arrays.asList(2, 4, 6);
        List<Integer> headerIndexes = importPreviewService.getHeaderIndexes(fileOrder, headerOrder);
        for (int i = 0; i < expectedHeadersIndexes.size(); i++) {
            Assertions.assertEquals(expectedHeadersIndexes.get(i), headerIndexes.get(i));
        }
    }

    @Test
    public void testSomeNullHeadersGetHeaderIndexes() {
        List<String> fileOrder = Arrays.asList("Type", "Name", "Winery", "Vintage", "Score", "Region", "Description");
        List<String> headerOrder = Arrays.asList("Winery", "Score", "");
        List<Integer> expectedHeadersIndexes = Arrays.asList(2, 4, -1);
        List<Integer> headerIndexes = importPreviewService.getHeaderIndexes(fileOrder, headerOrder);
        for (int i = 0; i < expectedHeadersIndexes.size(); i++) {
            Assertions.assertEquals(expectedHeadersIndexes.get(i), headerIndexes.get(i));
        }
    }
}
