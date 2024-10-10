package seng202.team7.cucumber;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
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
import io.cucumber.datatable.DataTable;

import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertTrue;


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

    @Given("A user flags the following reviews on a wine with details {string}, {string}, {int}:")
    public void userFlagsReviews(String wineName, String wineryName, int vintage, DataTable dataTable) throws DuplicateExc {
        Wine wine = new Wine("White", wineName, wineryName, vintage, 100, null, null);
        wineManager.add(wine);
        List<Map<String, String>> reviews = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> review1 : reviews) {
            String description = review1.get("description");
            int rating = Integer.parseInt(review1.get("rating"));
            int id = Integer.parseInt(review1.get("id"));

            Review review2 = new Review(id, rating, description, wine);
            reviewManager.add(review2);
            reviewManager.markAsReported(id);
        }
    }

    @When("The admin unflags the following reviews:")
    public void adminUnflagsReviews(DataTable dataTable) {
        List<Map<String, String>> reviews = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> review1 : reviews) {
            int id = Integer.parseInt(review1.get("id"));
            reviewManager.markAsUnreported(id);
        }
    }

    @Then("The following reviews are unflagged on a wine with details {string}, {string}, {int}:")
    public void checkUnflaggedReviews(String wineName, String wineryName, int vintage, DataTable dataTable) {
        Wine wine = new Wine("White", wineName, wineryName, vintage, 100, null, null);
        List<Review> reviewsOnWine = reviewManager.getReviewsByWineId(wineManager.getWineID(wine));

        List<Map<String, String>> reviews = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> review1 : reviews) {
            int id = Integer.parseInt(review1.get("id"));
            String description = review1.get("description");
            int rating = Integer.parseInt(review1.get("rating"));
            Review review2 = new Review(id, rating, description, wine);

            assertTrue(reviewsOnWine.contains(review2));
        }
    }

    @And("The flagged reviews contain the following reviews on a wine with details {string}, {string}, {int}:")
    public void checkFlaggedReviews(String wineName, String wineryName, int vintage, DataTable dataTable) {
        Wine wine = new Wine("White", wineName, wineryName, vintage, 100, null, null);
        List<Review> flaggedReviews = reviewManager.getFlaggedReviews();

        List<Map<String, String>> reviews = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> review1 : reviews) {
            int id = Integer.parseInt(review1.get("id"));
            String description = review1.get("description");
            int rating = Integer.parseInt(review1.get("rating"));
            Review review2 = new Review(id, rating, description, wine);

            assertTrue(flaggedReviews.contains(review2));
        }
    }

    @When("The admin deletes the following reviews:")
    public void adminDeletesReviews(DataTable dataTable) {
        List<Map<String, String>> reviews = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> review1 : reviews) {
            int id = Integer.parseInt(review1.get("id"));
            reviewManager.delete(id);
        }
    }
}
