package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import seng202.team0.services.WineEnvironment;


public class AddWineController {
    @FXML
    TextField wineName;

    @FXML
    RadioButton typeWhite;

    @FXML
    RadioButton typeRed;

    @FXML
    RadioButton typeRose;

    @FXML
    RadioButton typeSparkling;

    @FXML
    TextField wineWinery;

    @FXML
    TextField wineRegion;

    @FXML
    Button saveWine;

    private final WineEnvironment winery;

    public AddWineController(WineEnvironment winery) {
        this.winery = winery;
    }

    @FXML
    private void goBackToAdmin() {
        winery.getClearRunnable().run();
        winery.launchAdminScreen();
    }
}