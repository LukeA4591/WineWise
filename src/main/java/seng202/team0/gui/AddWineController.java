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
            float wineVintageFloat = Float.parseFloat(wineVintage.getText());
            int wineScoreInt = Integer.parseInt(wineScore.getText());
            String wineTypeString = ((RadioButton) wineTypeToggle.getSelectedToggle()).getText();
            String wineRegionString = wineRegion.getText();
            String wineDescriptionString = wineDescription.getText();
            System.out.println(wineTypeString);
        } catch (NumberFormatException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}