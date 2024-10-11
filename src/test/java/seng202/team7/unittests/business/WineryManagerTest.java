package seng202.team7.unittests.business;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team7.business.WineryManager;
import seng202.team7.exceptions.DuplicateExc;
import seng202.team7.models.Winery;
import seng202.team7.repository.DatabaseManager;

import java.util.HashSet;
import java.util.Set;

public class WineryManagerTest {

    private static WineryManager wineryManager;
    private static DatabaseManager databaseManager;

    @BeforeAll
    public static void setup() throws DuplicateExc {
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        wineryManager = new WineryManager();
    }

    @BeforeEach
    public void freshDatabase() {
        databaseManager.resetDB();
    }

    private void populateWineriesThroughManager() {
        wineryManager.add(new Winery("Swaws 1", (float) 1.0, (float) 2.0));
        wineryManager.add(new Winery("Namen 2", (float) 1.0, (float) 2.0));
        wineryManager.add(new Winery("Wine ry 3", (float) 1.0, (float) 2.0));
    }

    /**
     * Creates two wineryManagers and checks they are referencing the same instance of the wineryDao to add to the
     * same database
     */
    @Test
    public void testSameInstance() {
        wineryManager.add(new Winery("Swaws 1", (float) 1.0, (float) 2.0));
        WineryManager differentWineryManager = new WineryManager();
        Assertions.assertNotSame(wineryManager, differentWineryManager);
        differentWineryManager.add(new Winery("Swaws 2", (float) 1.0, (float) 3.0));
        Assertions.assertEquals(2, wineryManager.getAll().size());
        Assertions.assertEquals(2, differentWineryManager.getAll().size());
    }

    @Test
    public void testGetAll() {
        populateWineriesThroughManager();
        Assertions.assertEquals(3, wineryManager.getAll().size());
    }

    @Test
    public void testGetAllNothing() {
        Assertions.assertEquals(0, wineryManager.getAll().size());
    }

    @Test
    public void testAdd() {
        Winery wineryToAdd = new Winery("Only Winery", (float) 8.0, (float) 9.0);
        wineryManager.add(wineryToAdd);
        Assertions.assertEquals(1, wineryManager.getAll().size());
    }

    @Test
    public void testAddBatch() {
        Winery winery1 = new Winery("1stWinerrr", (float) 1.0, (float) 9.0);
        Winery winery2 = new Winery("2ndWinerrr", (float) 2.0, (float) 9.0);
        Winery winery3 = new Winery("3rdWinerrr", (float) 3.0, (float) 9.0);
        Winery winery4 = new Winery("4thWinerrr", (float) 4.0, (float) 9.0);
        Winery winery5 = new Winery("5thWinerrr", (float) 5.0, (float) 9.0);

        Set<Winery> winerySet = new HashSet<>();

        winerySet.add(winery1);
        winerySet.add(winery2);
        winerySet.add(winery3);
        winerySet.add(winery4);
        winerySet.add(winery5);

        wineryManager.addBatch(winerySet);
        Assertions.assertEquals(5, wineryManager.getAll().size());

    }

    @Test
    public void testGetExistingWineryNames() {
        populateWineriesThroughManager();
        Set<String> trueNames = new HashSet<>();
        trueNames.add("Swaws 1");
        trueNames.add("Namen 2");
        trueNames.add("Wine ry 3");

        for (int i = 0; i < trueNames.size(); i++) {
            Assertions.assertEquals(wineryManager.getExistingWineryNames(), trueNames);
        }

    }

    @Test
    public void testGetAllWithNullLocation() {
        populateWineriesThroughManager();
        wineryManager.add(new Winery("Totally Real Winery", null, null));
        Assertions.assertEquals(1, wineryManager.getAllWithNullLocation("").size());
    }

    @Test
    public void testUpdateLocationByWineryName() {
        populateWineriesThroughManager();
        wineryManager.updateLocationByWineryName("Swaws 1", (float) 100.0, (float) 250.0);
        Assertions.assertNotEquals(1.0, wineryManager.getWineryByName("Swaws 1").getLongitude(), (float) 0.0);
    }

    @Test
    public void testGetAllWithValidLocation() {
        populateWineriesThroughManager();
        wineryManager.add(new Winery("Totally Real Winery", null, null));
        wineryManager.add(new Winery("Totally Real Winery as well", null, null));
        wineryManager.add(new Winery("Actually Real Winery", (float) 80.0, (float) 45.0));
        Assertions.assertEquals(4, wineryManager.getAllWithValidLocation().size());
    }

    @Test
    public void testGetWineryByName() {
        populateWineriesThroughManager();
        Winery wineryToAdd = new Winery("Only Winery", (float) 8.0, (float) 9.0);
        wineryManager.add(wineryToAdd);
        Assertions.assertEquals(wineryToAdd, wineryManager.getWineryByName("Only Winery"));
    }

}
