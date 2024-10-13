package seng202.team7.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import seng202.team7.business.WineryManager;

/**
 * Controller class for the remove_winery_location.fxml file
 */
public class RemoveWineryPopupController {
    @FXML
    private Label removeWineryLabel;
    String wineryName;
    private WineryManager wineryManager;
    Runnable onConfirmRemove;

    /**
     * Default constructor for the RemoveWineryPopupController class
     */
    public RemoveWineryPopupController() {

    }

    /**
     * Initializes the remove winery popup
     * @param wineryName name of the winery
     * @param onConfirmRemove runnable onActionMethod to confirm the removal of the winery
     */
    void init(String wineryName, Runnable onConfirmRemove) {
        this.onConfirmRemove = onConfirmRemove;
        this.wineryName = wineryName;
        wineryManager = new WineryManager();
        removeWineryLabel.setText("Remove Winery: " + wineryName + "?");
    }

    /**
     * Actually removes the winery's lat & lon
     */
    @FXML
    void confirmRemove() {
        wineryManager.updateLocationByWineryName(wineryName, null, null);
        onConfirmRemove.run();
        Stage stage = (Stage) removeWineryLabel.getScene().getWindow();
        stage.close();
    }

    /**
     * Cancel removing the winery's and closes the pop-up
     */
    @FXML
    void cancelRemove() {
        Stage stage = (Stage) removeWineryLabel.getScene().getWindow();
        stage.close();
    }
}
