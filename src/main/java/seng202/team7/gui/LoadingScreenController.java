package seng202.team7.gui;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Controller class for the loading screen
 */
public class LoadingScreenController {

    @FXML
    private ImageView wineWiseGif;

    /**
     * Default constructor for the LoadingScreenController class
     */
    public LoadingScreenController() {

    }

    /**
     * Initializes the loading screen, setting the screen to the loading screen gif
     */
    @FXML
    private void initialize() {
        Image wineWiseImage = new Image(getClass().getResourceAsStream("/images/Winewise_gif_fast.gif"));
        wineWiseGif.setImage(wineWiseImage);
    }
}
