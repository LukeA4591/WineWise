package seng202.team0.gui;

import javafx.fxml.FXML;
import seng202.team0.models.Wine;

public class WineReviewsScreenController {

    Wine wine;

    public WineReviewsScreenController() {
    }

    @FXML
    public void init(Wine wine) {
        this.wine = wine;
    }
}
