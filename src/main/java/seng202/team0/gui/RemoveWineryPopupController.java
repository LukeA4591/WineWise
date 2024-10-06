package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import seng202.team0.business.WineryManager;
import seng202.team0.io.SetWineryInterface;
import seng202.team0.repository.WineryDAO;

public class RemoveWineryPopupController {
    @FXML
    private Label removeWineryLabel;
    String wineryName;
    private WineryManager wineryManager;
    Runnable onConfirmRemove;

    void init(String wineryName, Runnable onConfirmRemove) {
        this.onConfirmRemove = onConfirmRemove;
        this.wineryName = wineryName;
        wineryManager = new WineryManager();
        removeWineryLabel.setText("Remove Winery: " + wineryName);
    }

    @FXML
    void confirmRemove() {
        wineryManager.updateLocationByWineryName(wineryName, null, null);
        onConfirmRemove.run();
        Stage stage = (Stage) removeWineryLabel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void cancelRemove() {
        Stage stage = (Stage) removeWineryLabel.getScene().getWindow();
        stage.close();
    }
}
