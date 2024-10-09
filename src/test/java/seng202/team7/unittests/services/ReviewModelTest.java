package seng202.team7.unittests.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seng202.team7.models.Review;
import seng202.team7.models.Wine;

public class ReviewModelTest {

    @Test
    public void testBasicConstructor() {
        Wine wine = new Wine("Red", "A red wine", "Lakes Winery", 2018, 90, "Canterbury", "This is a wine from canterbury");
        Review review = new Review(90, "I thought this was really nice", wine);
        Assertions.assertEquals(90, review.getRating());
        Assertions.assertEquals("I thought this was really nice", review.getDescription());
        Assertions.assertEquals(review.getWine(), wine);
    }

    @Test()
    public void testIDConstructor() {
        Wine wine = new Wine("Red", "A red wine", "Lakes Winery", 2018, 90, "Canterbury", "This is a wine from canterbury");
        Review review = new Review(2,90, "I thought this was really nice", wine);
        Assertions.assertEquals(2, review.getReviewID());
        Assertions.assertEquals(90, review.getRating());
        Assertions.assertEquals("I thought this was really nice", review.getDescription());
        Assertions.assertEquals(review.getWine(), wine);
    }

}
