package seng202.team7.unittests.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team7.exceptions.DuplicateExc;
import seng202.team7.models.Winery;
import seng202.team7.repository.DatabaseManager;
import seng202.team7.repository.WineryDAO;

import java.util.*;

public class WineryDAOTest {
    private static WineryDAO wineryDAO;
    private static DatabaseManager databaseManager;

    @BeforeAll
    public static void setup() throws DuplicateExc {
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        wineryDAO = new WineryDAO();
    }

    @BeforeEach
    public void resetDB() {
        databaseManager.resetDB();
    }

    private void populateDatabase() {
        wineryDAO.add(new Winery("One Winery", (float) 1.0, (float) 2.0));
        wineryDAO.add(new Winery("Two Winery", (float) 1.0, (float) 2.0));
        wineryDAO.add(new Winery("Three Winery", (float) 1.0, (float) 2.0));
    }

    @Test
    public void testGetAllWineries() {
        populateDatabase();
        Assertions.assertEquals(3, wineryDAO.getAll().size());
    }

    @Test
    public void testAddWinery() {
        populateDatabase();
        wineryDAO.add(new Winery("Four Winery", (float) 1.0, (float) 2.0));
        Assertions.assertEquals(4, wineryDAO.getAll().size());
    }

    @Test
    public void testAddBatch() {
        Set<Winery> wineries = new HashSet<>();
        wineries.add(new Winery("One Winery", (float) 1.0, (float) 2.0));
        wineries.add(new Winery("Two Winery", (float) 1.0, (float) 2.0));
        wineries.add(new Winery("Three Winery", (float) 1.0, (float) 2.0));
        wineryDAO.addBatch(wineries);
        Assertions.assertEquals(wineries.size(), wineryDAO.getAll().size());
    }

    @Test
    public void testDupesAddBatch() {
        Set<Winery> wineries = new HashSet<>();
        wineries.add(new Winery("One Winery", (float) 1.0, (float) 2.0));
        wineries.add(new Winery("One Winery", (float) 1.0, (float) 2.0));
        wineries.add(new Winery("One Winery", (float) 1.0, (float) 2.0));
        Set<String> uniqueNames = new HashSet<>();
        for (Winery winery : wineries) {
            uniqueNames.add(winery.getWineryName());
        }
        wineryDAO.addBatch(wineries);
        Assertions.assertEquals(uniqueNames.size(), wineryDAO.getAll().size());
    }

    @Test
    public void testGetExistingWineryNames() {
        populateDatabase();
        List<String> nameList = new ArrayList<>(wineryDAO.getExistingWineryNames());
        Assertions.assertEquals(3, wineryDAO.getExistingWineryNames().size());
        List<String> expectedNameList = Arrays.asList("Three Winery", "Two Winery", "One Winery");
        Assertions.assertLinesMatch(expectedNameList, nameList);
    }

    @Test
    public void testGetAllWithNullLocation() {
        populateDatabase();
        wineryDAO.add(new Winery("Four Winery", null, null));
        wineryDAO.add(new Winery("Five Winery", null, null));
        List<Winery> wineryList = wineryDAO.getAllWithNullLocation("");
        List<Winery> expectedWineryList = Arrays.asList(new Winery("Five Winery"), new Winery("Four Winery"));
        for (int i = 0; i < wineryList.size(); i++) {
            Assertions.assertEquals(expectedWineryList.get(i), wineryList.get(i));
        }
    }

    @Test
    public void testGetAllWithNullLocationAndSearch() {
        populateDatabase();
        Winery winery = new Winery("Four Winery", null, null);
        wineryDAO.add(winery);
        wineryDAO.add(new Winery("Five Winery", null, null));
        List<Winery> wineryList = wineryDAO.getAllWithNullLocation("Four");
        Assertions.assertEquals(wineryList.size(), 1);
        Assertions.assertEquals(wineryList.getFirst(), winery);
    }

    @Test
    public void testUpdateLocationWithWineryName() {
        populateDatabase();
        Winery winery = new Winery("Four Winery", null, null);
        wineryDAO.add(winery);
        wineryDAO.updateLocationByWineryName("Four Winery", (float) 2, (float) 2);
        winery = wineryDAO.getWineryByName("Four Winery");
        Assertions.assertEquals(2, winery.getLatitude());
        Assertions.assertEquals(2, winery.getLongitude());
    }

    @Test
    public void testGetAllWithValidLocation() {
        populateDatabase();
        wineryDAO.add(new Winery("Four Winery", null, null));
        wineryDAO.add(new Winery("Five Winery", null, null));
        List<Winery> wineries = wineryDAO.getAllWithValidLocation();
        Assertions.assertEquals(wineries.size(), 3);
        for (Winery winery : wineries) {
            Assertions.assertNotEquals(winery.getLatitude(), null);
            Assertions.assertNotEquals(winery.getLongitude(), null);
        }
    }

    @Test
    public void testGetWineryByName() {
        populateDatabase();
        Winery winery = wineryDAO.getWineryByName("One Winery");
        Assertions.assertEquals(winery.getWineryName(), "One Winery");
        Assertions.assertEquals(winery.getLatitude(), 2);
        Assertions.assertEquals(winery.getLongitude(), 1);
    }
}
