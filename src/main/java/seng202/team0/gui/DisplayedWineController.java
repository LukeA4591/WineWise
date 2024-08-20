package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import seng202.team0.models.Wine;

import javax.swing.text.html.ImageView;

//import java.awt.*;

public class DisplayedWineController {

    @FXML
    private Label nameLabel;

    private Wine wine;

    public void setData(Wine wine){
        this.wine = wine;
        nameLabel.setText(wine.getWineName());
    }

//    public void setImageSize(int width, int height){
//        image.setSize(width, height);
//    }
}
