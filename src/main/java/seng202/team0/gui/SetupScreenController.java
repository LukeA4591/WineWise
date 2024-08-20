package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import seng202.team0.services.WineEnvironment;

public class SetupScreenController {

    private Stage stage;

//    private final WineEnvironment winery;
    void init(Stage stage) {
        this.stage = stage;
    }

    public SetupScreenController() {

    }



    @FXML
    public void AdminButtonClicked(){
//        FXWrapper.clearpane();
    }

    @FXML
    public void Search_Clicked(){

    }
}
