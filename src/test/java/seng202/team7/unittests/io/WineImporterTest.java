package seng202.team7.unittests.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import seng202.team7.io.Importable;
import seng202.team7.io.WineCSVImporter;
import seng202.team7.models.Wine;

import java.io.File;
import java.net.URL;
import java.util.List;

public class WineImporterTest {

    @Test
    public void readWinesFromFile() {
        Importable wineCSVImporter = new WineCSVImporter();
        URL url = Thread.currentThread().getContextClassLoader().getResource("files/TestDecanter.csv");
        File file = new File(url.getPath());
        List<Wine> wines = wineCSVImporter.readFromFile(file);
        Assertions.assertEquals(10, wines.size());
    }
}
