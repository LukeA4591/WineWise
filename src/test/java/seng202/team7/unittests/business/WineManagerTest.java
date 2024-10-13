package seng202.team7.unittests.business;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team7.business.WineManager;
import seng202.team7.business.WineryManager;
import seng202.team7.exceptions.DuplicateExc;
import seng202.team7.io.Importable;
import seng202.team7.io.WineCSVImporter;
import seng202.team7.models.Wine;
import seng202.team7.models.Winery;
import seng202.team7.repository.DatabaseManager;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class WineManagerTest {

    private static WineManager wineManager;
    private static DatabaseManager databaseManager;

    @BeforeAll
    public static void setup() throws DuplicateExc {
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        wineManager = new WineManager();
    }

    @BeforeEach
    public void freshDatabase() {
        databaseManager.resetDB();
    }

    private void populateDB() {
        Wine wine1 = new Wine("Red", "Noir", "Winery", 2000, 85, "REGION 1", "High quality :)");
        Wine wine2 = new Wine("White", "Not Noir", "Winery", 2001, 95, "REGION 1", "Higher quality :)");
        Wine wine3 = new Wine("Red", "Noir again", "Diff. Winery", 2010, 80, "REGION 2", "'aight");

        wineManager.add(wine1);
        wineManager.add(wine2);
        wineManager.add(wine3);
    }

    /**
     * Creates two wineManagers and checks they are referencing the same instance of the wineDao to add to the
     * same database
     */
    @Test
    public void testSameInstance() {
        WineManager anotherWineManager = new WineManager();
        Assertions.assertEquals(0, wineManager.getAll().size());
        Assertions.assertEquals(0, anotherWineManager.getAll().size());
        populateDB();
        Assertions.assertEquals(3, wineManager.getAll().size());
        Assertions.assertEquals(3, anotherWineManager.getAll().size());
        // True Test
        anotherWineManager.add(new Wine("Red", "New wine to diff manager", "Winery", 2019, 75, "REGION 1", "secret"));
        Assertions.assertEquals(4, wineManager.getAll().size());
        Assertions.assertEquals(4, anotherWineManager.getAll().size());
    }

    @Test
    public void testAddBatch() {
        Importable<Wine> wineCSVImporter = new WineCSVImporter();
        URL url = Thread.currentThread().getContextClassLoader().getResource("files/TestDecanter.csv");
        File file = new File(url.getPath());
        List<Integer> headerIndexes = Arrays.asList(5, 1, 0, 4, 2, 3, 6);
        wineManager.addBatch(wineCSVImporter, file, headerIndexes);
        List<Wine> wines = wineManager.getAll();
        List<Wine> expectedWines = wineCSVImporter.readFromFile(file, headerIndexes);
        Assertions.assertEquals(expectedWines.size(), wines.size());
    }

    @Test
    public void testDeleteAndUpdateWines() {
        Wine wine = new Wine("Red", "Pinot Noir", "Winery", 2000, 100, "Region", "Nice");
        wineManager.add(wine);
        Wine updateWine = new Wine("White", "Pinot Gris", "Winery", 2000, 100, "Region", "Nice");
        wineManager.updateWine(updateWine, wine);
        Assertions.assertTrue(wineManager.checkIfWineExists(updateWine));
        Wine newWine = wineManager.getWineFromID(wineManager.getWineID(updateWine));
        Assertions.assertEquals(updateWine, newWine);
        wineManager.delete("Pinot Gris", "Winery", 2000);
        Assertions.assertEquals(0, wineManager.getAll().size());
    }

    @Test
    public void testGetDistinct() {
        populateDB();
        List<String> expectedNames = Arrays.asList("Noir", "Not Noir", "Noir again");
        List<String> names = wineManager.getDistinct("name");
        Assertions.assertEquals(new HashSet<>(names), new HashSet<>(expectedNames));
    }

    @Test
    public void testSearchWines() {
        populateDB();
        List<Wine> wines = wineManager.searchWines("Noir");
        Assertions.assertEquals(3, wines.size());
    }

    @Test
    public void testGetWineWithWinery() {
        populateDB();
        List<Wine> expectedWines = wineManager.getAll().stream().filter(wine -> "Winery".equals(wine.getWineryString())).toList();
        List<Wine> wines = wineManager.getWineWithWinery(new Winery("Winery"));
        Assertions.assertEquals(expectedWines.size(), wines.size());
    }
}
