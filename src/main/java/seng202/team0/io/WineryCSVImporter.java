package seng202.team0.io;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import seng202.team0.models.Winery;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Import wineries from csv file
 * @author Ryan Hamilton
 */
public class WineryCSVImporter implements Importable<Winery> {
    /**
     * Read wineries from csv file
     * @param file File to read from
     * @return List of wineries in csv file
     */
    @Override
    public List<Winery> readFromFile(File file) {
        List<Winery> wineries = new ArrayList<>();
        try {
            CSVReader csvreader = new CSVReader(new FileReader(file));
            csvreader.skip(1);
            List<String[]> lines = csvreader.readAll();
            for (String[] line : lines) {
                Winery winery = readWineryFromLine(line);
                if (winery != null) {
                    wineries.add(winery);
                }
            }
        } catch (IOException | CsvException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        return wineries;
    }
    /**
     * Read winery from line of csv
     * @param line current csv line as list of Strings
     * @return Winery object parsed from line
     */
    private Winery readWineryFromLine(String[] line) {
        try {
            return null;
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        return null;
    }
}
