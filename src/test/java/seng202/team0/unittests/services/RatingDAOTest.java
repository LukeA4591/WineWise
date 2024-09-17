package seng202.team0.unittests.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team0.exceptions.DuplicateExc;
import seng202.team0.models.Rating;
import seng202.team0.models.Wine;
import seng202.team0.repository.ReviewDAO;
import seng202.team0.repository.DatabaseManager;
import seng202.team0.repository.WineDAO;

import java.util.List;

public class RatingDAOTest {
    private static ReviewDAO reviewDao;
    private static WineDAO wineDAO;
    private static DatabaseManager databaseManager;
    private Wine testWine1 = new Wine("Red", "Plume Pinot Noir", "Lake Chalice", 2019, 80, "Marlborough", "A yummy wine");
    private Wine testWine2 = new Wine("White", "Plume Sav", "Lake Chalice", 2019, 85, "Marlborough", "So tasty");

    @BeforeAll
    public static void setup() throws DuplicateExc {
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        reviewDao = new ReviewDAO();
        wineDAO = new WineDAO();
    }

    @BeforeEach
    public void resetDB() {
        databaseManager.resetDB();
        wineDAO.add(testWine1);
        wineDAO.add(testWine2);
    }

    /**
     * Populates the test database with ratings
     */
    private void populateDatabase() throws DuplicateExc {
        reviewDao.add(new Rating(60, "I thought it was really good, I liked the color", testWine1));
        reviewDao.add(new Rating(90, "This blew my socks off", testWine1));
        reviewDao.add(new Rating(30, "Actually horrendous", testWine2));
    }

    @Test
    public void testGetAll() throws DuplicateExc {
        populateDatabase();
        List<Rating> ratings = reviewDao.getAll();
        Assertions.assertEquals(3, ratings.size());
    }

    @Test
    public void testGetRatingsFromWine() throws DuplicateExc {
        populateDatabase();
        List<Rating> ratings = reviewDao.getReviewsByWineId(reviewDao.getWineID(testWine1));
        Assertions.assertEquals(2, ratings.size());
    }

}
