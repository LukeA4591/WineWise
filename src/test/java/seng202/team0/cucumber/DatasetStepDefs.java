package seng202.team0.cucumber;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import seng202.team0.io.Importable;
import seng202.team0.io.WineCSVImporter;
import seng202.team0.models.Wine;

import java.io.File;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatasetStepDefs {

    private List<Wine> wineList;

    @Given("A small wine dataset {string}")
    public void uploadDataset(String filename) {
        Importable wineCSVImporter = new WineCSVImporter();
        URL url = Thread.currentThread().getContextClassLoader().getResource("files/" + filename);
        assertTrue(url != null);
        File file = new File(url.getPath());
        wineList = wineCSVImporter.readFromFile(file);
    }

    @When("The wine dataset is imported")
    public void scanDataset() {
        assertTrue(wineList != null && !wineList.isEmpty());
    }
    @Then("A list of wines should be returned")
    public void wineListReturned() {
        assertTrue(wineList instanceof List<Wine>);
    }

    @And("The length of the list should be {int}")
    public void lengthList(int expectedLength) {
        assertEquals(wineList.size(), expectedLength);
    }
}
