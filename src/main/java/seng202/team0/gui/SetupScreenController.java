package seng202.team0.gui;

import javafx.fxml.FXML;
import seng202.team0.services.WineEnvironment;

public class SetupScreenController {

    private final WineEnvironment winery;

    public SetupScreenController(final WineEnvironment tempEnvironment) {
        this.winery = tempEnvironment;
    }


    @FXML
    public void AdminButtonClicked(){
//        FXWrapper.clearpane();
    }

    @FXML
    public void Search_Clicked(){

    }
}
