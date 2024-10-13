package seng202.team7.gui;

import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import seng202.team7.business.ReviewManager;
import seng202.team7.exceptions.DuplicateExc;
import seng202.team7.models.Review;
import seng202.team7.models.Wine;

/**
 * Constructor for the wine_user_rating_screen.fxml file
 */
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
    private final int MAX_CHARACTERS = 500;
    private final String charRemaining = "Characters Remaining: ";

    /**
     * Default constructor for the WineUserRatingScreenController
     */
    public WineUserRatingScreenController() {

    }

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
        if (wine.getScore() != null) {
            criticRatingLabel.setText("Critic rating: " + wine.getScore() + " / 100");
        } else {
            criticRatingLabel.setText("Critic rating: No rating");
        }
        characterLimitLabel.setText(charRemaining + MAX_CHARACTERS);

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
            movedSlider = false;
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
        characterLimitLabel.setText(charRemaining + MAX_CHARACTERS);
    }

    /**
     * Method to make sure user has moved the slider before saving their review
     */
    @FXML
    private void sliderMoved() {
        movedSlider = true;
        savedLabel.setText("");
    }

    /**
     * Resets message when user opens the review text area.
     */
    @FXML
    private void textSelected() {
        savedLabel.setText("");
    }

    /**
     * Whenever the user types in the review text box, it checks if the review is too long.
     */
    @FXML
    private void checkCharacterLimit() {
        int textLength = reviewTextArea.getLength();
        characterLimitLabel.setText(charRemaining + (MAX_CHARACTERS - textLength));
    }



}
