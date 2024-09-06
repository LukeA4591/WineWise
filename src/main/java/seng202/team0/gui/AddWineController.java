package seng202.team0.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

    public AddWineController(WineEnvironment winery) {
        this.winery = winery;
    }

    @FXML
    private void goBackToAdmin() {
        winery.getClearRunnable().run();
        winery.launchAdminScreen();
    }

    public void saveNewWine() {
        try {
            String wineWineryNameString = wineWineryName.getText();
            String wineNameString = wineName.getText();
            if (wineWineryNameString.isEmpty()) {
                saveNewWineMessage.setStyle("-fx-text-fill: #FF0000");
                saveNewWineMessage.setText("Winery Name field is empty.");
            } else if (wineNameString.isEmpty()){
                saveNewWineMessage.setStyle("-fx-text-fill: #FF0000");
                saveNewWineMessage.setText("Wine Name field is empty.");
            } else if (wineVintage.getText().isEmpty()) {
                saveNewWineMessage.setStyle("-fx-text-fill: #FF0000");
                saveNewWineMessage.setText("Wine Vintage field is empty.");
            } else {
                float wineVintageFloat = Float.parseFloat(wineVintage.getText());
                if (wineScore.getText().isEmpty()) {
                    int wineScoreInt = 0; // Maybe make it null
                } else {
                    int wineScoreInt = Integer.parseInt(wineScore.getText());
                }
                String wineTypeString = ((RadioButton) wineTypeToggle.getSelectedToggle()).getText();
                String wineRegionString = wineRegion.getText();
                String wineDescriptionString = wineDescription.getText();

                // Add save wine to database

                saveNewWineMessage.setStyle("-fx-text-fill: #00FF00");
                saveNewWineMessage.setText("New wine saved.");
            }
        } catch (NumberFormatException e) {
            saveNewWineMessage.setStyle("-fx-text-fill: #FF0000");
            saveNewWineMessage.setText("Wine Vintage and Score should be a number.");
        }
    }
}