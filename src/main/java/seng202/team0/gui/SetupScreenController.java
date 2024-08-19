package seng202.team0.gui;

import javafx.stage.Stage;
import seng202.team0.services.WineEnvironment;

public class SetupScreenController {

    private Stage stage;

    //private final WineEnvironment winery;
    void init(Stage stage) {
        this.stage = stage;
        System.out.println("Init ran");
    }

    public SetupScreenController() {

    }
    /**
    public SetupScreenController(final WineEnvironment tempEnvironment) {
        this.winery = tempEnvironment;
    }
     */
}
