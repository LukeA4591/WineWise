package seng202.team7.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AdminHelpPopupController {
    @FXML
    private Button backButton;

    /**
     * Closes the current stage/pop-up
     */
    @FXML
    private void onBack() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
}
