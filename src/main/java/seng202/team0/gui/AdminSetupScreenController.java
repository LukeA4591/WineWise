package seng202.team0.gui;

import javafx.fxml.FXML;
import seng202.team0.services.WineEnvironment;

import java.util.function.Consumer;

public class AdminSetupScreenController {

    private final WineEnvironment winery;

    //pass this class the wine environment
    //in the on action method for the create button:
    //wineEnvironment.clear.run();
    //wineEnvironment.launch Adminscreen

    public AdminSetupScreenController(WineEnvironment winery) {
        this.winery = winery;
    }

    @FXML
    public void createAccountLaunchAdminScreen() {
        winery.getClearRunnable().run();
        winery.launchAdminScreen();
    }


}
