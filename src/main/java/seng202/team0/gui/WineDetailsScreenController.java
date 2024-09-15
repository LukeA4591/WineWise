package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import seng202.team0.models.Rating;
import seng202.team0.models.Wine;

import java.util.List;

public class WineDetailsScreenController {

    @FXML
    private Label wineName;

    @FXML
    private Label winery;

    @FXML
    private Label wineDesc;
    String colour;
    String wineName1;
    int score;
    int vintage;
    String region;
    String wineryString;
    String description;
    List<Rating> userRatings;

    Wine wine;

    public WineDetailsScreenController() {
    }

    @FXML
    public void init(Wine wine) {
        this.wine = wine;
        colour = wine.getColor();
        wineName1 = wine.getWineName();
        score = wine.getScore();
        vintage = wine.getVintage();
        region = wine.getRegion();
        wineryString = wine.getWineryString();
        description = wine.getDescription();
        userRatings = wine.getUserRatings();
        wineName.setText(wineName1);
        winery.setText(wineryString);
        wineDesc.setText(description);
    }
}
