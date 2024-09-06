package seng202.team0.gui;

import javafx.fxml.FXML;
import seng202.team0.services.WineEnvironment;

public class AddWineryController {
    private final WineEnvironment winery;

    public AddWineryController(WineEnvironment winery) {
        this.winery = winery;
    }

    @FXML
    private void goBackToAdmin() {
        winery.getClearRunnable().run();
        winery.launchAdminScreen();
    }
}