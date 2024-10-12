package seng202.team7.cucumber;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import seng202.team7.exceptions.DuplicateExc;
import seng202.team7.models.Winery;
import seng202.team7.repository.DatabaseManager;
import seng202.team7.repository.WineryDAO;

import java.util.Objects;

public class WineryDAOStepDefs {
    private WineryDAO wineryDAO;
    private DatabaseManager databaseManager;
    private String selectedWinery;
    private Winery winery1;
    private Winery winery2;
    private Winery winery3;
    float Lat;
    float Lng;

    @Before
    public void setup() throws DuplicateExc {
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        wineryDAO = new WineryDAO();
        winery1 = new Winery("Winery 1", null, null);
        winery2 = new Winery("Winery 2", (float) 1.0, (float) -2.0);
        winery3 = new Winery("Rose", (float) 3.0, (float) 1.0);
        wineryDAO.add(winery1);
        wineryDAO.add(winery2);
        wineryDAO.add(winery3);
    }

    @After
    public void reset() {
        databaseManager.resetDB();
    }

    @Given("An admin is on the place winery page and they place are winery either by address or clicking on the map at Latitude: {int} and Longitude: {int}")
    public void wineryLatLng(float Lat, float Lng) {
        this.Lat = Lat;
        this.Lng = Lng;
    }


    @When("The admin selects the winery to place")
    public void selectWinery() {
        selectedWinery = "Winery 1";
    }

    @Then("The Winery is updated to have that location")
    public void updateLocation() {
        wineryDAO.updateLocationByWineryName(selectedWinery, Lat, Lng);
        Assertions.assertEquals(wineryDAO.getWineryByName(selectedWinery).getLatitude(), 10);
        Assertions.assertEquals(wineryDAO.getWineryByName(selectedWinery).getLongitude(), 5);
    }

    @Given("An admin selects winery {string} to remove location")
    public void selectWineryToRemove(String winery) {
        selectedWinery = winery;
    }

    @Then("They confirm to remove the winery")
    public void removeWineryLocation() {
        wineryDAO.updateLocationByWineryName(selectedWinery, null, null);
    }

    @Then("The wineries location is set to null, null")
    public void checkLocation() {
        Assertions.assertEquals(wineryDAO.getWineryByName(selectedWinery).getLatitude(), null);
        Assertions.assertEquals(wineryDAO.getWineryByName(selectedWinery).getLongitude(), null);
    }

    @Given("An admin enters a new winery {string} with latitude {string} and longitude {string}")
    public void addNewWinery(String wineryName, String lat, String lng) {
        if (Objects.equals(lat, "null") && Objects.equals(lng, "null")) {
            wineryDAO.add(new Winery(wineryName, null, null));
        }
    }

    @When("They enter a location for {string} by address or clicking on the map at lat: {int}, lng: {int}")
    public void addLocation(String wineryName, int lat, int lng) {
        wineryDAO.updateLocationByWineryName(wineryName, (float) lat, (float) lng);
    }

    @Then("The winery {string} is added to the database with the correct latitude and longitude")
    public void checkWinery(String wineryName) {
        Winery winery = wineryDAO.getWineryByName(wineryName);
        Assertions.assertEquals(winery.getLatitude(), 70);
        Assertions.assertEquals(winery.getLongitude(), 50);
    }

}
