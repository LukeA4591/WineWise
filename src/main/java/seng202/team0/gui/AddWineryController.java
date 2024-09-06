package seng202.team0.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
    @FXML
    Label saveNewWineryMessage;

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
            if (wineryNameString.isEmpty()) {
                saveNewWineryMessage.setStyle("-fx-text-fill: #FF0000");
                saveNewWineryMessage.setText("Winery Name field is empty.");
            } else {
                String wineryRegionString = wineryRegion.getText();
                float wineryLatitudeFloat = Float.parseFloat(wineryLatitude.getText());
                float wineryLongitudeFloat = Float.parseFloat(wineryLongitude.getText());

                // Add save winery to database

                saveNewWineryMessage.setStyle("-fx-text-fill: #00FF00");
                saveNewWineryMessage.setText("New winery saved.");
            }
        } catch (NumberFormatException e) {
            saveNewWineryMessage.setStyle("-fx-text-fill: #FF0000");
            saveNewWineryMessage.setText("Winery Latitude and Longitude should be a number.");
        }
    }
}