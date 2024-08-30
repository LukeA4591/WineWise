package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import seng202.team0.services.AdminLoginService;
import seng202.team0.services.WineEnvironment;

import javafx.scene.control.TextField;

public class AdminLoginPopupController {

    /**
     * The WineEnvironment which keeps track of the state of the program
     */
    private WineEnvironment winery;

    /**
     * The singleton instance of AdminLoginService
     */
    private AdminLoginService adminLoginInstance;

    @FXML
    private TextField usernameInput;

    @FXML
    private TextField passwordInput;

    @FXML
    private Label adminLoginErrorLabel;

    public AdminLoginPopupController() {}

    @FXML
    public void init(WineEnvironment winery) {
        this.winery = winery;
        this.adminLoginInstance = winery.getAdminLoginInstance();
    }

    @FXML
    public void onLogin() {
        String inputtedUsername = usernameInput.getText();
        String inputtedPassword = passwordInput.getText();
        String errorMessage = adminLoginInstance.login(inputtedUsername, inputtedPassword);
        if (!errorMessage.isEmpty()) {
            adminLoginErrorLabel.setText(errorMessage);
        } else {
            // go to admin screen
            // close controller
            ((Stage) usernameInput.getScene().getWindow()).close();
        }
    }

    /**
     * Closes the popup on click
     * Uses usernameInput to get the window
     */
    @FXML
    public void onCancel() {
        ((Stage) usernameInput.getScene().getWindow()).close();
    }

}
