package seng202.team0.cucumber;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.junit.jupiter.api.BeforeEach;
import seng202.team0.business.WineManager;
import seng202.team0.exceptions.DuplicateExc;
import seng202.team0.gui.SearchPageController;
import seng202.team0.models.Wine;
import seng202.team0.repository.DatabaseManager;
import seng202.team0.services.AppEnvironment;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WineDAOStepDefs {


    private WineManager wineManager;

    private DatabaseManager databaseManager;

    List<Wine> winesToAdd = new ArrayList<>();

    @Before
    public void setup() throws DuplicateExc {
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        wineManager = new WineManager();
    }

    @After
    public void resetDB() {
        databaseManager.resetDB();
        winesToAdd.clear();
    }

    @Given("An admin is on the admin page and has filled in the add wine popup with inputs {string}, {string}, {string}, {int}, {int}, {string}, {string}")
    public void wineValues(String type, String winery, String name, int vintage, int score, String region, String description) {
        Wine wine = new Wine(type, name, winery, vintage, score, region, description);
        System.out.println(wine);
        winesToAdd.add(wine);
    }


    @When("The admin clicks save new wine")
    public void saveWine() {
        for (int i = 0; i < winesToAdd.size(); i++) {
            wineManager.add(winesToAdd.get(i));
        }
    }

    @Then("The wine would be saved to the database")
    public void checkWineInDB() {
        List<Wine> wines = wineManager.getAll();
        assertEquals(winesToAdd.size(), wines.size());
    }

    @Then("The wine duplicate wine is not saved to the database")
    public void checkWineinDB() {
        List<Wine> wines = wineManager.getAll();
        assertEquals(winesToAdd.size()-1, wines.size());
    }
}
