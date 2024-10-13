package seng202.team7.cucumber;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import seng202.team7.business.WineManager;
import seng202.team7.exceptions.DuplicateExc;
import seng202.team7.models.Wine;
import seng202.team7.repository.DatabaseManager;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class WineDAOStepDefs {


    private WineManager wineManager;

    private DatabaseManager databaseManager;

    List<Wine> winesToAdd = new ArrayList<>();
    List<Wine> generatedWines;

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
        generatedWines = null;
    }

    @Given("An admin is on the admin page and has filled in the add wine popup with inputs {string}, {string}, {string}, {int}, {int}, {string}, {string}")
    public void wineValues(String type, String winery, String name, int vintage, int score, String region, String description) {
        Wine wine = new Wine(type, name, winery, vintage, score, region, description);
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
        assertEquals(winesToAdd.size() - 1, wines.size());
    }

    @Given("A database with the following wines:")
    public void addMultipleWinesToDatabase(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> wines = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> wine : wines) {
            String name = wine.get("wineName");
            String type = wine.get("wineType");
            String winery = wine.get("winery");
            int vintage = Integer.parseInt(wine.get("year"));

            wineManager.add(new Wine(type, name, winery, vintage, 100, null, null));
        }
    }


    @When("The admin deletes a wine with details {string}, {string}, {int}")
    public void adminDeletesWine(String name, String winery, int vintage) {
        wineManager.delete(name, winery, vintage);
    }

    @Then("The wine with details {string}, {string}, {int} is deleted from the database")
    public void wineNoLongerInDatabase(String name, String winery, int vintage) {
        List<Wine> wines = wineManager.getAll();
        Wine wine = new Wine("White", name, winery, vintage, 100, null, null);
        assertFalse(wines.contains(wine));
    }

    @And("The database has a size of {int}")
    public void checkDatabaseSize(int expectedSize) {
        assertEquals(expectedSize, wineManager.getAll().size());
    }

    @And("No other wines have been deleted")
    public void checkOtherWines() {
        Wine wine = new Wine("Red", "Not Famous Wine", "Not Famous Winery", 2000, 0, null, null);
        List<Wine> wines = wineManager.getAll();
        assertTrue(wines.contains(wine));
    }

    @When("The admin updates the wine with details {string}, {string}, {string}, {int} to {string}, {string}, {string}, {int}")
    public void updateWine(String name1, String type1, String winery1, int vintage1, String name2, String type2, String winery2, int vintage2) {
        Wine oldWine = new Wine(type1, name1, winery1, vintage1, 100, null, null);
        Wine newWine = new Wine(type2, name2, winery2, vintage2, 100, null, null);
        wineManager.updateWine(newWine, oldWine);
    }

    @Then("There will be a wine with details {string}, {string}, {string}, {int}")
    public void savedDetails(String name, String type, String winery, int vintage) {
        Wine wine = new Wine(type, name, winery, vintage, 100, null, null);
        assertTrue(wineManager.getAll().contains(wine));
    }

    @When("A user queries for similar wines to the wine with details {string}, {string}, {string}, {int}")
    public void similarWines(String name, String type, String winery, int vintage) {
        Wine wine = new Wine(type, name, winery, vintage, 100, null, null);
        generatedWines = wineManager.getTheSimilarWines(wine);
    }

    @Then("The generated wines will contain the following wines:")
    public void checkSimilarWines(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> wines = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> wine : wines) {
            String name = wine.get("wineName");
            String type = wine.get("wineType");
            String winery = wine.get("winery");
            int vintage = Integer.parseInt(wine.get("year"));

            assertTrue(generatedWines.contains(new Wine(type, name, winery, vintage, 100, null, null)));
        }
    }

    @And("The generated wines will have a size of {int}")
    public void checkSimilarWinesSize(int size) {
        assertEquals(size, generatedWines.size());
    }

    @When("A user selects the following filters:")
    public void filterWines(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> filters = dataTable.asMaps(String.class, String.class);
        Map<String, String> passedFilters = new HashMap<>();
        Map<String, List<String>> scoreFilters = new HashMap<>();
        List<String> criticScore = new ArrayList<>();
        criticScore.add("");
        criticScore.add("");
        scoreFilters.put("score", criticScore);

        for (Map<String, String> filter : filters) {
            String filterName = filter.get("filterName");
            String filterEntry = filter.get("filterEntry");

            passedFilters.put(filterName, filterEntry);
        }

        generatedWines = wineManager.getFilteredWines(passedFilters, scoreFilters, "");
    }

    @And("The list is all wines")
    public void checkList() {
        assertFalse(generatedWines.contains(null));
    }

    @When("a user searches for wines with the search {string}")
    public void search(String search) {
        generatedWines = wineManager.searchWines(search);
    }
}
