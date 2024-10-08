package seng202.team7.cucumber;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import seng202.team7.business.WineManager;
import seng202.team7.io.Importable;
import seng202.team7.io.WineCSVImporter;
import seng202.team7.models.Wine;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EditWinesStepDefs {

    private WineManager wineManager = new WineManager();
    private List<Wine> wineList;
    private Wine selectedWine;
    int wineID;
    private Wine updatedWine;
    @Given("The admin has added the dataset {string}")
    public void addDataset(String filename) {
        Importable wineCSVImporter = new WineCSVImporter();
        URL url = Thread.currentThread().getContextClassLoader().getResource("files/" + filename);
        if (url == null) {
            throw new RuntimeException("File not found: " + filename);
        }
        File file = new File(url.getPath());
        List<Integer> headerIndexes = Arrays.asList(5, 1, 0, 4, 2, 3, 6);
        wineList = wineCSVImporter.readFromFile(file, headerIndexes);
        wineManager.addBatch(wineCSVImporter, file, headerIndexes);
    }

    @And("The admin is on the view table page")
    public void viewTable() {
        // Simulating the admin navigating to the view table page
        System.out.println("Admin clicked on the view table button from the admin page.");
        // Since we do not need to test controllers we assume navigation was successful, and no further testing is needed.
    }

    @When("The admin selects the wine: {int} {string} from {string}")
    public void selectWine(int vintage, String wineName, String winery) {
        selectedWine = null;
        for (Wine wine: wineList) {
            if (wine.getVintage() == vintage && wine.getWineName().equals(wineName) && wine.getWineryString().equals(winery)) {
                selectedWine = wine;
                break;
            }
        }
        assertNotNull(selectedWine);
        wineID = wineManager.getWineID(selectedWine);
    }

    @And("The admin edits the wine details with a score of {int} and region {string}")
    public void editWineDetails(int newScore, String newRegion) {
        updatedWine = new Wine(selectedWine.getColor(),
            selectedWine.getWineName(),
            selectedWine.getWineryString(),
            selectedWine.getVintage(),
            newScore,
            newRegion,
            selectedWine.getDescription());
    }

    @And("The admin does not edit any of the wines details.")
    public void uneditedWineDetails() {
        updatedWine = new Wine(selectedWine.getColor(),
                selectedWine.getWineName(),
                selectedWine.getWineryString(),
                selectedWine.getVintage(),
                selectedWine.getScore(),
                selectedWine.getRegion(),
                selectedWine.getDescription());
    }

    @And("The admin clicks Save Wine")
    public void saveWineDetails() {
        // Simulating the admin clicking the 'save wine' button on the edit wines popup.
        // This simulates the EditWineController's check to see if the updated score is in valid ranges.
        if (updatedWine.getScore() <= 100 & updatedWine.getScore() >= 0) {
            wineManager.updateWine(updatedWine, selectedWine);
        }
    }

    @Then("The wine should be updated in both the WineWise system and the wines database table")
    public void assertWineUpdated() {
        Wine wineFromDatabase = wineManager.getWineFromID(wineID);
        assertEquals(updatedWine.getScore(), wineFromDatabase.getScore(), "Score does not match in the database.");
        assertEquals(updatedWine.getRegion(), wineFromDatabase.getRegion(), "Region does not match in the database.");
    }

    @Then("The wine is not updated in the WineWise system or the wines database table")
    public void assertWineNotUpdated() {
        Wine wineFromDatabase = wineManager.getWineFromID(wineID);
        assertNotEquals(updatedWine.getScore(), wineFromDatabase.getScore(), "Score should not have been updated in the database.");
        assertEquals(selectedWine.getRegion(), wineFromDatabase.getRegion(), "Score does not match in the database.");
        assertEquals(updatedWine.getRegion(), wineFromDatabase.getRegion(), "Region does not match in the database.");
    }
}
