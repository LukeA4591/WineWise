package seng202.team7.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import seng202.team7.business.WineryManager;
import seng202.team7.models.Winery;

/**
 * Controller for the add_winery.fxml file
 */
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

    /**
     * Default constructor for add winery controller
     */
    public AddWineryController() {

    }

    /**
     * closes the add winery pop-up
     */
    private void goBackToAdmin() {
        Stage stage = (Stage) wineryName.getScene().getWindow();
        stage.close();
    }

    /**
     * Creates an instance of wineryManager when the controller is initialised.
     */
    @FXML
    void initialize() {
        wineryManager = new WineryManager();
    }

    /**
     * Calls validate winery, and if that function returns a non-null winery it adds it to the
     * db through the manager
     */
    public void saveNewWinery() {
        Winery winery = validateWinery();
        if (winery != null) {
            wineryManager.add(winery);
            goBackToAdmin();
        }
    }

    /**
     * Function that checks the global variables if they are valid inputs for a winery
     * @return Winery that has passed all checks, if not it's null
     */
    private Winery validateWinery() {
        String setLabelRed = "-fx-text-fill: #ff0000";
        try {
            String wineryNameString = wineryName.getText();
            if (wineryNameString.isEmpty()) {
                saveNewWineryMessage.setStyle(setLabelRed);
                saveNewWineryMessage.setText("Name field is empty.");
                return null;
            } else if (wineryManager.getWineryByName(wineryNameString) != null) {
                saveNewWineryMessage.setStyle(setLabelRed);
                saveNewWineryMessage.setText("Winery name exists.");
                return null;
            } else {
                return validateWineryLongLat(wineryNameString);
            }
        } catch (NumberFormatException e) {
            saveNewWineryMessage.setStyle(setLabelRed);
            saveNewWineryMessage.setText("Longitude and latitude should be a float.");
            return null;
        }
    }

    private Winery validateWineryLongLat(String wineryNameString) {
        String setLabelRed = "-fx-text-fill: #ff0000";
        Float wineryLongitudeFloat = null;
        Float wineryLatitudeFloat = null;
        if (!wineryLongitude.getText().isEmpty() && !wineryLatitude.getText().isEmpty()) {
            wineryLongitudeFloat = Float.parseFloat(wineryLongitude.getText());
            wineryLatitudeFloat = Float.parseFloat(wineryLatitude.getText());
            if (wineryLongitudeFloat < -180 || wineryLongitudeFloat >= 180) {
                saveNewWineryMessage.setStyle(setLabelRed);
                saveNewWineryMessage.setText("Longitude should be >= -180 and less than 180.");
                return null;
            }
            if (wineryLatitudeFloat < -90 || wineryLatitudeFloat > 90) {
                saveNewWineryMessage.setStyle(setLabelRed);
                saveNewWineryMessage.setText("Latitude should be between -90 and 90.");
                return null;
            }
        } else if (!wineryLongitude.getText().isEmpty() || !wineryLatitude.getText().isEmpty()) {
            saveNewWineryMessage.setStyle(setLabelRed);
            saveNewWineryMessage.setText("Enter a latitude and longitude.");
            return null;
        } else {
            saveNewWineryMessage.setStyle("-fx-text-fill: #008000");
            saveNewWineryMessage.setText("New winery saved.");
            wineryName.setText("");
            wineryLongitude.setText("");
            wineryLatitude.setText("");
        }
        return new Winery(wineryNameString, wineryLongitudeFloat, wineryLatitudeFloat);
    }
}
