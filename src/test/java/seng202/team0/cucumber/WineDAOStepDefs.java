package seng202.team0.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.junit.Before;
import seng202.team0.business.WineManager;
import seng202.team0.gui.AdminSetupScreenController;
import seng202.team0.gui.SearchPageController;
import seng202.team0.models.Wine;
import seng202.team0.services.AdminLoginService;
import seng202.team0.services.AppEnvironment;

import java.io.File;

public class WineDAOStepDefs {

    private AppEnvironment appEnvironment;
    private SearchPageController searchPageController;

    private WineManager wineManager;

    private String type;
    private String winery;
    private String name;
    private int vintage;
    private int score;
    private String region;
    private String description;

    @Before
    public void setup() {

    }

    @Given("An admin is on the admin page and has filled in the add wine popup with a valid {string}, {string}, {int}, ")
    public void wineValues(String type, String winery, String name, int vintage, int score, String region, String description) {
        this.type = type;
        this.winery = winery;
        this.name = name;
        this.vintage = vintage;
        this.score = score;
        this.region = region;
        this.description = description;
    }

    @When("The admin clicks save new wine")
    public void saveWine() {
        wineManager.add(new Wine(type, name, winery, vintage, score, region, description));
    }
}
