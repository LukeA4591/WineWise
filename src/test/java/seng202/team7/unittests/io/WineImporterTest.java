package seng202.team7.unittests.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import seng202.team7.io.Importable;
import seng202.team7.io.WineCSVImporter;
import seng202.team7.models.Wine;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class WineImporterTest {

    public List<String[]> getSixLinesFromTestDecanter() {
        return Arrays.asList(
                new String[] {"Producer","Wine Name","Score","Region","Vintage","Colour","Description"},
                new String[] {"Lake Chalice","Plume Pinot Noir","86","Marlborough","2017","Red",
                        "A great improvement with Oaky notes and a hint of raspberries and peaches"},
                new String[] {"Giesen Group","Clayvin Single Vineyard Chardonnay","94","Marlborough","2017","White",
                        "A great improvement with Oaky notes and a hint of raspberries and peaches"},
                new String[] {"Spy Valley","Envoy Outpost Pinot Noir","89","Marlborough","2017","Red",
                        "A great improvement with Oaky notes and a hint of raspberries and peaches"},
                new String[] {"Te Kano Estate","Northburn Chardonnay","91","Central Otago","2021","White",
                        "A great improvement with Oaky notes and a hint of raspberries and peaches"},
                new String[] {"Vidal","Reserve Chardonnay","88","Hawke's Bay","2021","White",
                        "A great improvement with Oaky notes and a hint of raspberries and peaches"});
    }

    @Test
    public void textReadWinesFromFile() {
        Importable<Wine> wineCSVImporter = new WineCSVImporter();
        URL url = Thread.currentThread().getContextClassLoader().getResource("files/TestDecanter.csv");
        File file = new File(url.getPath());
        List<Integer> headerIndexes = Arrays.asList(5, 1, 0, 4, 2, 3, 6);
        List<Wine> wines = wineCSVImporter.readFromFile(file, headerIndexes);
        Assertions.assertEquals(10, wines.size());
    }

    @Test
    public void testReadSixLinesFromFile() {
        Importable<Wine> importer = new WineCSVImporter();
        URL url = Thread.currentThread().getContextClassLoader().getResource("files/TestDecanter.csv");
        File file = new File(url.getPath());
        List<String[]> expectedLines = getSixLinesFromTestDecanter();
        List<String[]> lines = importer.readSixLinesFromFile(file);
        for (int i = 0; i < lines.size(); i++) {
            Assertions.assertArrayEquals(expectedLines.get(i), lines.get(i));
        }
    }
}
