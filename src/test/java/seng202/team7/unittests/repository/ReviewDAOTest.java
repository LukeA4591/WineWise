package seng202.team7.unittests.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team7.exceptions.DuplicateExc;
import seng202.team7.models.Review;
import seng202.team7.models.Wine;
import seng202.team7.repository.ReviewDAO;
import seng202.team7.repository.DatabaseManager;
import seng202.team7.repository.WineDAO;

import java.util.LinkedHashMap;
import java.util.List;

public class ReviewDAOTest {
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
     * Populates the test database with reviews
     */
    private void populateDatabase() throws DuplicateExc {
        reviewDao.add(new Review(60, "I thought it was really good, I liked the color", testWine1));
        reviewDao.add(new Review(90, "This blew my socks off", testWine1));
        reviewDao.add(new Review(30, "Actually horrendous", testWine2));
    }

    @Test
    public void testGetAll() throws DuplicateExc {
        populateDatabase();
        List<Review> reviews = reviewDao.getAll();
        Assertions.assertEquals(3, reviews.size());
    }

    @Test
    public void testGetReviewsFromWine() throws DuplicateExc {
        populateDatabase();
        List<Review> reviews = reviewDao.getReviewsByWineId(wineDAO.getWineID(testWine1));
        Assertions.assertEquals(2, reviews.size());
    }

    @Test
    public void testAddReview() throws DuplicateExc {
        Review newReview = new Review(85, "Very good wine", testWine1);
        int insertedId = reviewDao.add(newReview);
        Assertions.assertTrue(insertedId > 0);

        List<Review> reviews = reviewDao.getAll();
        Assertions.assertEquals(1, reviews.size());
        Assertions.assertEquals(newReview.getRating(), reviews.get(0).getRating());
    }

    @Test
    public void testDeleteReview() throws DuplicateExc {
        populateDatabase();
        List<Review> reviews = reviewDao.getAll();
        int reviewId = reviews.get(0).getReviewID();
        reviewDao.delete(reviewId);
        List<Review> updatedReviews = reviewDao.getAll();
        Assertions.assertEquals(2, updatedReviews.size());
        Assertions.assertTrue(updatedReviews.stream().noneMatch(review -> review.getReviewID() == reviewId));
    }

    @Test
    public void testMarkAsReported() throws DuplicateExc {
        populateDatabase();
        List<Review> reviews = reviewDao.getAll();
        int reviewId = reviews.get(0).getReviewID();
        reviewDao.markAsReported(reviewId);
        List<Review> flaggedReviews = reviewDao.getFlaggedReviews();
        Assertions.assertEquals(1, flaggedReviews.size());
        Assertions.assertEquals(reviewId, flaggedReviews.get(0).getReviewID());
    }

    @Test
    public void testMarkAsUnreported() throws DuplicateExc{
        populateDatabase();
        List<Review> reviews = reviewDao.getAll();
        int reviewId = reviews.get(0).getReviewID();
        reviewDao.markAsReported(reviewId);
        reviewDao.markAsUnreported(reviewId);
        List<Review> flaggedReviews = reviewDao.getFlaggedReviews();
        Assertions.assertTrue(flaggedReviews.isEmpty());
    }

    @Test
    public void testGetFlaggedReviews() throws DuplicateExc {
        populateDatabase();
        List<Review> reviews = reviewDao.getAll();
        int reviewId = reviews.get(0).getReviewID();
        reviewDao.markAsReported(reviewId);
        List<Review> flaggedReviews = reviewDao.getFlaggedReviews();
        Assertions.assertEquals(reviewId, flaggedReviews.get(0).getReviewID());
    }

    @Test
    public void testAverageReviews() throws DuplicateExc {
        populateDatabase();
        LinkedHashMap<Integer, Integer> averageReviews = reviewDao.getAverageReviews(0);
        Assertions.assertEquals(75, averageReviews.get(wineDAO.getWineID(testWine1)));
        Assertions.assertEquals(30, averageReviews.get(wineDAO.getWineID(testWine2)));
    }

    @Test
    public void testNumWinesWithReviews() throws DuplicateExc {
        populateDatabase();
        int numWines = reviewDao.getNumWinesWithReviews();
        Assertions.assertEquals(wineDAO.getAll().size(), numWines);
    }
}
