package seng202.team7.unittests.business;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team7.business.ReviewManager;
import seng202.team7.business.WineManager;
import seng202.team7.exceptions.DuplicateExc;
import seng202.team7.models.Review;
import seng202.team7.models.Wine;
import seng202.team7.repository.DatabaseManager;

public class ReviewManagerTest {
    private static ReviewManager reviewManager;
    private static WineManager wineManager;
    private static DatabaseManager databaseManager;

    private Wine wine1;

    @BeforeAll
    public static void setup() throws DuplicateExc {
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        reviewManager = new ReviewManager();
        wineManager = new WineManager();
    }

    @BeforeEach
    public void freshDatabase() {
        databaseManager.resetDB();
    }

    private void populateReviewsThroughManager() {
        try {
            wine1 = new Wine("Red", "A red wine", "Lakes Winery", 2018, 90, "Canterbury", "This is a wine from canterbury");
            Wine wine2 = new Wine("White", "A white wine", "Lakes Winery 2", 2020, 60, "Canterbury", "This is a wine from canterbury");
            Wine wine3 = new Wine("Red", "A red wine", "Craggy", 2004, 95, "Canterbury", "This is a wine from canterbury");
            wineManager.add(wine1);
            wineManager.add(wine2);
            wineManager.add(wine3);
            reviewManager.add(new Review(90, "I really liked this wine from reviewer 1 :D", wine1));
            reviewManager.add(new Review(85, "I also really liked this wine from reviewer 2 :D", wine1));
            reviewManager.add(new Review(45, "I really did not like this wine from reviewer 2 :(", wine2));
            reviewManager.add(new Review(95, "I like wine from reviewer 3 >:D", wine3));
        } catch (DuplicateExc e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tests any ReviewManagers point to the same instance of ReviewDao
     */
    @Test
    public void testSameInstance() {
        ReviewManager anotherReviewManager = new ReviewManager();
        Wine wine1 = new Wine("Red", "A red wine", "Lakes Winery", 2018, 90, "Canterbury", "This is a wine from canterbury");
        try {
            reviewManager.add(new Review(90, "I really liked this wine from reviewer 1 :D", wine1));
            anotherReviewManager.add(new Review(80, "I also REALLY really liked this wine from reviewer 1 :D", wine1));
        } catch (DuplicateExc e) {
            throw new RuntimeException(e);
        }

        Assertions.assertEquals(2, reviewManager.getAll().size());
    }

    /**
     * Flags one review and tests flagged reviews list is empty before -> then has a size of one after flagging 1 review
     */
    @Test
    public void testMarkAsReportedANDUnreported() {
        populateReviewsThroughManager();
        Assertions.assertEquals(0, reviewManager.getFlaggedReviews().size());
        reviewManager.markAsReported(1);
        Assertions.assertEquals(1, reviewManager.getFlaggedReviews().size());
        reviewManager.markAsUnreported(1);
        Assertions.assertEquals(0, reviewManager.getFlaggedReviews().size());
    }

    @Test
    public void testGetReviewsByWineID() {
        populateReviewsThroughManager();
        Assertions.assertEquals(2, reviewManager.getReviewsByWineId(wineManager.getWineID(wine1)).size());
    }

    @Test
    public void testDelete() {
        populateReviewsThroughManager();
        Assertions.assertEquals(4, reviewManager.getAll().size());
        reviewManager.delete(1);
        Assertions.assertEquals(3, reviewManager.getAll().size());
    }

}
