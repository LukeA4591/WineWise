package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import seng202.team0.io.SetWineryInterface;
import seng202.team0.repository.WineryDAO;

public class RemoveWineryPopupController {
    @FXML
    private Label removeWineryLabel;
    String wineryName;
    private WineryDAO wineryDAO;
    SetWineryInterface setWineryInterface;
    Runnable onConfirmRemove;

    void init(String wineryName, Runnable onConfirmRemove) {
        this.onConfirmRemove = onConfirmRemove;
        this.wineryDAO = new WineryDAO();
        this.wineryName = wineryName;
        this.setWineryInterface = setWineryInterface;
        removeWineryLabel.setText("Remove Winery: " + wineryName);
    }

    @FXML
    void confirmRemove() {
        wineryDAO.updateLocationByWineryName(wineryName, null, null);
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
