package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
    private Label adminLoginErrorLabel;

    /**
     * Empty constructor
     */
    public AdminLoginPopupController() {}

    /**
     * Init method for admin login popup
     * It is called from in the AdminScreenController
     * Passes in the wine environment and gets the singleton instance of admin login service.
     * Sets up a listener that listens for when the TextField is attached to the scene
     * Then it can return a non-null scene.
     */

    @FXML
    public void init(WineEnvironment winery) {
        this.winery = winery;
        this.adminLoginInstance = winery.getAdminLoginInstance();
        passwordInput.sceneProperty().addListener((observable, oldscene, newscene) -> {
            if (newscene != null) {
                newscene.getStylesheets().add(getClass().getResource("/style/navbar.css").toExternalForm());
            }
        });
    }


    /**
     * FXML on action method for when the login button is clicked.
     * Returns error message if invalid credentials.
     * If valid launches the admin screen.
     */
    @FXML
    public void onLogin() {
        String inputtedUsername = usernameInput.getText();
        String inputtedPassword = passwordInput.getText();
        String errorMessage = adminLoginInstance.login(inputtedUsername, inputtedPassword);
        if (!errorMessage.isEmpty()) {
            adminLoginErrorLabel.setText(errorMessage);
        } else {
            ((Stage) usernameInput.getScene().getWindow()).close();
            winery.getClearRunnable().run();
            winery.launchAdminScreen();
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
