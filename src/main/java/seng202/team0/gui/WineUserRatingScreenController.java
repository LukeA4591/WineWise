package seng202.team0.gui;

import javafx.fxml.FXML;
import seng202.team0.models.Wine;
public class WineUserRatingScreenController {

    Wine wine;

    public WineUserRatingScreenController() {
    }

    @FXML
    public void init(Wine wine) {
        this.wine = wine;
    }

}
