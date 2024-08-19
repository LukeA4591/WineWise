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

    /**
     * NavBarController initializer, needs to be empty for FXML
     */
    public NavBarController() {
    }

    /**
     * FXML initialize method, loads
     */
    @FXML
    private void initialize() {
        loadHomePage(stage);
    }

    /**
     * Loads the home page into the border pane
     * @param stage stage
     */
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

    /**
     * The Setter method for the wineEnvironment
     * @param wineEnvironment wine environment
     */
    public void setWineEnvironment(WineEnvironment wineEnvironment) {
        this.wineEnvironment = wineEnvironment;
    }

    /**
     * The Setter method for the stage
     * @param stage stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}