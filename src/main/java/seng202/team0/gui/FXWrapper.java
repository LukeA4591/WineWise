package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import seng202.team0.services.WineEnvironment;

import java.io.IOException;

public class FXWrapper {
    /**
     * Pane for the application to be run on.
     */
    @FXML
    private Pane pane;
    /**
     * Primary stage for the application.
     */
    private Stage stage;

    /**
     * Initializes the FXWrapper with the primary stage of the application.
     *
     * @param stage the primary stage of the application.
     */
    public void init(final Stage stage) {
        this.stage = stage;
        new WineEnvironment(this::launchSetupScreen, this::launchNavBar, this::launchAdminSetupScreen, this::launchAdminScreen, this::clearPane);
    }

    /**
     * Launches the setup screen.
     *
     * @param winery the wine environment which manages the app's state.
     */
    public void launchSetupScreen(final WineEnvironment winery) {
        try {
            FXMLLoader setupLoader = new FXMLLoader(getClass().getResource("/fxml/setup_screen.fxml"));
            //setupLoader.setControllerFactory(param -> new SetupScreenController(winery));
            Parent setupParent  = setupLoader.load();
            pane.getChildren().add(setupParent);
            stage.setTitle("Setup Screen");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Launches the NavBar Screen
     * @param wineEnvironment the environment which manages the state of the application
     */
    public void launchNavBar(final WineEnvironment wineEnvironment) {
        try {
            FXMLLoader navBarLoader = new FXMLLoader(getClass().getResource("/fxml/nav_bar.fxml"));
            //navBarLoader.setControllerFactory(param -> new NavBarController(wineEnvironment, stage));
            Parent setupParent  = navBarLoader.load();
            NavBarController navBarController = navBarLoader.getController();
            navBarController.setWineEnvironment(wineEnvironment);
            navBarController.setStage(stage);
            pane.getChildren().add(setupParent);
            stage.setTitle("WineWise Nav Bar");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void launchAdminSetupScreen(final WineEnvironment winery) {
        try {
            FXMLLoader setupLoader = new FXMLLoader(getClass().getResource("/fxml/setup_admin.fxml"));
            setupLoader.setControllerFactory(param -> new AdminSetupScreenController(winery));
            Parent setupParent  = setupLoader.load();
            pane.getChildren().add(setupParent);
            stage.setTitle("Admin Setup Screen");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void launchAdminScreen(final WineEnvironment winery) {
        try {
            FXMLLoader setupLoader = new FXMLLoader(getClass().getResource("/fxml/admin_screen.fxml"));
            //setupLoader.setControllerFactory(param -> new SetupScreenController(winery));
            Parent setupParent  = setupLoader.load();
            pane.getChildren().add(setupParent);
            stage.setTitle("Admin Screen");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * Clears Page
     */
    public void clearPane(){
        pane.getChildren().removeAll(pane.getChildren());
    }
}
