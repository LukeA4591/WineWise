package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import seng202.team0.services.WineEnvironment;

import java.io.IOException;

public class NavBarController {

    private WineEnvironment wineEnvironment;
    @FXML
    private BorderPane mainWindow;
    private Stage stage;

    public NavBarController() {
    }

    @FXML
    private void initialize() {
        if (stage != null) {
            loadHomePage(stage);
        }
    }

    private void loadHomePage(Stage stage) {
        try {
            FXMLLoader homePageLoader = new FXMLLoader(getClass().getResource("/fxml/setup_screen.fxml"));
            Parent mainParent = homePageLoader.load();
            SetupScreenController homePageController = homePageLoader.getController();
            homePageController.init(stage);
            mainWindow.setCenter(mainParent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setWineEnvironment(WineEnvironment wineEnvironment) {
        this.wineEnvironment = wineEnvironment;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}