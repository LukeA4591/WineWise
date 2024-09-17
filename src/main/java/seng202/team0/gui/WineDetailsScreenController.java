package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import seng202.team0.models.Wine;
import javafx.scene.image.Image;

/**
 * Controller for the wine_details_screen.fxml file, shows further details of a wine
 */
public class WineDetailsScreenController {

    @FXML
    private Label wineYear;

    @FXML
    private Label wineRegion;

    @FXML
    private Label rating;

    @FXML
    private ImageView wineImage;

    @FXML
    private Label wineName;

    @FXML
    private Label winery;

    @FXML
    private Label wineDesc;
    String wineName1;
    int score;
    int vintage;
    String region;
    String wineryString;
    String description;
    Wine wine;

    /**
     * Provide all information about the wine to allow the user to read more about any specific wine
     * @param wine wine which is being displayed
     * @param image image of wine depending on class
     */
    @FXML
    public void init(Wine wine, Image image) {
        this.wine = wine;
        wineName1 = wine.getWineName();
        score = wine.getScore();
        vintage = wine.getVintage();
        String vintageString = Integer.toString(vintage);
        region = wine.getRegion();
        wineryString = wine.getWineryString();
        description = wine.getDescription();
        wineImage.setImage(image);
        wineName.setText(wineName1);
        winery.setText(wineryString);
        wineDesc.setText(description);
        wineRegion.setText(region);
        wineYear.setText(vintageString);
        rating.setText(wine.getScore() + " / 100");
    }
}
