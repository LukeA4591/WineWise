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

import java.util.List;

public class RatingDAOTest {
    private static ReviewDAO reviewDao;
    private static DatabaseManager databaseManager;

    private Wine testWine1 = new Wine("Red", "Plume Pinot Noir", "Lake Chalice", 2019, 80, "Marlborough", "High quality wine with woody notes");
    private Wine testWine2 = new Wine("White", "Plume Sav", "Lake Chalice", 2019, 85, "Marlborough", "Lake Chalice's pride and joy");

    @BeforeAll
    public static void setup() throws DuplicateExc {
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        reviewDao = new ReviewDAO();
    }

    @BeforeEach
    public void resetDB() {
        databaseManager.resetDB();
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

}
