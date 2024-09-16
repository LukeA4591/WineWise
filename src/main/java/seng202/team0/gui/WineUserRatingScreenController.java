package seng202.team0.gui;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import seng202.team0.exceptions.DuplicateExc;
import seng202.team0.models.Rating;
import seng202.team0.models.Wine;
import seng202.team0.repository.ReviewDAO;

public class WineUserRatingScreenController {
    Wine wine;

    @FXML
    private Label wineNameLabel;
    @FXML
    private Label criticRatingLabel;
    @FXML
    private Label wineryLabel;
    @FXML
    private Label vintageLabel;
    @FXML
    private Slider ratingSlider;
    @FXML
    private TextArea reviewTextArea;
    @FXML
    private Button saveReviewButton;
    @FXML
    private Label savedLabel;
    @FXML
    private Button resetReviewButton;
    private ReviewDAO reviewDAO = new ReviewDAO();
    private boolean movedSlider = false;


    public WineUserRatingScreenController() {
    }

    /**
     * Init method for the user rating screen
     * @param wine
     */
    @FXML
    public void init(Wine wine) {
        this.wine = wine;
        wineNameLabel.setText(wineNameLabel.getText() + wine.getWineName());
        wineryLabel.setText(wineryLabel.getText() + wine.getWineryString());
        vintageLabel.setText(vintageLabel.getText() + wine.getVintage());
        criticRatingLabel.setText("Critic rating: " + wine.getScore() + " / 100");
    }

    @FXML
    private void saveReview() throws DuplicateExc {
        if (movedSlider) {
            Rating rating = new Rating((int) ratingSlider.getValue(), reviewTextArea.getText(), wine.getWineName(), wine.getWineryString(), wine.getVintage());
            reviewDAO.add(rating);
            savedLabel.setText("Saved!");
            savedLabel.setStyle("-fx-text-fill: indigo");
            resetReview();
        } else {
            savedLabel.setText("Please Move Slider");
            savedLabel.setStyle("-fx-text-fill: red");
        }
    }

    @FXML
    private void resetReview() {
        ratingSlider.setValue(0);
        reviewTextArea.setText("");
    }

    @FXML
    private void sliderMoved() {
        movedSlider = true;
    }



}
