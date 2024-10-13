package seng202.team7.unittests.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team7.exceptions.DuplicateExc;
import seng202.team7.models.Wine;
import seng202.team7.repository.DatabaseManager;
import seng202.team7.repository.WineDAO;

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
        wineDao.clearAlreadySelected();
    }

    /**
     * Populates the test database with wines
     */
    private void populateDatabase() throws DuplicateExc {
        wineDao.add(new Wine("Red", "Plume Pinot Noir", "Lake Chalice", 2019, 80, "Marlborough", "High quality wine with woody notes"));
        wineDao.add(new Wine("White", "Plume Sav", "Lake Chalice", 2019, 85, "Marlborough", "So tasty"));
        wineDao.add(new Wine("Rose", "Rosy Rose", "Lakes Winery", 2020, 90, "Otago", "Very rosy"));
        wineDao.add(new Wine("White", "Bland Blanc", "Fields of Grapes", 2019, 50, "Canterbury", "Bland and boring"));
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
        wineDao.add(new Wine("White", "Bland Blanc", "Fields of Grapes", 2019, 60, "Canterbury", "Bland and boring"));
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
        List<Wine> wines = wineDao.getTopRated(0);
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
        Wine NewWine = new Wine("White", "Test Defo Not Plume Pinot Noir", "Lake Chalice", 2020, 2, "Marlborough", "High quality wine with woody notes");
        List<Wine> wines = wineDao.getSimilarWines(NewWine);
        Assertions.assertTrue(wines.contains(new Wine("White", "Plume Sav", "Lake Chalice", 2019, 85, "Marlborough", "So tasty")));
        Assertions.assertTrue(wines.contains(new Wine("Red", "Plume Pinot Noir", "Lake Chalice", 2019, 80, "Marlborough", "High quality wine with woody notes")));
        Assertions.assertTrue(wines.contains(new Wine("Rose", "Rosy Rose", "Lakes Winery", 2020, 90, "Otago", "Very rosy")));
    }

    @Test
    public void testRandomOtherWines() throws DuplicateExc {
        populateDatabase();
        Wine NewWine = new Wine("Red", "Plume Pinot Noir", "Lake Chalice", 2019, 80, "Marlborough", "High quality wine with woody notes");
        Wine RandomWine = wineDao.getRandomOtherWine(NewWine);
        Assertions.assertNotEquals("Plume Pinot Noir", RandomWine.getWineName());
    }

    @Test
    public void testSuccessfulUpdateWineNotKeyAttribute() throws DuplicateExc {
        populateDatabase();
        Wine oldWine = new Wine("Red", "Plume Pinot Noir", "Lake Chalice", 2019, 80, "Marlborough", "High quality wine with woody notes");
        Wine newWine = new Wine("Red", "Plume Pinot Noir", "Lake Chalice", 2019, 90, "Marlborough", "High quality wine with woody notes");
        wineDao.updateWine(newWine, oldWine);
        Assertions.assertTrue(wineDao.getAll().contains(newWine));
        Wine newWineFromDB = wineDao.getWineFromID(wineDao.getWineID(newWine));
        Assertions.assertEquals(90, newWineFromDB.getScore());
    }

    @Test
    public void testSuccessfulUpdateWineKeyAttribute() throws DuplicateExc {
        populateDatabase();
        Wine oldWine = new Wine("Red", "Plume Pinot Noir", "Lake Chalice", 2019, 80, "Marlborough", "High quality wine with woody notes");
        Wine newWine = new Wine("Red", "Plume Pinot Noir But Better", "Lake Chalice", 2019, 90, "Marlborough", "High quality wine with woody notes");
        boolean success = wineDao.updateWine(newWine, oldWine);

        Assertions.assertTrue(success);
        Assertions.assertTrue(wineDao.getAll().contains(newWine));
        Assertions.assertFalse(wineDao.getAll().contains(oldWine));

        Wine newWineFromDB = wineDao.getWineFromID(wineDao.getWineID(newWine));
        Assertions.assertEquals(90, newWineFromDB.getScore());
        Assertions.assertEquals("Plume Pinot Noir But Better", newWineFromDB.getWineName());
    }

    @Test
    public void testUnsuccessfulUpdateWine() throws DuplicateExc {
        populateDatabase();
        Wine oldWine = new Wine("Red", "Plume Pinot Noir", "Lake Chalice", 2019, 80, "Marlborough", "High quality wine with woody notes");
        Wine newWine = new Wine("White", "Plume Sav", "Lake Chalice", 2019, 85, "Marlborough", "So tasty");
        boolean success = wineDao.updateWine(newWine, oldWine);

        Assertions.assertFalse(success);
        Assertions.assertTrue(wineDao.getAll().contains(oldWine));
    }

    @Test
    public void testSearchWineSuccess() throws DuplicateExc {
        populateDatabase();
        List<Wine> wines = wineDao.searchWines("Lake Chalice");

        Wine wine1 = new Wine("Red", "Plume Pinot Noir", "Lake Chalice", 2019, 80, "Marlborough", "High quality wine with woody notes");
        Wine wine2 = new Wine("White", "Plume Sav", "Lake Chalice", 2019, 85, "Marlborough", "So tasty");

        Assertions.assertEquals(2, wines.size());
        Assertions.assertTrue(wines.contains(wine1));
        Assertions.assertTrue(wines.contains(wine2));
    }

    @Test
    public void testSearchWineNoWines() throws DuplicateExc {
        populateDatabase();
        List<Wine> wines = wineDao.searchWines("This is a huge string that makes no sense and no wines will have anything to do with it!");
        Assertions.assertEquals(0, wines.size());
    }

    @Test
    public void testAddBatch() {
        Wine wine1 = new Wine("Red", "Plume Pinot Noir", "Lake Chalice", 2019, 80, "Marlborough", "High quality wine with woody notes");
        Wine wine2 = new Wine("White", "Plume Sav", "Lake Chalice", 2019, 85, "Marlborough", "So tasty");
        Wine wine3 = new Wine("Rose", "Rosy Rose", "Lakes Winery", 2020, 90, "Otago", "Very rosy");
        Wine wine4 = new Wine("White", "Bland Blanc", "Fields of Grapes", 2019, 50, "Canterbury", "Bland and boring");

        List<Wine> wines = Arrays.asList(wine1, wine2, wine3, wine4);

        wineDao.addBatch(wines);

        List<Wine> winesFromDB = wineDao.getAll();
        Assertions.assertTrue(winesFromDB.contains(wine1));
        Assertions.assertTrue(winesFromDB.contains(wine2));
        Assertions.assertTrue(winesFromDB.contains(wine3));
        Assertions.assertTrue(winesFromDB.contains(wine4));
    }

    @Test
    public void testAddBatchDuplicate() {
        wineDao.add(new Wine("Red", "Plume Pinot Noir", "Lake Chalice", 2019, 80, "Marlborough", "High quality wine with woody notes"));

        Wine wine1 = new Wine("Red", "Plume Pinot Noir", "Lake Chalice", 2019, 80, "Marlborough", "High quality wine with woody notes");
        Wine wine2 = new Wine("White", "Plume Sav", "Lake Chalice", 2019, 85, "Marlborough", "So tasty");
        Wine wine3 = new Wine("Rose", "Rosy Rose", "Lakes Winery", 2020, 90, "Otago", "Very rosy");
        Wine wine4 = new Wine("White", "Bland Blanc", "Fields of Grapes", 2019, 50, "Canterbury", "Bland and boring");

        List<Wine> wines = Arrays.asList(wine1, wine2, wine3, wine4);

        wineDao.addBatch(wines);

        Assertions.assertEquals(wineDao.getAll().size(), 4);
    }

    @Test
    public void testFilteredWinesWithSearch() throws DuplicateExc {
        populateDatabase();

        filters.put("winery", "Lake Chalice");
        List<Wine> wines = wineDao.getFilteredWines(filters, scoreFilters, "Plume Sav");
        Assertions.assertEquals(1, wines.size());
        Assertions.assertTrue(wines.contains(new Wine("White", "Plume Sav", "Lake Chalice", 2019, 85, "Marlborough", "So tasty")));
    }

    @Test
    public void testRecommendedWinesNoSimilar() {
        Wine wine1 = new Wine("Red", "Plume Pinot Noir", "Lake Chalice", 2019, 80, "Marlborough", "High quality wine with woody notes");
        Wine wine2 = new Wine("White", "Plume Sav", "Grassy Plains", 2020, 85, "Marlborough", "So tasty");
        Wine wine3 = new Wine("White", "Rosy Rose", "Lakes Winery", 2020, 90, "Otago", "Very rosy");
        Wine wine4 = new Wine("White", "Bland Blanc", "Fields of Grapes", 2020, 50, "Canterbury", "Bland and boring");

        wineDao.addBatch(Arrays.asList(wine1, wine2, wine3, wine4));

        List<Wine> wines = wineDao.getSimilarWines(wine1);

        Assertions.assertEquals(3, wines.size());
        Assertions.assertTrue(wines.contains(wine2));
        Assertions.assertTrue(wines.contains(wine3));
        Assertions.assertTrue(wines.contains(wine4));
    }

    @Test
    public void testCheckIfWineExists() {
        Wine wine1 = new Wine("Red", "Plume Pinot Noir", "Lake Chalice", 2019, 80, "Marlborough", "High quality wine with woody notes");
        Wine wine2 = new Wine("White", "Plume Sav", "Grassy Plains", 2020, 85, "Marlborough", "So tasty");
        wineDao.add(wine1);
        Assertions.assertTrue(wineDao.checkIfWineExists(wine1));
        Assertions.assertFalse(wineDao.checkIfWineExists(wine2));
    }
}
