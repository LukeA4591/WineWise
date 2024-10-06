package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import seng202.team0.services.AppEnvironment;

public class AdminHelpPopupController {
    @FXML
    private Button backButton;

    @FXML
    private void onBack() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
}
