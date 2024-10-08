package seng202.team0.unittests.services;

import io.cucumber.java.bs.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team0.exceptions.DuplicateExc;
import seng202.team0.models.Wine;
import seng202.team0.repository.DatabaseManager;
import seng202.team0.repository.WineDAO;

import java.util.*;

public class WineDAOTest {
    private static WineDAO wineDao;
    private static DatabaseManager databaseManager;
    private static Map<String, String> filters = new HashMap<>();
    private static Map<String, List<String>> scoreFilters = new HashMap<>();

    @BeforeAll
    public static void setup() throws DuplicateExc {
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        wineDao = new WineDAO();

        scoreFilters.put("score", new ArrayList<>()); //will need to change when user scores come in

        filters.put("type", "ALL");
        filters.put("winery", "ALL");
        filters.put("vintage", "ALL");
        filters.put("region", "ALL");

        scoreFilters.put("score", Arrays.asList("", ""));
    }

    @BeforeEach
    public void resetDB() {
        databaseManager.resetDB();
    }

    /**
     * Populates the test database with wines
     */
    private void populateDatabase() throws DuplicateExc {
        wineDao.add(new Wine("Red", "Plume Pinot Noir", "Lake Chalice", 2019, 80, "Marlborough", "High quality wine with woody notes"));
        wineDao.add(new Wine("White", "Plume Sav", "Lake Chalice", 2019, 85, "Marlborough", "So tasty"));
        wineDao.add(new Wine("Rose", "Rosy Rose", "Lakes Winery", 2020, 90, "Otago", "Very rosy"));
        wineDao.add(new Wine("White", "Bland Blanc", "Fields of Grapes", 2021, 50, "Canterbury", "Bland and boring"));
        wineDao.add(new Wine("Rose", "Rose", "Winery1", 2021, 50, "Canterbury", "Bland and boring"));
        wineDao.add(new Wine("Red", "Red", "Winery2", 2021, 50, "Canterbury", "Bland and boring"));

    }

    @Test
    public void testGetAllWines() throws DuplicateExc {
        populateDatabase();
        Assertions.assertEquals(6, wineDao.getAll().size());
    }

    @Test
    public void testFilterByType() throws DuplicateExc {
        populateDatabase();
        filters.put("type", "White");
        List<Wine> wines = wineDao.getFilteredWines(filters, scoreFilters, "");
        Assertions.assertEquals(2, wines.size());
        Assertions.assertEquals("White", wines.get(0).getColor());
        Assertions.assertEquals("White", wines.get(1).getColor());
    }

    @Test
    public void testFilterByTypeAndWinery() throws DuplicateExc {
        populateDatabase();
        filters.put("type", "White");
        filters.put("winery", "Lake Chalice");
        List<Wine> wines = wineDao.getFilteredWines(filters, scoreFilters, "");
        Assertions.assertEquals(1, wines.size());
        Assertions.assertEquals("White", wines.getFirst().getColor());
        Assertions.assertEquals("Lake Chalice", wines.getFirst().getWineryString());
    }

    @Test
    public void testFilterByTypeAndRating() throws DuplicateExc {
        populateDatabase();
        filters.put("type", "White");
        scoreFilters.put("score", Arrays.asList("60", "90"));
        List<Wine> wines = wineDao.getFilteredWines(filters, scoreFilters, "");
        Assertions.assertEquals(1, wines.size());
        Assertions.assertEquals("White", wines.getFirst().getColor());
        Assertions.assertTrue(wines.getFirst().getScore() < 90 && wines.getFirst().getScore() >= 60);
    }

    @Test
    public void testGetDistinct() throws DuplicateExc {
        populateDatabase();
        List<String> distinctWineries = wineDao.getDistinct("winery");
        Assertions.assertEquals(5, distinctWineries.size());
    }

    @Test
    public void testDuplicateWine() throws DuplicateExc {
        populateDatabase();
        wineDao.add(new Wine("White", "Tasty Blanc", "Fields of Grapes", 2021, 80, "Canterbury", "Yummy"));
        wineDao.add(new Wine("White", "Bland Blanc", "Fields of Grapes", 2021, 60, "Canterbury", "Bland and boring"));
        Assertions.assertEquals(7, wineDao.getAll().size());
    }

    @Test
    public void testDelete() throws DuplicateExc {
        populateDatabase();
        wineDao.delete("Rosy Rose", "Lakes Winery", 2020);
        Assertions.assertEquals(5, wineDao.getAll().size());
    }

    @Test
    public void testDeleteDoesNotExist() throws DuplicateExc {
        populateDatabase();
        wineDao.delete("WWWWWWW", "AAAAAAAAAAA", 0);
        Assertions.assertEquals(6, wineDao.getAll().size());
    }

    @Test
    public void testGetTopRated() throws DuplicateExc {
        populateDatabase();
        List<Wine> wines = wineDao.getTopRated();
        Assertions.assertEquals(6, wines.size());
        Assertions.assertEquals(wines.get(0).getWineName(), "Rosy Rose");
        Assertions.assertEquals(wines.get(1).getWineName(), "Plume Sav");
        Assertions.assertEquals(wines.get(2).getWineName(), "Plume Pinot Noir");
    }

    @Test
    public void testGetSimilarWinesSize() throws DuplicateExc {
        populateDatabase();
        Wine NewWine = new Wine("Red", "Test Plume Pinot Noir", "Lake Chalice", 2019, 60, "Marlborough", "High quality wine with woody notes");
        List<Wine> wines = wineDao.getSimilarWines(NewWine);
        Assertions.assertEquals(3, wines.size());
    }

    @Test
    public void testGetSimilarWinesCorrectly() throws DuplicateExc {
        populateDatabase();
        Wine NewWine = new Wine("White", "Test Defo Not Plume Pinot Noir", "Lake Chalice", 2019, 2, "Marlborough", "High quality wine with woody notes");
        List<Wine> wines = wineDao.getSimilarWines(NewWine);
        Assertions.assertEquals("Bland Blanc", wines.get(0).getWineName()); // NOT Plume Sav as For loop to check similarities check 3 - > 1 so 1 has to change, AKA Plume Sav -> Bland Blanc
        Assertions.assertEquals("Plume Pinot Noir", wines.get(1).getWineName());
        Assertions.assertEquals("Plume Sav", wines.get(2).getWineName());
    }

    @Test
    public void testRandomOtherWines() throws DuplicateExc {
        populateDatabase();
        Wine NewWine = new Wine("Red", "Plume Pinot Noir", "Lake Chalice", 2019, 80, "Marlborough", "High quality wine with woody notes");
        Wine RandomWine = wineDao.getRandomOtherWine(NewWine);
        Assertions.assertNotEquals("Plume Pinot Noir", RandomWine.getWineName());
    }
}
