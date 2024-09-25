package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import seng202.team0.services.AdminLoginService;
import seng202.team0.services.AppEnvironment;

public class ChangePasswordPopupController {


    public Label errorLabel;
    public Button viewButton;
    public TextField currentPasswordInputField;

    public PasswordField currentPasswordField;
    public TextField newPasswordInputField;
    public PasswordField newPasswordField;
    public TextField confirmNewPasswordInputField;
    public PasswordField confirmNewPasswordField;

    private AppEnvironment appEnvironment;

    private AdminLoginService adminLoginService;

    @FXML
    public void init(AppEnvironment appEnvironment) {
        this.appEnvironment = appEnvironment;
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

    public void changePassword() {
        String currentPassword = currentPasswordInputField.getText();
        String newPassword = newPasswordInputField.getText();
        String confirmPassword = confirmNewPasswordInputField.getText();
        String errorMessage = adminLoginService.changePassword(currentPassword, newPassword, confirmPassword);
        System.out.println(errorMessage);
        if (!errorMessage.isEmpty()) {
            errorLabel.setText(errorMessage);
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

    @FXML
    public void changeAdminPassword() {
        changePassword();
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
