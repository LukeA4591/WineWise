package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import seng202.team0.models.Wine;
import seng202.team0.services.AdminLoginService;
import seng202.team0.services.WineEnvironment;

public class AdminScreenController {
    private WineEnvironment wineEnvironment;

    public AdminScreenController(WineEnvironment wineEnvironment) {
        this.wineEnvironment = wineEnvironment;
    }

    @FXML
    public void onAddWine() {
        wineEnvironment.getClearRunnable().run();
        wineEnvironment.launchAddWineScreen();
    }

    @FXML
    void adminLogout() {
        wineEnvironment.getClearRunnable().run();
        wineEnvironment.launchNavBar();
    }
}