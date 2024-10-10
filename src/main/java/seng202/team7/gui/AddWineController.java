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
    }

    /**
     * Sends the new wine to the wineManager when the save wine button is pressed if all the wine values filled in the
     * text fields were all valid.
     */
    public void saveNewWine() {
        Wine wine = validateWine();
        if (wine != null) {

            if (!wineManager.checkIfWineExists(wine)) {
                confirmChangeUI();
                wineManager.add(wine);
                Platform.runLater(this::closePage);


            } else {
                saveNewWineMessage.setText("Wine Already Exists in the Database");
                saveNewWineMessage.setStyle("-fx-text-fill: #FF0000");
            }
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

    @FXML
    private void closePressed() {
        Stage stage = (Stage) (wineName.getScene().getWindow());
        stage.close();
    }

    /**
     * Checks to see if the text fields were all filled with valid inputs. If at least one of the text fields were not
     * filled in correctly, it will set an error message with a description of the error. If all the fields were
     * correct, it will tell the user that the wine has been saved.
     * @return Wine if the text fields were all valid, null if at least one of them failed
     */
    private Wine validateWine() {
        try {
            String wineWineryNameString = wineWineryName.getText();
            String wineNameString = wineName.getText();
            if (wineWineryNameString.isEmpty()) {
                saveNewWineMessage.setStyle("-fx-text-fill: #FF0000");
                saveNewWineMessage.setText("Winery Name field is empty.");
                return null;
            } else if (wineNameString.isEmpty()){
                saveNewWineMessage.setStyle("-fx-text-fill: #FF0000");
                saveNewWineMessage.setText("Wine Name field is empty.");
                return null;
            } else if (wineVintage.getText().isEmpty()) {
                saveNewWineMessage.setStyle("-fx-text-fill: #FF0000");
                saveNewWineMessage.setText("Wine Vintage field is empty.");
                return null;
            } else {
                int wineVintageInt = Integer.parseInt(wineVintage.getText());
                if (wineVintageInt < 0 || wineVintageInt > Year.now().getValue()) {
                    saveNewWineMessage.setStyle("-fx-text-fill: #FF0000");
                    saveNewWineMessage.setText("Vintage should be between 0 and the current year.");
                    return null;
                }
                int wineScoreInt;
                if (wineScore.getText().isEmpty()) {
                    wineScoreInt = 0;
                } else {
                    wineScoreInt = Integer.parseInt(wineScore.getText());
                    if (wineScoreInt < 0 || wineScoreInt > 100) {
                        saveNewWineMessage.setStyle("-fx-text-fill: #FF0000");
                        saveNewWineMessage.setText("Score should be between 0-100.");
                        return null;
                    }
                }
                String wineTypeString = ((RadioButton) wineTypeToggle.getSelectedToggle()).getText();
                String wineRegionString = wineRegion.getText();
                String wineDescriptionString = wineDescription.getText();

                if (wineWineryNameString.length() > 100 | wineNameString.length() > 100 |
                        wineRegionString.length() > 100 | wineDescriptionString.length() > 1000) {
                    saveNewWineMessage.setStyle("-fx-text-fill: #FF0000");
                    saveNewWineMessage.setText("Text fields are too long.");
                    return null;
                }


                return new Wine(wineTypeString, wineNameString, wineWineryNameString, wineVintageInt, wineScoreInt,
                        wineRegionString, wineDescriptionString);
            }
        } catch (NumberFormatException e) {
            saveNewWineMessage.setStyle("-fx-text-fill: #FF0000");
            saveNewWineMessage.setText("Wine Vintage and Score should be a number.");
            return null;
        }
    }
}