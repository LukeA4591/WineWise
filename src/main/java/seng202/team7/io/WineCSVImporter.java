package seng202.team7.io;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team7.models.Wine;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Import wines from csv file
 */
public class WineCSVImporter implements Importable<Wine>{

    private static final Logger log = LogManager.getLogger(WineCSVImporter.class);

    /**
     * Read wines from csv file
     * @param file File to read from
     * @return List of wines in csv file
     */
    @Override
    public List<Wine> readFromFile(File file) {
        List<Wine> wines = new ArrayList<>();
        try {
            CSVReader csvreader = new CSVReader(new FileReader(file));
            //TODO Read headers and turn into a list (if duplicate add integer to end)
            csvreader.skip(1);
            List<String[]> lines = csvreader.readAll();
            for (String[] line : lines) {
                Wine wine = readWineFromLine(line);
                if (wine != null) {
                    wines.add(wine);
                }
            }
        } catch (IOException | CsvException e) {
            log.error(e);
        }
        return wines;
    }

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
     * @return Wine object parsed from line
     */
    private Wine readWineFromLine(String[] line) {
        try {
            String type = line[5];
            String name = line[1];
            String winery = line[0];
            int score = Integer.parseInt(line[2]);
            int vintage = Integer.parseInt(line[4]);
            String description = line[6];
            String region = line[3];
            return new Wine(type, name, winery, vintage, score, region, description);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            log.error(e);
        }
        return null;
    }
}