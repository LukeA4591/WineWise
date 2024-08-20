package seng202.team0.gui;

import javafx.fxml.FXML;
import seng202.team0.services.AdminLoginService;
import seng202.team0.services.WineEnvironment;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;


public class AdminSetupScreenController {

    private final WineEnvironment winery;

    private final AdminLoginService adminLoginInstance;

    @FXML
    private TextField createUsernameInputField;

    @FXML
    private TextField createPasswordInputField;

    @FXML
    private TextField createConfirmPasswordInputField;

    @FXML
    private Label errorLabel;

    public AdminSetupScreenController(WineEnvironment winery) {
        this.winery = winery;
        this.adminLoginInstance = winery.getAdminLoginInstance();
    }

    // TODO need to make sure username inputs are validated.
    @FXML
    public void createAccountLaunchAdminScreen() {
        String inputtedUsername = createUsernameInputField.getText();
        String inputtedPassword = createPasswordInputField.getText();
        String confirmPassword = createConfirmPasswordInputField.getText();
        String errorMessage = adminLoginInstance.checkPasswordConfirmation(inputtedPassword, confirmPassword);
        if (errorMessage != "") {
            errorLabel.setText(errorMessage);
        } else {
            adminLoginInstance.createNewUser(inputtedUsername, inputtedPassword);
            winery.getClearRunnable().run();
            winery.launchAdminScreen();
        }
    }


}
