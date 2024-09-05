package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import seng202.team0.services.WineEnvironment;

public class AdminScreenController {
    @FXML
    private Button logoutButton;
    private WineEnvironment wineEnvironment;

    public AdminScreenController(WineEnvironment wineEnvironment) {
        this.wineEnvironment = wineEnvironment;
    }

    @FXML
    void adminLogout() {
        wineEnvironment.getClearRunnable().run();
        wineEnvironment.launchNavBar();
    }
}
