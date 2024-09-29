package seng202.team0.cucumber;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import seng202.team0.business.WineManager;
import seng202.team0.io.Importable;
import seng202.team0.io.WineCSVImporter;
import seng202.team0.models.Wine;

import java.io.File;
import java.net.URL;
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
        wineList = wineCSVImporter.readFromFile(file);
        wineManager.addBatch(wineCSVImporter, file);
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

    @And("The admin clicks Save Wine")
    public void saveWineDetails() {
        // Simulating the admin clicking the 'save wine' button on the edit wines popup.
        wineManager.updateWine(updatedWine, selectedWine);
    }

    @Then("The wine should be updated in both the WineWise system and the wines database table")
    public void updateTable() {
        Wine wineFromDatabase = wineManager.getWineFromID(wineID);
        assertEquals(updatedWine.getScore(), wineFromDatabase.getScore(), "Score does not match in the database.");
        assertEquals(updatedWine.getRegion(), wineFromDatabase.getRegion(), "Region does not match in the database.");
    }
}
