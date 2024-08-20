package seng202.team0.io;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import seng202.team0.models.Winery;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class WineryXLSImporter implements Importable<Winery>{
    /**
     * Read wineries from xls file
     * @param file File to read from
     * @return List of wineries in xls file
     */
    @Override
    public List<Winery> readFromFile(File file) {
        List<Winery> wineries = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Winery winery = readWineryFromLine(row);
                if (winery != null) {
                    wineries.add(winery);
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        wineries.sort(Comparator.comparing(Winery::getName));
        return wineries;
    }
    /**
     * Read winery from line of xls
     * @param row current xls line as Row
     * @return Winery object parsed from line
     */
    private Winery readWineryFromLine(Row row) {
        try {
            Cell nameCell = row.getCell(0);
            Cell latitudeCell = row.getCell(1);
            Cell longitudeCell = row.getCell(2);
            String name = nameCell.getStringCellValue();
            float latitude = (float) latitudeCell.getNumericCellValue();
            float longitude = (float) longitudeCell.getNumericCellValue();
            return new Winery(name, latitude, longitude);
        } catch (IllegalStateException | NumberFormatException | IndexOutOfBoundsException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        return null;
    }
    public static void main(String[] args) {
        Importable<Winery> importer = new WineryXLSImporter();
        File file = new File("testwinery.xlsx");
        List<Winery> wineries = importer.readFromFile(file);
        for (Winery winery : wineries) {
            System.out.printf("%s\n%.2f\n%.2f\n", winery.getName(), winery.getLatitude(), winery.getLongitude());
        }
    }
}