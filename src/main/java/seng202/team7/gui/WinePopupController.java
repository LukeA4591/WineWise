package seng202.team7.gui;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import seng202.team7.exceptions.DuplicateExc;
import seng202.team7.models.Wine;
import javafx.fxml.FXML;

import java.io.IOException;

/**
 * Controller for the wine_popup.fxml class, a border pane which loads further wine details
 */
public class WinePopupController {

    @FXML
    private Button wineDetailsButton;
    @FXML
    private Button wineReviewsButton;
    @FXML
    private Button wineUserRatingButton;

    @FXML
    private Button wineCloseButton;
    @FXML
    private Pane wineNavWindow;

    private Wine wine;
    private Image image;

    private final String turnButtonIndigo = "-fx-background-color: indigo; -fx-text-fill: white";

    /**
     * Default constructor for the wine popup controller
     */
    public WinePopupController() {

    }

    /**
     * Init method for the wine popup navbar
     * @param wine wine which details are being presented
     * @param image image depending on the colour of the wine
     */
    @FXML
    public void init(Wine wine, Image image) {
        this.image = image;
        this.wine = wine;
        Platform.runLater(() -> {
            Scene scene = wineNavWindow.getScene();
            if (scene != null) {
                scene.getStylesheets().add(getClass().getResource("/style/navbar.css").toExternalForm());
            }
        });
        wineDetailsButton.setStyle(turnButtonIndigo);
        loadDetailsScreen();
    }

    /**
     * Method to load the details screen into the pane
     */
    private void loadDetailsScreen() {
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

    /**
     * Method to load the reviews screen into the pane
     */
    private void loadReviewsScreen() {
        try {
            FXMLLoader reviewsScreenLoader = new FXMLLoader(getClass().getResource("/fxml/wine_reviews_screen.fxml"));
            Pane reviewsScreen = reviewsScreenLoader.load();
            WineReviewsScreenController reviewsScreenController = reviewsScreenLoader.getController();
            reviewsScreenController.init(wine);
            wineNavWindow.getChildren().clear();
            wineNavWindow.getChildren().add(reviewsScreen);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DuplicateExc e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to load the screen for the user to leave a review into the pane
     */
    private void loadUserRatingScreen() {
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

    /**
     * OnAction method for clicking the details button
     */
    @FXML
    void detailsButtonPressed() {
        loadDetailsScreen();
        setAllButtonsGrey();
        wineDetailsButton.setStyle(turnButtonIndigo);
    }

    /**
     * OnAction method for clicking the reviews button
     */
    @FXML
    void reviewsButtonPressed() {
        loadReviewsScreen();
        setAllButtonsGrey();
        wineReviewsButton.setStyle(turnButtonIndigo);
    }

    /**
     * OnAction method for clicking the ratings button
     */
    @FXML
    void ratingButtonPressed() {
        loadUserRatingScreen();
        setAllButtonsGrey();
        wineUserRatingButton.setStyle(turnButtonIndigo);
    }

    /**
     * onAction method for clicking the close button, will close the wine popup
     */
    @FXML
    void closeButtonPressed() {
        Stage stage = (Stage) (wineCloseButton.getScene().getWindow());
        stage.close();
        setAllButtonsGrey();
        wineCloseButton.setStyle(turnButtonIndigo);
    }

}
