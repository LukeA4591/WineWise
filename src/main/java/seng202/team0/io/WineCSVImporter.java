package seng202.team0.io;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import seng202.team0.exceptions.DuplicateExc;
import seng202.team0.models.Rating;
import seng202.team0.models.Wine;
import seng202.team0.models.Winery;
import seng202.team0.repository.DatabaseManager;
import seng202.team0.repository.ReviewDAO;
import seng202.team0.repository.WineDAO;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Import wines from csv file
 * @author Ryan Hamilton
 */
public class WineCSVImporter implements Importable<Wine>{

    static WineDAO wineDAO;
    static ReviewDAO reviewDAO;
    static DatabaseManager databaseManager;


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
            String type = line[5];
            String name = line[1];
            String winery = line[0];
            int score = Integer.parseInt(line[2]);
            int vintage = Integer.parseInt(line[4]);
            String description = line[6];
            String region = line[3];
            return new Wine(type, name, winery, vintage, score, region, description);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        return null;
    }
}