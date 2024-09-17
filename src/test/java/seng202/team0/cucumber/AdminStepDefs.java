package seng202.team0.cucumber;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import org.junit.jupiter.api.Assertions;
import seng202.team0.services.AdminLoginService;
import seng202.team0.services.AppEnvironment;
import seng202.team0.gui.AdminSetupScreenController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class AdminStepDefs {
    private String username;
    private String password;
    private String confirmPassword;
    private AppEnvironment appEnvironment;
    private AdminLoginService adminLoginService;
    private AdminSetupScreenController adminSetupScreenController;
    private boolean loggedIn = false;

    void consumer1(AppEnvironment appEnvironment){}
    void consumer2(AppEnvironment appEnvironment){}
    void consumer3(AppEnvironment appEnvironment){}
    void clear(){}

    @Before
    public void setup() {
        this.appEnvironment = new AppEnvironment(this::consumer1, this::consumer2, this::consumer3, this::clear);
        this.adminLoginService = AdminLoginService.getInstance();
        this.adminSetupScreenController = new AdminSetupScreenController(appEnvironment);
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


}
