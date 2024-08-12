package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import seng202.team0.models.Winery;

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
        new Winery(this::launchSetupScreen);
    }

    /**
     * Launches the setup screen.
     *
     * @param winery the game environment which manages the game's state.
     */
    public void launchSetupScreen(final Winery winery) {
        try {
            FXMLLoader setupLoader = new FXMLLoader(getClass().getResource("/fxml/setup_screen.fxml"));
            setupLoader.setControllerFactory(param -> new SetupScreenController(winery));
            Parent setupParent  = setupLoader.load();
            pane.getChildren().add(setupParent);
            stage.setTitle("Setup Screen");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
