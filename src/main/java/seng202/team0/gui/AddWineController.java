package seng202.team0.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import seng202.team0.business.WineManager;
import seng202.team0.models.Wine;
import seng202.team0.services.WineEnvironment;


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

    private final WineEnvironment winery;
    private final WineManager wineManager;

    public AddWineController(WineEnvironment winery) {
        this.winery = winery;
        wineManager = new WineManager();
    }

    @FXML
    private void goBackToAdmin() {
        winery.getClearRunnable().run();
        winery.launchAdminScreen();
    }

    public void saveNewWine() {
        Wine wine = validateWine();
        if (wine != null) {
            wineManager.addWine(wine);
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
                int wineScoreInt;
                if (wineScore.getText().isEmpty()) {
                    wineScoreInt = 0; // Maybe make it null
                } else { // Add else if to check valid score
                    wineScoreInt = Integer.parseInt(wineScore.getText());
                }
                String wineTypeString = ((RadioButton) wineTypeToggle.getSelectedToggle()).getText();
                String wineRegionString = wineRegion.getText();
                String wineDescriptionString = wineDescription.getText();

                // Add save wine to database

                saveNewWineMessage.setStyle("-fx-text-fill: #00FF00");
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