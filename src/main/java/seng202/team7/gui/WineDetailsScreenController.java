package seng202.team7.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import seng202.team7.business.WineManager;
import seng202.team7.models.Wine;
import javafx.scene.image.Image;
import seng202.team7.services.WinePopupService;

import java.util.List;

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

    @FXML
    private Pane recommendWine1;
    @FXML
    private ImageView recommendImage1;
    @FXML
    private Label recommendLabel1;
    @FXML
    private Pane recommendWine2;
    @FXML
    private ImageView recommendImage2;
    @FXML
    private Label recommendLabel2;
    @FXML
    private Pane recommendWine3;
    @FXML
    private ImageView recommendImage3;
    @FXML
    private Label recommendLabel3;

    @FXML
    ImageView[] RecommendedImageList;
    @FXML
    Label[] RecommendedLabelList;


    List<Wine> wineList;

    String wineName1;
    int score;
    int vintage;
    String region;
    String wineryString;
    String description;
    Wine wine;
    Stage stage;

    WineManager wineManager;

    private WinePopupService winePopupService;

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
        wineManager = new WineManager();
        winePopupService = new WinePopupService();
        showSimilarWines();
    }

    @FXML
    public void initialize() {
        recommendImage1.setImage(new Image(getClass().getResourceAsStream("/images/defaultwine.png")));
        recommendImage2.setImage(new Image(getClass().getResourceAsStream("/images/defaultwine.png")));
        recommendImage3.setImage(new Image(getClass().getResourceAsStream("/images/defaultwine.png")));

        RecommendedImageList = new ImageView[]{recommendImage1, recommendImage2, recommendImage3};
        RecommendedLabelList = new Label[]{recommendLabel1, recommendLabel2, recommendLabel3};

    }

    private void showSimilarWines() {
        wineList = wineManager.getTheSimilarWines(this.wine);

        for (int i = 0; i < wineList.size(); i++) {
            if (wineList.get(i) != null) {
                Image image = winePopupService.getImage(wineList.get(i));
                RecommendedImageList[i].setImage(image);

                RecommendedLabelList[i].setText(wineList.get(i).getWineName());
            }
        }
    }

    @FXML
    void recWine1Pressed() {
        winePopupService.winePressed(wineList.getFirst(), RecommendedImageList[0].getImage(), rating);
        stage = (Stage) wineYear.getScene().getWindow();
        stage.close();
    }
    @FXML
    void recWine2Pressed() {
        winePopupService.winePressed(wineList.get(1), RecommendedImageList[1].getImage(), rating);
        stage = (Stage) wineYear.getScene().getWindow();
        stage.close();
    }
    @FXML
    void recWine3Pressed() {
        winePopupService.winePressed(wineList.get(2), RecommendedImageList[2].getImage(), rating);
        stage = (Stage) wineYear.getScene().getWindow();
        stage.close();
    }

}
