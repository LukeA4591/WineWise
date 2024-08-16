package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import seng202.team0.services.WineEnvironment;

import java.io.IOException;

public class NavBarController {

    private final WineEnvironment wineEnvironment;
    @FXML
    private BorderPane mainWindow;
    private Stage stage;


    /**
     * Initializer for the NavBarController, takes in a WineEnvironment
     * @param tempEnvironment WineEnvironment
     */
    public NavBarController(final WineEnvironment tempEnvironment, Stage stage) {
        this.wineEnvironment = tempEnvironment;
        this.stage = stage;
        loadHomePage(stage);
    }

    private void loadHomePage(Stage stage) {
        try {
            FXMLLoader homePageLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Parent mainParent = homePageLoader.load();
            MainController homePageController = homePageLoader.getController();
            homePageController.init(stage);
            mainWindow.setCenter(mainParent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}