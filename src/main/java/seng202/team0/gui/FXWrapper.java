package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import seng202.team0.services.WineEnvironment;

import java.io.IOException;

/**
 * The class allows pages to be launched when the current page is switched to another page.
 */
public class FXWrapper {

    @FXML
    private Pane pane;

    private Stage stage;

    /**
     * Initializes the FXWrapper with the primary stage of the application.
     * @param stage The primary stage of the application.
     */
    public void init(final Stage stage) {
        this.stage = stage;
        new WineEnvironment(this::launchNavBar, this::launchAdminSetupScreen, this::launchAdminScreen, this::clearPane);
    }

    /**
     * Launches the navigation bar screen which will contain the navigation bar for the user side of the application.
     * @param wineEnvironment The environment which manages the state of the application.
     */
    public void launchNavBar(final WineEnvironment wineEnvironment) {
        try {
            FXMLLoader navBarLoader = new FXMLLoader(getClass().getResource("/fxml/nav_bar.fxml"));
            Parent setupParent  = navBarLoader.load();
            NavBarController navBarController = navBarLoader.getController();
            navBarController.setWineEnvironment(wineEnvironment);
            navBarController.setStage(stage);
            pane.getChildren().add(setupParent);
            stage.setTitle("WineWise Nav Bar");
            stage.setWidth(900);
            stage.setHeight(624);
            stage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Launches the admin setup screen which is used to set up the admin details when a user first logs in.
     * @param wineEnvironment The environment which manages the state of the application.
     */
    public void launchAdminSetupScreen(final WineEnvironment wineEnvironment) {
        try {
            FXMLLoader setupLoader = new FXMLLoader(getClass().getResource("/fxml/setup_admin.fxml"));
            setupLoader.setControllerFactory(param -> new AdminSetupScreenController(wineEnvironment));
            Parent setupParent  = setupLoader.load();
            pane.getChildren().add(setupParent);
            setupParent.getStylesheets().add(getClass().getResource("/style/navbar.css").toExternalForm());
            stage.setTitle("Admin Setup Screen");
            stage.setWidth(600);
            stage.setHeight(400);
            stage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Launches the admin screen which is the home page for the admin. It is used to add and modify data such as
     * reviews and wines.
     * @param wineEnvironment The environment which manages the state of the application.
     */
    public void launchAdminScreen(final WineEnvironment wineEnvironment) {
        try {
            FXMLLoader setupLoader = new FXMLLoader(getClass().getResource("/fxml/admin_screen.fxml"));
            setupLoader.setControllerFactory(param -> new AdminScreenController(wineEnvironment));
            stage.setWidth(900);
            stage.setHeight(624);
            Parent setupParent  = setupLoader.load();
            setupParent.getStylesheets().add(getClass().getResource("/style/navbar.css").toExternalForm());
            pane.getChildren().add(setupParent);
            stage.setTitle("Admin Screen");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clears the current page.
     */
    public void clearPane(){
        pane.getChildren().removeAll(pane.getChildren());
    }
}
