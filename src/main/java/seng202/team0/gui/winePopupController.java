package seng202.team0.gui;

import seng202.team0.models.Rating;
import seng202.team0.models.Wine;
import javafx.fxml.FXML;

import java.util.List;

public class winePopupController {

    String colour;
    String wineName;
    int score;
    int vintage;
    String region;
    String wineryString;
    String description;
    List<Rating> userRatings;

    @FXML
    public void init() {
        /*
        colour = wine.getColor();
        wineName = wine.getWineName();
        score = wine.getScore();
        vintage = wine.getVintage();
        region = wine.getRegion();
        wineryString = wine.getWineryString();
        description = wine.getDescription();
        userRatings = wine.getUserRatings();
         */
    }
}
