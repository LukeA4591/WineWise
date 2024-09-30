package seng202.team0.gui;

import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import seng202.team0.business.ReviewManager;
import seng202.team0.exceptions.DuplicateExc;
import seng202.team0.models.Review;
import seng202.team0.models.Wine;
import seng202.team0.repository.ReviewDAO;

public class WineUserRatingScreenController {

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
    private Label savedLabel;
    @FXML
    private Label characterLimitLabel;

    Wine wine;
    private ReviewManager reviewManager;
    private boolean movedSlider = false;
    private final int MAX_CHARACTERS = 60;

    /**
     * Init method for the user rating screen
     * @param wine Wine that is selected.
     */
    @FXML
    public void init(Wine wine) {
        this.wine = wine;
        reviewManager = new ReviewManager();
        wineNameLabel.setText(wineNameLabel.getText() + wine.getWineName());
        wineryLabel.setText(wineryLabel.getText() + wine.getWineryString());
        vintageLabel.setText(vintageLabel.getText() + wine.getVintage());
        criticRatingLabel.setText("Critic rating: " + wine.getScore() + " / 100");
        characterLimitLabel.setText("Characters Remaining: " + MAX_CHARACTERS);

        TextFormatter<String> textFormatter = new TextFormatter<>(text -> {
            if (text.getControlNewText().length() > MAX_CHARACTERS) {
                return null;
            }
            return text;
        });

        reviewTextArea.setTextFormatter(textFormatter);
    }

    /**
     * OnAction method for the user to save their review, adds review to database
     * @throws DuplicateExc if review already exists
     */
    @FXML
    private void saveReview() throws DuplicateExc {
        if (movedSlider) {
            Review review = new Review((int) ratingSlider.getValue(), reviewTextArea.getText(), wine);
            reviewManager.add(review);
            savedLabel.setText("Saved!");
            savedLabel.setStyle("-fx-text-fill: indigo");
            resetReview();
        } else {
            savedLabel.setText("Please Move Slider");
            savedLabel.setStyle("-fx-text-fill: red");
        }
    }

    /**
     * Resets the text box and the slider
     */
    @FXML
    private void resetReview() {
        ratingSlider.setValue(0);
        reviewTextArea.setText("");
    }

    /**
     * Method to make sure user has moved the slider before saving their review
     */
    @FXML
    private void sliderMoved() {
        movedSlider = true;
    }

    @FXML
    private void checkCharacterLimit() {
        int textLength = reviewTextArea.getLength();
        characterLimitLabel.setText("Characters Remaining: " + (MAX_CHARACTERS - textLength));
    }



}
