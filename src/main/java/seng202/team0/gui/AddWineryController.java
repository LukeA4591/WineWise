package seng202.team0.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import seng202.team0.services.WineEnvironment;

public class AddWineryController {
    @FXML
    TextField wineryName;
    @FXML
    TextField wineryRegion;
    @FXML
    TextField wineryLatitude;
    @FXML
    TextField wineryLongitude;

    private final WineEnvironment winery;

    public AddWineryController(WineEnvironment winery) {
        this.winery = winery;
    }

    @FXML
    private void goBackToAdmin() {
        winery.getClearRunnable().run();
        winery.launchAdminScreen();
    }

    public void saveNewWinery() {
        try {
            String wineryNameString = wineryName.getText();
            String wineryRegionString = wineryRegion.getText();
            float wineryLatitudeFloat = Float.parseFloat(wineryLatitude.getText());
            float wineryLongitudeFloat = Float.parseFloat(wineryLongitude.getText());
        } catch (NumberFormatException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}