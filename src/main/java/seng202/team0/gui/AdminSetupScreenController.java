package seng202.team0.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import seng202.team0.services.AdminLoginService;
import seng202.team0.services.WineEnvironment;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;


public class AdminSetupScreenController {

    /**
     * The WineEnvironment which keeps track of the state of the program
     */
    private final WineEnvironment winery;

    /**
     * The singleton instance of AdminLoginService
     */
    private final AdminLoginService adminLoginInstance;

    /**
     * The Textfield that gets the users username
     */
    @FXML
    private TextField createUsernameInputField;

    /**
     * The Textfield that gets the users password
     */
    @FXML
    private TextField createPasswordInputField;

    /**
     * The Textfield that gets the users password confirmation
     */
    @FXML
    private TextField createConfirmPasswordInputField;

    /**
     * The Label that displays any errors when create button is clicked
     * Initially the label is hidden
     */
    @FXML
    private Label errorLabel;

    @FXML
    private AnchorPane mainWindow;

    /**
     * Constructor for admin setup screen controller
     * @param winery WineEnvironment keeps track of the state of the program
     */
    public AdminSetupScreenController(WineEnvironment winery) {
        this.winery = winery;
        this.adminLoginInstance = winery.getAdminLoginInstance();

    }

    // TODO need to make sure username inputs are validated.

    /**
     * A button clicked OnAction method that creates an account and launches admin screen
     */
    @FXML
    public void createAccountLaunchAdminScreen() {
        String inputtedUsername = createUsernameInputField.getText();
        String inputtedPassword = createPasswordInputField.getText();
        String confirmPassword = createConfirmPasswordInputField.getText();
        String errorMessage = adminLoginInstance.checkPasswordConfirmation(inputtedPassword, confirmPassword);
        if (!errorMessage.isEmpty()) {
            errorLabel.setText(errorMessage);
        } else {
            adminLoginInstance.createCredentialsFile();
            adminLoginInstance.createNewUser(inputtedUsername, inputtedPassword);
            winery.getClearRunnable().run();
            winery.launchAdminScreen();
        }
    }

    /**
     * Method to trigger login when the enter key is pressed
     * @param event keyboard event
     */
    @FXML
    private void enterKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            createAccountLaunchAdminScreen();
        }
    }




}
