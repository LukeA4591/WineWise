package seng202.team7.io;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team7.models.Wine;
import seng202.team7.services.DatasetUploadFeedbackService;

import java.io.*;
import java.nio.charset.Charset;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

/**
 * Import wines from csv file
 */
public class WineCSVImporter implements Importable<Wine>{

    private static final Logger log = LogManager.getLogger(WineCSVImporter.class);

    private DatasetUploadFeedbackService datasetUploadFeedbackService = new DatasetUploadFeedbackService();

    /**
     * Default constructor for the WineCSVImporter
     */
    public WineCSVImporter() {

    }

    /**
     * Read wines from csv file
     * @param file File to read from
     * @param headerIndexes A list of header indexes for column mapping
     * @return List of wines in csv file
     */
    @Override
    public List<Wine> readFromFile(File file, List<Integer> headerIndexes) {
        List<Wine> wines = new ArrayList<>();
        try {
            CSVReader csvreader = new CSVReader(new InputStreamReader(new FileInputStream(file), Charset.forName("Windows-1256")));
            csvreader.skip(1);
            List<String[]> lines = csvreader.readAll();
            csvreader.close();
            for (String[] line : lines) {
                Wine wine = readWineFromLine(line, headerIndexes);
                if (wine != null) {
                    wines.add(wine);
                }
            }
        } catch (IOException | CsvException e) {
            log.error(e);
        }
        return wines;
    }

    /**
     * Reads six lines from a file, used to show a preview of the input
     * @param file file to read from
     * @return List of the lines from the file
     */
    public List<String[]> readSixLinesFromFile(File file) {
        List<String[]> lines = new ArrayList<>();
        String[] line;
        int count = 0;
        try {
            CSVReader csvreader = new CSVReader(new FileReader(file));
            while ((line = csvreader.readNext()) != null && count++ < 6) {
                lines.add(line);
            }
        } catch (IOException | CsvException e) {
            log.error(e);
        }
        return lines;
    }

    /**
     * Read wine from line of csv
     * @param line current csv line as list of Strings
     * @param headerIndexes A list of header indexes to map CSV fields.
     * @return Wine object parsed from line
     */
    public Wine readWineFromLine(String[] line, List<Integer> headerIndexes) {
        try {
            String type = line[headerIndexes.getFirst()];
            String name = line[headerIndexes.get(1)];
            String winery = line[headerIndexes.get(2)];
            Integer vintage = Integer.parseInt(line[headerIndexes.get(3)]);
            Integer score = Integer.parseInt(line[headerIndexes.get(4)]);
            String region = line[headerIndexes.get(5)];
            String description = line[headerIndexes.get(6)];
            if (!validateLine(type, name, winery, vintage, score, region, description)) {
                return new Wine(type, name, winery, vintage, score, region, description);
            }
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            log.error(e);
            datasetUploadFeedbackService.setUploadMessage(3);
        }
        return null;
    }

    /**
     * Validates the fields of the wine and sets appropriate error messages.
     * @param type the type of the wine.
     * @param name the name of the wine.
     * @param wineryString the winery that made the wine.
     * @param vintage the vintage year of the wine.
     * @param score the score of the wine.
     * @param region the region of the wine.
     * @param description a description of the wine.
     * @return true if validation errors exist, otherwise false.
     */
    private boolean validateLine(String type, String name, String wineryString, Integer vintage, Integer score, String region, String description) {
        boolean isError = false;
        if (name.isBlank() || wineryString.isBlank() || vintage == null) {
            datasetUploadFeedbackService.setUploadMessage(0);
            isError = true;
        }
        if (vintage < 0 || vintage > Year.now().getValue()) {
            datasetUploadFeedbackService.setUploadMessage(1);
            isError = true;
        }
        if (score < 0 || score > 100) {
            datasetUploadFeedbackService.setUploadMessage(2);
            isError = true;
        }
        return isError;
    }
}