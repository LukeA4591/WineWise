package seng202.team7.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import seng202.team7.services.AdminLoginService;
import seng202.team7.services.AppEnvironment;

import javafx.scene.control.TextField;

/**
 * Controller class for the admin_login_popup.fxml page.
 */
public class AdminLoginPopupController {

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

    private AppEnvironment appEnvironment;
    private AdminLoginService adminLoginInstance;

    /**
     * Default constructor for the AdminLoginPopupController
     */
    public AdminLoginPopupController() {

    }

    /**
     * Init method for admin login popup. It is called from in the AdminScreenController. Passes in the wine
     * environment and gets the singleton instance of admin login service. Sets up a listener that listens for when the
     * TextField is attached to the scene.
     * @param appEnvironment The AppEnvironment to let us launch other pages.
     */
    @FXML
    public void init(AppEnvironment appEnvironment) {
        this.appEnvironment = appEnvironment;
        this.adminLoginInstance = appEnvironment.getAdminLoginInstance();
        // Bind textField with passwordField
        passwordInput.textProperty().bindBidirectional(passwordTextInput.textProperty());
        passwordInput.setVisible(false);
        passwordInput.sceneProperty().addListener((observable, oldscene, newscene) -> {
            if (newscene != null) {
                newscene.getStylesheets().add(getClass().getResource("/style/navbar.css").toExternalForm());
            }
        });
    }

    /**
     * FXML on action method for when the login button is clicked. Prints an error message if there are invalid
     * credentials. If valid, the admin screen is launched.
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
            appEnvironment.getClearRunnable().run();
            appEnvironment.launchAdminScreen();
        }
    }

    /**
     * When the button is pressed, if the passwords are set to invisible (dots), the passwords will become visible
     * (characters) and vice versa.
     */
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
     * Closes the popup on click and uses usernameInput to get the window it needs to close.
     */
    @FXML
    public void onCancel() {
        ((Stage) usernameInput.getScene().getWindow()).close();
    }

}
