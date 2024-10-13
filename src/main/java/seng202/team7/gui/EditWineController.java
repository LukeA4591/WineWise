package seng202.team7.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import seng202.team7.business.WineManager;
import seng202.team7.models.Wine;
import seng202.team7.services.WineService;

import java.util.Objects;

/**
 * Controller class for the edit_wine.fxml file
 */
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
    private WineService wineService;

    /**
     * Constructor for the EditWineController class
     * @param wine wine which has been selected
     */
    public EditWineController(Wine wine) {
        this.origionalWine = wine;
        this.wineManager = new WineManager();
        this.wineService = new WineService();
    }

    /**
     * Initializes the UI objects on the edit wine page
     */
    @FXML
    public void initialize() {
        wineWineryName.setText(origionalWine.getWineryString());
        wineName.setText(origionalWine.getWineName());
        wineVintage.setText(Integer.toString(origionalWine.getVintage()));
        if (origionalWine.getScore() != null) {
            wineScore.setText(Integer.toString(origionalWine.getScore()));
        }
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

    /**
     * Saves the new wine to the db through the wineManager.updateWine function, and changes UI depending on the outcome
     */
    @FXML
    public void saveWine() {
        String wineNameString = wineName.getText();
        String wineryNameString = wineWineryName.getText();
        String wineVintageString = wineVintage.getText();
        String wineRegionString = wineRegion.getText();
        String wineScoreString = wineScore.getText();
        String wineDescriptionString = wineDescription.getText();
        String wineTypeString = ((RadioButton) wineTypeToggle.getSelectedToggle()).getText();
        String errorLabel = wineService.validateWine(wineNameString, wineryNameString, wineVintageString, wineScoreString, wineRegionString, wineDescriptionString);
        if (Objects.equals(errorLabel, "")) {
            Wine wine = new Wine(wineTypeString, wineNameString, wineryNameString, Integer.parseInt(wineVintageString), Integer.parseInt(wineScoreString), wineRegionString, wineDescriptionString);
            boolean successfulUpdate = wineManager.updateWine(wine, this.origionalWine);
            if (!successfulUpdate) {
                saveNewWineMessage.setText("This wine already exists.");
            } else {
                Stage stage = (Stage) wineWineryName.getScene().getWindow();
                stage.close();
            }
        } else {
            saveNewWineMessage.setText(errorLabel);
        }
    }
}
