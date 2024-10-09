package seng202.team7.gui;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LoadingScreenController {

    @FXML
    private ImageView wineWiseGif;

    @FXML
    private void initialize() {
        Image wineWiseImage = new Image(getClass().getResourceAsStream("/images/Winewise_gif_fast.gif"));
        wineWiseGif.setImage(wineWiseImage);
    }
}
