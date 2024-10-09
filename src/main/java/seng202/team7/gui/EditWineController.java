package seng202.team7.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import seng202.team7.business.WineManager;
import seng202.team7.models.Wine;

import java.time.Year;

public class EditWineController {

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
    private Wine origionalWine;

    public EditWineController(Wine wine) {
        this.origionalWine = wine;
        this.wineManager = new WineManager();
    }

    @FXML
    public void initialize() {
        wineWineryName.setText(origionalWine.getWineryString());
        wineName.setText(origionalWine.getWineName());
        wineVintage.setText(Integer.toString(origionalWine.getVintage()));
        wineScore.setText(Integer.toString(origionalWine.getScore()));
        wineRegion.setText(origionalWine.getRegion());
        wineDescription.setText(origionalWine.getDescription());
        if (origionalWine.getColor().equals("Red")) {
            wineTypeRed.setSelected(true);
        } else if (origionalWine.getColor().equals("White")) {
            wineTypeWhite.setSelected(true);
        } else {
            wineTypeWhite.setSelected(true);
        }
    }

    @FXML
    public void saveWine() {
        Wine wine = validateWine();
        if (wine != null) {
            Stage stage = (Stage) wineWineryName.getScene().getWindow();
            stage.close();
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

                Wine wine = new Wine(wineTypeString, wineNameString, wineWineryNameString, wineVintageInt, wineScoreInt,
                        wineRegionString, wineDescriptionString);
                boolean successfulUpdate = wineManager.updateWine(wine, this.origionalWine);
                if (!successfulUpdate) {
                    saveNewWineMessage.setStyle("-fx-text-fill: #FF0000");
                    saveNewWineMessage.setText("This wine already exists.");
                    return null;
                } else {
                    return wine;
                }
            }
        } catch (NumberFormatException e) {
            saveNewWineMessage.setStyle("-fx-text-fill: #FF0000");
            saveNewWineMessage.setText("Wine Vintage and Score should be a number.");
            return null;
        }
    }
}
