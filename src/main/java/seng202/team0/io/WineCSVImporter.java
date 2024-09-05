package seng202.team0.io;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import seng202.team0.models.Wine;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Import wines from csv file
 * @author Ryan Hamilton
 */
public class WineCSVImporter implements Importable<Wine>{
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
            csvreader.skip(1);
            List<String[]> lines = csvreader.readAll();
            for (String[] line : lines) {
                Wine wine = readWineFromLine(line);
                if (wine != null) {
                    wines.add(wine);
                }
            }
        } catch (IOException | CsvException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        return wines;
    }
    /**
     * Read wine from line of csv
     * @param line current csv line as list of Strings
     * @return Wine object parsed from line
     */
    private Wine readWineFromLine(String[] line) {
        try {
            // Add winery
            String wineName = line[1];
            // Probably remove award
            // Probably remove score
            int vintage = Integer.parseInt(line[2]);
            String colour = line[3];
            String description = line[4];
            // Could add list of ratings
            return null; // Return winery
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        return null;
    }
}