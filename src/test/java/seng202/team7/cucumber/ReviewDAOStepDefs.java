package seng202.team7.cucumber;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import seng202.team7.business.ReviewManager;
import seng202.team7.business.WineManager;
import seng202.team7.exceptions.DuplicateExc;
import seng202.team7.models.Review;
import seng202.team7.models.Wine;
import seng202.team7.repository.DatabaseManager;

import java.util.List;

public class ReviewDAOStepDefs {

    private Review review;
    private String description;
    private int rating;
    private ReviewManager reviewManager;

    private WineManager wineManager;

    private DatabaseManager databaseManager;

    private Wine testWine1;

    private Wine testWine2;

    @Before
    public void setup() throws DuplicateExc {
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        reviewManager = new ReviewManager();
        wineManager = new WineManager();
        testWine1 = new Wine("Red", "Plume Pinot Noir", "Lake Chalice", 2019, 80, "Marlborough", "A yummy wine");
        testWine2 = new Wine("White", "Plume Sav", "Lake Chalice", 2019, 85, "Marlborough", "So tasty");
        wineManager.add(testWine1);
        wineManager.add(testWine2);
        databaseManager.resetDB();
    }

    @Given("A user leaves review {string}, and rating {int} on a wine")
    public void addReview(String review, int rating) {
        this.description = review;
        this.rating = rating;
    }

    @When("The user saves the review")
    public void saveReview() throws DuplicateExc {
        this.review = new Review(1, rating, description, testWine1);
        reviewManager.add(review);
    }

    @Then("The review is store in the database")
    public void savedReview() {
        int id = wineManager.getWineID(testWine1);
        List<Review> reviews = reviewManager.getReviewsByWineId(id);
        Assertions.assertEquals(1, reviews.size());
        Assertions.assertEquals(60, reviews.get(0).getRating());
        Assertions.assertEquals("I liked it", reviews.get(0).getDescription());
    }
}
