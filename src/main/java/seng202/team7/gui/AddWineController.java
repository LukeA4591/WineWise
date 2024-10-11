package seng202.team7.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import seng202.team7.business.WineManager;
import seng202.team7.models.Wine;
import seng202.team7.services.WineService;

import java.time.Year;

/**
 * Controller class for the add_wine.fxml page.
 */
public class AddWineController {

    @FXML
    TextField wineWineryName;
    @FXML
    TextField wineName;
    @FXML
    TextField wineVintage;
    @FXML
    TextField wineScore;
    @FXML
    TextField wineRegion;
    @FXML
    TextArea wineDescription;
    @FXML
    Label saveNewWineMessage;
    @FXML
    RadioButton wineTypeWhite;
    @FXML
    RadioButton wineTypeRed;
    @FXML
    RadioButton wineTypeRose;
    @FXML
    ToggleGroup wineTypeToggle;
    private WineManager wineManager;
    private WineService wineService;
    private final String setLabelRed = "-fx-text-fill: FF0000";

    private int newWineScore;

    /**
     * Method for going back to admin when the go back button is pressed.
     * **/
    private void goBackToAdmin() {
        Stage stage = (Stage) wineWineryName.getScene().getWindow();
        stage.close();
    }

    /**
     * Creates an instance of wineManager when the controller is initialised.
     * **/
    @FXML
    void initialize() {
        wineManager = new WineManager();
        wineService = new WineService();
    }

    /**
     * Sends the new wine to the wineManager when the save wine button is pressed if all the wine values filled in the
     * text fields were all valid.
     */
    public void saveNewWine() {
        String wineNameString = wineName.getText();
        String wineryNameString = wineWineryName.getText();
        String wineVintageString = wineVintage.getText();
        String wineRegionString = wineRegion.getText();
        String wineScoreString = wineScore.getText();
        String wineDescriptionString = wineDescription.getText();
        String wineTypeString = ((RadioButton) wineTypeToggle.getSelectedToggle()).getText();
        String errorLabel = wineService.validateWine(wineNameString, wineryNameString, wineVintageString, wineScoreString, wineRegionString, wineDescriptionString);
        if (errorLabel.isEmpty()) {

            if (wineScoreString.isBlank()) {
                wineScoreString = null;
            } else {
                newWineScore = Integer.parseInt(wineScoreString);
            }

            Wine wine = new Wine(wineTypeString, wineNameString, wineryNameString, Integer.parseInt(wineVintageString), newWineScore, wineRegionString, wineDescriptionString);

            if (!wineManager.checkIfWineExists(wine)) {
                confirmChangeUI();
                wineManager.add(wine);
                Platform.runLater(this::closePage);

            } else {
                saveNewWineMessage.setText("Wine Already Exists in the Database");
                saveNewWineMessage.setStyle(setLabelRed);
            }
        } else {
            saveNewWineMessage.setText(errorLabel);
            saveNewWineMessage.setStyle(setLabelRed);
        }
    }

    private void confirmChangeUI() {
        saveNewWineMessage.setStyle("-fx-text-fill: #008000");
        saveNewWineMessage.setText("New wine saved.");
        wineWineryName.setText("");
        wineName.setText("");
        wineVintage.setText("");
        wineScore.setText("");
        wineTypeToggle.selectToggle(wineTypeWhite);
        wineRegion.setText("");
        wineDescription.setText("");
    }

    private void closePage() {
        Platform.runLater(() -> {
            try {
                Thread.sleep(1500);
                goBackToAdmin();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Tied to UI button "Cancel" to close out the Add Wine Page
     */
    @FXML
    private void closePressed() {
        Stage stage = (Stage) (wineName.getScene().getWindow());
        stage.close();
    }
}