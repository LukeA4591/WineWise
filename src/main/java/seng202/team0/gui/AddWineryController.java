package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import seng202.team0.business.WineryManager;
import seng202.team0.models.Winery;

public class AddWineryController {

    @FXML
    TextField wineryName;
    @FXML
    TextField wineryLongitude;
    @FXML
    TextField wineryLatitude;
    @FXML
    Label saveNewWineryMessage;

    private WineryManager wineryManager;

    private void goBackToAdmin() {
        Stage stage = (Stage) wineryName.getScene().getWindow();
        stage.close();
    }

    @FXML
    void initialize() {
        wineryManager = new WineryManager();
    }

    public void saveNewWinery() {
        Winery winery = validateWinery();
        if (winery != null) {
            wineryManager.add(winery);
            goBackToAdmin();
        }
    }

    private Winery validateWinery() {
        try {
            String wineryNameString = wineryName.getText();
            if (wineryNameString.isEmpty()) {
                saveNewWineryMessage.setStyle("-fx-text-fill: #FF0000");
                saveNewWineryMessage.setText("Name field is empty.");
                return null;
            } else {
                Float wineryLongitudeFloat = null;
                Float wineryLatitudeFloat = null;
                if (!wineryLongitude.getText().isEmpty()) {
                    wineryLongitudeFloat = Float.parseFloat(wineryLongitude.getText());
                    if (wineryLongitudeFloat < -180 || wineryLongitudeFloat >= 180) {
                        saveNewWineryMessage.setStyle("-fx-text-fill: #FF0000");
                        saveNewWineryMessage.setText("Longitude should be >= -180 and less than 180.");
                        return null;
                    }
                }
                if (!wineryLatitude.getText().isEmpty()) {
                    wineryLatitudeFloat = Float.parseFloat(wineryLatitude.getText());
                    if (wineryLatitudeFloat < -90 || wineryLatitudeFloat > 90) {
                        saveNewWineryMessage.setStyle("-fx-text-fill: #FF0000");
                        saveNewWineryMessage.setText("Latitude should be between -90 and 90.");
                        return null;
                    }
                }
                saveNewWineryMessage.setStyle("-fx-text-fill: #008000");
                saveNewWineryMessage.setText("New winery saved.");
                wineryName.setText("");
                wineryLongitude.setText("");
                wineryLatitude.setText("");
                return new Winery(wineryNameString, wineryLongitudeFloat, wineryLatitudeFloat);
            }
        } catch (NumberFormatException e) {
            saveNewWineryMessage.setStyle("-fx-text-fill: #FF0000");
            saveNewWineryMessage.setText("Longitude and latitude should be a float.");
            return null;
        }
    }
}
