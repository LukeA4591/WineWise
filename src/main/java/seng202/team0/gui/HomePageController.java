package seng202.team0.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import seng202.team0.models.Wine;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import seng202.team0.repository.DatabaseManager;
import seng202.team0.repository.WineDAO;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HomePageController {

    @FXML
    private ImageView imageView1;

    @FXML
    private ImageView imageView2;

    @FXML
    private ImageView imageView3;

    @FXML
    private Label rating1;

    @FXML
    private Label rating2;

    @FXML
    private Label rating3;

    @FXML
    private Label desc1;

    @FXML
    private Label desc2;

    @FXML
    private Label desc3;

    @FXML
    private Label wine1;

    @FXML
    private Label wine2;

    @FXML
    private Label wine3;


    private Stage stage;

    private WineDAO wineDAO;
    /**
     * Init method for HomePageController
     * @param stage stage from NavBarController
     */
    public void init(Stage stage){
        this.stage = stage;
        wineDAO = new WineDAO();
        if (wineDAO.getAll().size() >= 3) {
            List<Wine> wines = wineDAO.getTopRated();
            displayWines(wines);
            displayWinery(wines);
            displayRatings(wines);
            setImage(wines);
        } else {
            System.out.println("NAY");
        }
    }

    public void displayWines(List<Wine> wines){
        wine1.setText(wines.get(0).getWineName());
        wine2.setText(wines.get(1).getWineName());
        wine3.setText(wines.get(2).getWineName());
    }

    public void displayWinery(List<Wine> wines){
        desc1.setText("Winery: " + wines.get(0).getWineryString());
        desc2.setText("Winery: " + wines.get(1).getWineryString());
        desc3.setText("Winery: " + wines.get(2).getWineryString());
    }

    public void displayRatings(List<Wine> wines){
        rating1.setText(wines.get(0).getScore() + " / 100");
        rating2.setText(wines.get(1).getScore() + " / 100");
        rating3.setText(wines.get(2).getScore() + " / 100");
    }

    public void setImage(List<Wine> wines) {
        List<ImageView> imageViews = Arrays.asList(imageView1, imageView2, imageView3);
        for (int i=0; i < 3; i++) {
            Image image = getImage(wines.get(i));
            ImageView imageView = imageViews.get(i);
            imageView.setImage(image);
        }
    }

    public Image getImage(Wine wine) {
        String imagePath;
        switch(wine.getColor()) {
            case "Red":
                imagePath = "/images/redwine.png";
                break;
            case "White":
                imagePath = "/images/whitewine.png";
                break;
            case "Ros�":
                imagePath = "/images/rose.png";
                break;
            default:
                imagePath = "/images/defaultwine.png";
        }
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        return image;
    }

    @FXML
    void wine1Pressed() {
        List<Wine> wines = wineDAO.getTopRated();
        Wine wine = wines.get(0);
        Image image = getImage(wine);
        winePressed(wine, image);
    }

    @FXML
    void wine2Pressed() {
        List<Wine> wines = wineDAO.getTopRated();
        Wine wine = wines.get(1);
        Image image = getImage(wine);
        winePressed(wine, image);
    }

    @FXML
    void wine3Pressed() {
        List<Wine> wines = wineDAO.getTopRated();
        Wine wine = wines.get(2);
        Image image = getImage(wine);
        winePressed(wine, image);
    }


    void winePressed(Wine wine, Image image) {
        try {
            // load a new fxml file
            FXMLLoader newStageLoader = new FXMLLoader(getClass().getResource("/fxml/wine_popup.fxml"));
            BorderPane root = newStageLoader.load();

            winePopupController controller = newStageLoader.getController();
            controller.init(wine, image);
            Scene modalScene = new Scene(root);
            Stage modalStage = new Stage();
            modalStage.setScene(modalScene);
            modalStage.setResizable(false);
            modalStage.setTitle("Wine Popup");
            modalStage.initModality(Modality.WINDOW_MODAL);
            modalStage.initOwner(rating1.getScene().getWindow());
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
