package seng202.team0.io;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import seng202.team0.exceptions.DuplicateExc;
import seng202.team0.models.Wine;
import seng202.team0.models.Winery;
import seng202.team0.repository.DatabaseManager;
import seng202.team0.repository.WineDAO;

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

    static WineDAO wineDAO;
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
            // Add winery
            String wineryname = line[0];
            String wineName = line[1];
            // Probably remove award
            // Probably remove score
            int vintage = Integer.parseInt(line[2]);
            String colour = line[3];
            String description = line[4];
            // Could add list of ratings
            return new Wine(colour, wineName, 007, vintage, "Bay of Unimplemented", new Winery(wineryname, 0, 0), description, new ArrayList<>());
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        return null;
    }

    static void setup() {
        DatabaseManager.REMOVE_INSTANCE();
//        databaseManager = DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        databaseManager = new DatabaseManager("jdbc:sqlite:./src/main/resources/sql/initialise_database.sql");
        wineDAO = new WineDAO();
    }

    public static void main(String[] args) {
        Importable<Wine> importer = new WineCSVImporter();
        File file = new File("testwine.csv");
        List<Wine> wines = importer.readFromFile(file);

        setup();

        for (Wine el_wines : wines) {
            System.out.printf("Adding Wine: %s to Database", el_wines.getWineName());
            try {
                wineDAO.add(el_wines);
            } catch (DuplicateExc e) {
                throw new RuntimeException(e);
            }

//            System.out.printf("Colour: %s\nName: %s\nScore: %d\nVintage: %d\n...\nDesc.: %s", el_wines.getColor(), el_wines.getWineName(), el_wines.getScore(), el_wines.getVintage(), el_wines.getDescription());
//            System.out.println("\n\n");
        }

        System.out.println("## ALl Wines in Database ##");
        System.out.println(wineDAO.getAll());

        // Removes Database at end
        DatabaseManager.REMOVE_INSTANCE();
    }
}