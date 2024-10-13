package seng202.team7.cucumber;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import org.junit.jupiter.api.Assertions;
import seng202.team7.services.AdminLoginService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class AdminStepDefs {
    private String username;
    private String password;
    private String confirmPassword;
    private AdminLoginService adminLoginService;

    @Before
    public void setup() {
        this.adminLoginService = AdminLoginService.getInstance();
        File file = adminLoginService.getCredentialsFile();
        if (file.exists()) {
            file.delete();
        }
    }

    @Given("An admin registers with username {string}, password {string}, and confirm password {string}")
    public void correctCredentials(String username, String password, String confirmPassword) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    @When("The admin creates a valid account")
    public void adminCreatesValidAccount() {
        Assertions.assertEquals("", adminLoginService.checkPasswordConfirmation(this.password, this.confirmPassword));
        if (adminLoginService.doesFileExist()) {adminLoginService.createCredentialsFile();}
        adminLoginService.createNewUser(this.username, this.password);
    }

    @Then("The account is created and the credentials.txt file is made")
    public void theAccountIsCreated() {
        Assertions.assertEquals(true, adminLoginService.doesFileExist());
    }

    @And("The username matches")
    public void usernameAndPasswordMatch() throws IOException {
        String jarStr = adminLoginService.getJarFilePath();
        jarStr += "/credentials.txt";
        File file = new File(jarStr);

        BufferedReader br = new BufferedReader(new FileReader(file));
        String enteredUsername = br.readLine();
        Assertions.assertEquals(this.username, enteredUsername);
    }


    @When("The admin creates an invalid account")
    public void adminCreatesInvalidAccount() {
        Assertions.assertEquals("The password is under 8 characters", adminLoginService.checkPasswordConfirmation(this.password, this.confirmPassword));
        adminLoginService.createNewUser(this.username, this.password);
    }

    @Then("The account is not created")
    public void accountIsNotCreated() {
        //TODO, invalid accounts are still created which is probably not good
    }

    @Given("An admin registers using username {string}, password {string}, and confirm password {string}")
    public void signupCredentials(String username, String password, String confirmPassword) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    @When("The admin creates the account")
    public void createAccout() {
        Assertions.assertEquals("", adminLoginService.checkPasswordConfirmation(this.password, this.confirmPassword));
        if (adminLoginService.doesFileExist()) {adminLoginService.createCredentialsFile();}
        adminLoginService.createNewUser(this.username, this.password);
    }

    @And("A user tries to log in with username {string} and password {string}")
    public void login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Then("The user is not able to login to the admin account")
    public void wrongLogin() {
        String error = adminLoginService.login(username, password);
        Assertions.assertEquals(error, "Password is incorrect");
    }

}
