package seng202.team0.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
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

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private PasswordField createPasswordField;

    @FXML
    private Button viewButton;

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

    /**
     * Initializes the controller and sets up bindings.
     */
    @FXML
    public void initialize() {
        // Bind text properties after all FXML fields have been loaded
        createPasswordInputField.textProperty().bindBidirectional(createPasswordField.textProperty());
        createConfirmPasswordInputField.textProperty().bindBidirectional(confirmPasswordField.textProperty());
        createPasswordInputField.setVisible(false);
        createConfirmPasswordInputField.setVisible(false);
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
            adminLoginInstance.createNewUser(inputtedUsername, inputtedPassword);
            winery.getClearRunnable().run();
            winery.launchAdminScreen();
        }
    }

    @FXML
    public void onViewButtonClicked() {
        if (createPasswordInputField.isVisible()) {
            createPasswordInputField.setVisible(false); // hide password
            createConfirmPasswordInputField.setVisible(false);
            createPasswordField.setVisible(true);
            confirmPasswordField.setVisible(true);
            viewButton.setText("View");
        } else {
            createPasswordInputField.setVisible(true); // show password
            createConfirmPasswordInputField.setVisible(true);
            createPasswordField.setVisible(false);
            confirmPasswordField.setVisible(false);
            viewButton.setText("Hide");
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
