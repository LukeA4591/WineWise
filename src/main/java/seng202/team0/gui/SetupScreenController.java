package seng202.team0.gui;

import javafx.fxml.FXML;
import seng202.team0.models.Winery;

public class SetupScreenController {

    private final Winery winery;
//    private MainController mainController;

    public SetupScreenController(final Winery tempEnvironment) {
        this.winery = tempEnvironment;
//        this.mainController = controller;
    }

    @FXML
    public void AdminButtonClicked(){
//        FXWrapper.clearpane();
    }
}
