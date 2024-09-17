package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import seng202.team0.services.AdminLoginService;
import seng202.team0.services.WineEnvironment;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

/**
 * Controller class for the setup_admin.fxml page.
 */
public class AdminSetupScreenController {

    @FXML
    private TextField createUsernameInputField;
    @FXML
    private TextField createPasswordInputField;
    @FXML
    private TextField createConfirmPasswordInputField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private PasswordField createPasswordField;
    @FXML
    private Button viewButton;
    @FXML
    private Label errorLabel;

    private final WineEnvironment winery;
    private final AdminLoginService adminLoginInstance;


    /**
     * Constructor for admin setup screen controller. Sets the WineEnvironment and adminLoginInstance.
     * @param winery WineEnvironment keeps track of the state of the program
     */
    public AdminSetupScreenController(WineEnvironment winery) {
        this.winery = winery;
        this.adminLoginInstance = winery.getAdminLoginInstance();
    }

    /**
     * Initializes the controller and method for admin setup screen. Allows passwords to be switched between invisible
     * and visible mode.
     */
    @FXML
    public void initialize() {
        createPasswordInputField.textProperty().bindBidirectional(createPasswordField.textProperty());
        createConfirmPasswordInputField.textProperty().bindBidirectional(confirmPasswordField.textProperty());
        createPasswordInputField.setVisible(false);
        createConfirmPasswordInputField.setVisible(false);

        // Temp remove as styling is done on fxml
//        createUsernameInputField.sceneProperty().addListener((observable, oldscene, newscene) -> {
//            if (newscene != null) {
//                newscene.getStylesheets().add(getClass().getResource("/style/navbar.css").toExternalForm());
//            }
//        });
    }

    /**
     * When the create account button is pressed, if the password is validated, the method creates a new account by
     * calling adminLoginInstance to set up the hashed password and storage of user details and then launches the admin
     * screen.
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
     * When the view/hide password button is pressed, the password is then set to visible/invisible.
     */
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
