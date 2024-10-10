package seng202.team7.unittests.services;

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
        Importable<Wine> wineCSVImporter = new WineCSVImporter();
        URL url = Thread.currentThread().getContextClassLoader().getResource("files/TestDecanter.csv");
        File file = new File(url.getPath());
        List<Integer> headerIndexes = Arrays.asList(5, 1, 0, 4, 2, 3, 6);
        List<Wine> wines = wineCSVImporter.readFromFile(file, headerIndexes);
        Assertions.assertEquals(10, wines.size());
    }
}
