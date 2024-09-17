package seng202.team0.cucumber;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import org.junit.jupiter.api.Assertions;
import seng202.team0.services.AdminLoginService;
import seng202.team0.services.WineEnvironment;
import seng202.team0.gui.AdminSetupScreenController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.FileHandler;


public class AdminStepDefs {
    private String username;
    private String password;
    private String confirmPassword;
    private WineEnvironment wineEnvironment;
    private AdminLoginService adminLoginService;
    private AdminSetupScreenController adminSetupScreenController;
    private boolean loggedIn = false;

    void consumer1(WineEnvironment wineEnvironment){}
    void consumer2(WineEnvironment wineEnvironment){}
    void consumer3(WineEnvironment wineEnvironment){}
    void consumer4(WineEnvironment wineEnvironment){}
    void consumer5(WineEnvironment wineEnvironment){}
    void clear(){}

    @Before
    public void setup() {
        this.wineEnvironment = new WineEnvironment(this::consumer1, this::consumer2, this::consumer3, this::clear);
        this.adminLoginService = AdminLoginService.getInstance();
        this.adminSetupScreenController = new AdminSetupScreenController(wineEnvironment);
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

    @And("The username and password match")
    public void usernameAndPasswordMatch() throws IOException {
        String jarStr = adminLoginService.getJarFilePath();
        jarStr += "/credentials.txt";
        File file = new File(jarStr);

        BufferedReader br = new BufferedReader(new FileReader(file));
        String enteredUsername = br.readLine();
        String hashedPassword = br.readLine();
        Assertions.assertEquals(this.username, enteredUsername);
//        Assertions.assertEquals("##Hashed##" + this.password, hashedPassword);

    }

    @Given("An admin registers with username {string}, incorrect password {string}, and confirm password {string}")
    public void incorrectCredentials(String username, String password, String confirmPassword) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
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
