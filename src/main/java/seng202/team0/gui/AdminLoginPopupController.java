package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    private PasswordField passwordTextInput;

    @FXML
    private Button viewButton;

    @FXML
    private Label adminLoginErrorLabel;

    public AdminLoginPopupController() {}

    @FXML
    public void init(WineEnvironment winery) {
        this.winery = winery;
        this.adminLoginInstance = winery.getAdminLoginInstance();
        // Bind textField with passwordField
        passwordInput.textProperty().bindBidirectional(passwordTextInput.textProperty());
        passwordInput.setVisible(false);
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
            winery.getClearRunnable().run();
            winery.launchAdminScreen();
        }
    }

    @FXML
    public void onViewButtonClicked() {
        if (passwordInput.isVisible()) {
            passwordInput.setVisible(false); // hide password
            passwordTextInput.setVisible(true);
            viewButton.setText("View");
        } else {
            passwordInput.setVisible(true); // show password
            passwordTextInput.setVisible(false);
            viewButton.setText("Hide");
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
