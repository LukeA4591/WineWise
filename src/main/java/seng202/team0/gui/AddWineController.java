package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import seng202.team0.business.WineManager;
import seng202.team0.models.Wine;
import seng202.team0.services.WineEnvironment;

import java.time.Year;


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
    TextField wineDescription;
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

    private void goBackToAdmin() {
        Stage stage = (Stage) wineWineryName.getScene().getWindow();
        stage.close();
    }

    @FXML
    void initialize() {
        wineManager = new WineManager();
    }

    public void saveNewWine() {
        Wine wine = validateWine();
        if (wine != null) {
            wineManager.addWine(wine);
            goBackToAdmin();
        }
    }

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

                saveNewWineMessage.setStyle("-fx-text-fill: #008000");
                saveNewWineMessage.setText("New wine saved.");
                wineWineryName.setText("");
                wineName.setText("");
                wineVintage.setText("");
                wineScore.setText("");
                wineTypeToggle.selectToggle(wineTypeWhite);
                wineRegion.setText("");
                wineDescription.setText("");
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