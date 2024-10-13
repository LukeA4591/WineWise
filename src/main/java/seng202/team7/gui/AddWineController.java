package seng202.team7.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import seng202.team7.business.WineManager;
import seng202.team7.business.WineryManager;
import seng202.team7.models.Wine;
import seng202.team7.models.Winery;
import seng202.team7.services.WineService;


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
    private WineryManager wineryManager;
    private WineService wineService;

    /** Global wine var to be assigned **/
    Wine wine;

    /**
     * Default constructor for the AddWineController class.
     */
    public AddWineController() {
    }

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
        wineryManager = new WineryManager();
        wineService = new WineService();
    }

    /**
     * Sends the new wine to the wineManager when the save wine button is pressed if all the wine values filled in the
     * text fields were all valid.
     */
    @FXML
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

            if (!wineScoreString.isEmpty()) {
                int wineScore = Integer.parseInt(wineScoreString);
                wine = new Wine(wineTypeString, wineNameString, wineryNameString, Integer.parseInt(wineVintageString), wineScore, wineRegionString, wineDescriptionString);
            } else {
                wine = new Wine(wineTypeString, wineNameString, wineryNameString, Integer.parseInt(wineVintageString), null, wineRegionString, wineDescriptionString);

            }

            if (!wineManager.checkIfWineExists(wine)) {
                confirmChangeUI();
                wineManager.add(wine);
                addWinery(wineryNameString);
                Platform.runLater(this::closePage);

            } else {
                saveNewWineMessage.setText("Wine Already Exists in the Database");
            }
        } else {
            saveNewWineMessage.setText(errorLabel);
        }

    }

    /**
     * Adds a winery with only a name to the db through the manager
     * @param wineryName
     */
    private void addWinery(String wineryName) {
        Winery winery = wineryManager.getWineryByName(wineryName);
        if (winery == null) {
            wineryManager.add(new Winery(wineryName, null, null));
        }
    }

    /**
     * Shows green text saying the wine has been saved to confirm to the user the wine has been saved
     */
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

    /**
     * Closes the pop-up after a delay to give users time to see the confirmation message
     */
    private void closePage() {
        Platform.runLater(() -> {
            try {
                Thread.sleep(1250);
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