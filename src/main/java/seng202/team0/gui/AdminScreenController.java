package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import seng202.team0.models.Wine;
import seng202.team0.services.AdminLoginService;
import seng202.team0.services.WineEnvironment;

public class AdminScreenController {
    @FXML
    Button addWine;

    private final WineEnvironment winery;

    public AdminScreenController(WineEnvironment winery) {
        this.winery = winery;
    }

    @FXML
    public void onAddWine() {
        winery.getClearRunnable().run();
        winery.launchAddWineScreen();
    }

    @FXML
    public void onLogout() {
        winery.getClearRunnable().run();
        winery.launchNavBar();
    }
}
