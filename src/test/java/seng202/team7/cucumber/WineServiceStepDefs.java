package seng202.team7.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.Before;
import seng202.team7.services.WineService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WineServiceStepDefs {

    private WineService wineService;
    private String errorMessage;
    private String winery;
    private String name;
    private String vintage;
    private String score;
    private String region;
    private String description;

    @Before
    public void setup(){
        wineService = new WineService();
    }

    @Given("A user inputting the text {string}, {string}, {string}, {string}, {string}, {string}")
    public void userInputtingData(String winery, String name, String vintage, String score, String region, String description) {
        this.winery = winery;
        this.name = name;
        this.vintage = vintage;
        this.score = score;
        this.region = region;
        this.description = description;
    }

    @When("The user saves the wine")
    public void userSavesWine() {
        this.errorMessage = wineService.validateWine(name, winery, vintage, score, region, description);
    }

    @Then("The error message will be {string}")
    public void checkErrorMessage(String expectedError) {
        assertEquals(expectedError, errorMessage);
    }
}
