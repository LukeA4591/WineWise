package seng202.team0.unittests.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import seng202.team0.io.Importable;
import seng202.team0.io.WineCSVImporter;
import seng202.team0.models.Wine;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class WineImporterTest {

    @Test
    public void readWinesFromFile() {
        Importable wineCSVImporter = new WineCSVImporter();
        URL url = Thread.currentThread().getContextClassLoader().getResource("files/TestDecanter.csv");
        File file = new File(url.getPath());
        List<Wine> wines = wineCSVImporter.readFromFile(file);
        Assertions.assertEquals(wines.size(), 10);
    }
//
//    @Test
//    public void testThing() {
//        Importable<Wine> importer = new WineCSVImporter();
//        URL url = Thread.currentThread().getContextClassLoader().getResource("files/EmptyCSV.csv");
//        File file = new File(url.getPath());
//        List<String[]> lines = importer.readSixLinesFromFile(file);
//        for (String[] line :lines) {
//            System.out.println(Arrays.toString(line));
//        }
//        Assertions.assertEquals(10, 10);
//    }
}
