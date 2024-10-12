package seng202.team7.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Controller class for the admin_help_popup.fxml file
 */
public class AdminHelpPopupController {
    @FXML
    private Button backButton;

    /**
     * Default constructor for the AdminHelpPopupController
     */
    public AdminHelpPopupController() {

    }

    /**
     * Closes the current stage/pop-up
     */
    @FXML
    private void onBack() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
}
