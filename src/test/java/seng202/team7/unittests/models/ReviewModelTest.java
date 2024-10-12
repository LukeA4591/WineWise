package seng202.team7.unittests.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seng202.team7.models.Review;
import seng202.team7.models.Wine;

public class ReviewModelTest {

    private Wine createWine() {
        return new Wine("Red", "A red wine", "Lakes Winery", 2018, 90, "Canterbury", "This is a wine from canterbury");
    }

    private Review createReviewWithId() {
        Wine wine = createWine();
        return new Review(1, 90, "Very nice", wine);
    }

    private Review createReviewWithoutId() {
        Wine wine = createWine();
        return new Review(90, "Very nice", wine);
    }

    @Test
    public void testCreateReviewWithId() {
        Review reviewWithId = createReviewWithId();
        Assertions.assertEquals(1, reviewWithId.getReviewID());
        Assertions.assertEquals(90, reviewWithId.getRating());
        Assertions.assertEquals("Very nice", reviewWithId.getDescription());
        Assertions.assertEquals(createWine(), reviewWithId.getWine());
        reviewWithId.setReported(true);
        Assertions.assertTrue(reviewWithId.getReported());
    }

    @Test
    public void testCreateReviewWithoutId() {
        Review reviewWithoutId = createReviewWithId();
        Assertions.assertEquals(90, reviewWithoutId.getRating());
        Assertions.assertEquals("Very nice", reviewWithoutId.getDescription());
        Assertions.assertEquals(createWine(), reviewWithoutId.getWine());
        Assertions.assertFalse(reviewWithoutId.getReported());
        reviewWithoutId.setReported(true);
        Assertions.assertTrue(reviewWithoutId.getReported());
    }

    @Test
    public void testReviewObjectsEquals() {
        Review firstReviewWithId = createReviewWithId();
        Review secondReviewWithId = createReviewWithId();
        Review reviewWithoutId = createReviewWithoutId();
        Assertions.assertEquals(firstReviewWithId, secondReviewWithId);
        Assertions.assertNotEquals(firstReviewWithId, reviewWithoutId);
    }
}
