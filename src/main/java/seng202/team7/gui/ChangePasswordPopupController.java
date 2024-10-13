package seng202.team7.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import seng202.team7.services.AdminLoginService;
import seng202.team7.services.AppEnvironment;

/**
 * Controller for the admin_change_password_popup.fxml
 */
public class ChangePasswordPopupController {
    @FXML
    private Label errorLabel;
    @FXML
    private Button viewButton;
    @FXML
    private TextField currentPasswordInputField;
    @FXML
    private PasswordField currentPasswordField;
    @FXML
    private TextField newPasswordInputField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private TextField confirmNewPasswordInputField;
    @FXML
    private PasswordField confirmNewPasswordField;
    private AdminLoginService adminLoginService;

    /**
     * Default constructor for ChangePasswordPopupController
     */
    public ChangePasswordPopupController() {

    }

    /**
     * Initializes the Change Password Popup window and all UI objects
     * @param appEnvironment instance of AppEnvironment
     */
    @FXML
    public void init(AppEnvironment appEnvironment) {
        adminLoginService = appEnvironment.getAdminLoginInstance();
        currentPasswordInputField.textProperty().bindBidirectional(currentPasswordField.textProperty());
        newPasswordInputField.textProperty().bindBidirectional(newPasswordField.textProperty());
        confirmNewPasswordInputField.textProperty().bindBidirectional(confirmNewPasswordField.textProperty());
        newPasswordField.setVisible(true);
        confirmNewPasswordField.setVisible(true);
        newPasswordInputField.setVisible(false);
        confirmNewPasswordInputField.setVisible(false);
        viewButton.setText("View");
    }

    /**
     * Takes the input text fields and calls the adminLoginService to change the values to the ones given
     */
    public void changePassword() {
        String currentPassword = currentPasswordInputField.getText();
        String newPassword = newPasswordInputField.getText();
        String confirmPassword = confirmNewPasswordInputField.getText();
        String errorMessage = adminLoginService.changePassword(currentPassword, newPassword, confirmPassword);
        if (!errorMessage.isEmpty()) {
            errorLabel.setText(errorMessage);

        } else {
            errorLabel.setText("Password successfully changed");
            errorLabel.setTextFill(Color.GREEN);
            currentPasswordInputField.clear();
            newPasswordInputField.clear();
            confirmNewPasswordInputField.clear();
        }
    }

    /**
     * When the button is pressed, if the passwords are set to invisible (dots), the passwords will become visible
     * (characters) and vice versa.
     */
    @FXML
    public void onViewButtonClicked() {
        if (newPasswordInputField.isVisible()) {
            newPasswordInputField.setVisible(false);
            confirmNewPasswordInputField.setVisible(false);
            newPasswordField.setVisible(true);
            confirmNewPasswordField.setVisible(true);
            viewButton.setText("View");
        } else {
            newPasswordInputField.setVisible(true);
            confirmNewPasswordInputField.setVisible(true);
            newPasswordField.setVisible(false);
            confirmNewPasswordField.setVisible(false);
            viewButton.setText("Hide");
        }
    }

    /**
     * Ties change password button to corresponding function
     */
    @FXML
    public void changeAdminPassword() {
        changePassword();
    }

    /**
     * Tied to UI button "Cancel" to close out the change password window
     */
    @FXML
    public void closePressed() {
        Stage stage = (Stage) errorLabel.getScene().getWindow();
        stage.close();
    }

    /**
     * Method to trigger login when the enter key is pressed
     * @param event keyboard event
     */
    @FXML
    private void enterKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            changePassword();
        }
    }



}
