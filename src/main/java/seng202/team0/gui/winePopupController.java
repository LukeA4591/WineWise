package seng202.team0.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import seng202.team0.models.Rating;
import seng202.team0.models.Wine;
import javafx.scene.control.Label;
import javafx.fxml.FXML;

import java.io.IOException;
import java.util.List;

public class winePopupController {

    @FXML
    private Button wineDetailsButton;

    @FXML
    private Button wineReviewsButton;

    @FXML
    private Button wineUserRatingButton;

    @FXML
    private Pane wineNavWindow;

    private Wine wine;

    private Image image;
    @FXML
    public void init(Wine wine, Image image) {
        this.image = image;
        this.wine = wine;
        wineDetailsButton.setStyle("-fx-background-color: indigo; -fx-text-fill: white");
        loadDetailsScreen(wine, image);
    }

    private void loadDetailsScreen(Wine wine, Image image) {
        try {
            FXMLLoader detailsScreenLoader = new FXMLLoader(getClass().getResource("/fxml/wine_details_screen.fxml"));
            Pane detailsScreen = detailsScreenLoader.load();
            WineDetailsScreenController detailsScreenController = detailsScreenLoader.getController();
            detailsScreenController.init(wine, image);
            wineNavWindow.getChildren().clear();
            wineNavWindow.getChildren().add(detailsScreen);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadReviewsScreen(Wine wine) {
        try {
            FXMLLoader reviewsScreenLoader = new FXMLLoader(getClass().getResource("/fxml/wine_reviews_screen.fxml"));
            Pane reviewsScreen = reviewsScreenLoader.load();
            WineReviewsScreenController reviewsScreenController = reviewsScreenLoader.getController();
            reviewsScreenController.init(wine);
            wineNavWindow.getChildren().clear();
            wineNavWindow.getChildren().add(reviewsScreen);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUserRatingScreen(Wine wine) {
        try {
            FXMLLoader userRatingScreenLoader = new FXMLLoader(getClass().getResource("/fxml/wine_user_rating_screen.fxml"));
            Pane userRatingScreen = userRatingScreenLoader.load();
            WineUserRatingScreenController userRatingScreenController = userRatingScreenLoader.getController();
            userRatingScreenController.init(wine);
            wineNavWindow.getChildren().clear();
            wineNavWindow.getChildren().add(userRatingScreen);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper function to set all the buttons grey
     */
    private void setAllButtonsGrey() {
        wineDetailsButton.setStyle("");
        wineReviewsButton.setStyle("");
        wineUserRatingButton.setStyle("");
    }

    @FXML
    void detailsButtonPressed() {
        loadDetailsScreen(wine, image);
        setAllButtonsGrey();
        wineDetailsButton.setStyle("-fx-background-color: indigo; -fx-text-fill: white");
    }

    @FXML
    void reviewsButtonPressed() {
        loadReviewsScreen(wine);
        setAllButtonsGrey();
        wineReviewsButton.setStyle("-fx-background-color: indigo; -fx-text-fill: white");
    }

    @FXML
    void ratingButtonPressed() {
        loadUserRatingScreen(wine);
        setAllButtonsGrey();
        wineUserRatingButton.setStyle("-fx-background-color: indigo; -fx-text-fill: white");
    }

}
