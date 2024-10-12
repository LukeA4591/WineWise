package seng202.team7.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import seng202.team7.business.ReviewManager;
import seng202.team7.business.WineManager;
import seng202.team7.exceptions.DuplicateExc;
import seng202.team7.models.Review;
import seng202.team7.models.Wine;

import java.util.List;

/**
 * Controller class for the wine_reviews_screen.fxml file
 */
public class WineReviewsScreenController {

    @FXML
    private TableView<Review> ratingTable;
    @FXML
    private TableColumn<Review, Integer> ratingColumn;
    @FXML
    private TableColumn<Review, String> reviewColumn;
    @FXML
    private TableColumn<Review, Boolean> reportColumn;
    @FXML
    private Label wineNameLabel;
    @FXML
    private Label wineryLabel;
    @FXML
    private Label vintageLabel;
    @FXML
    private Label criticRatingLabel;
    @FXML
    private Text selectedReviewText;
    @FXML
    private ScrollPane selectedReviewScrollPane;

    private WineManager wineManager = new WineManager();
    private ReviewManager reviewManager;
    private Wine wine;

    /**
     * Default constructor for the WineReviewsScreenController
     */
    public WineReviewsScreenController() {

    }

    /**
     * Init method for wine reviews screen
     * @param wine wine which the screen is displaying
     * @throws DuplicateExc Throw exception if a duplicate is created.
     */
    @FXML
    public void init(Wine wine) throws DuplicateExc {
        this.wine = wine;
        reviewManager = new ReviewManager();
        displayAllReviews();
        selectedReviewText.wrappingWidthProperty().bind(selectedReviewScrollPane.widthProperty().subtract(20));
        selectedReviewScrollPane.setPannable(true);
        selectedReviewScrollPane.setContent(selectedReviewText);

        wineNameLabel.setText(wineNameLabel.getText() + wine.getWineName());
        wineryLabel.setText(wineryLabel.getText() + wine.getWineryString());
        vintageLabel.setText(vintageLabel.getText() + wine.getVintage());
        if (wine.getScore() != null) {
            criticRatingLabel.setText("Critic rating: " + wine.getScore() + " / 100");
        } else {
            criticRatingLabel.setText("Critic rating: No rating");
        }
    }

    /**
     * Method to display all reviews in the review table
     */
    @FXML
    public void displayAllReviews() {
        int wineID = wineManager.getWineID(wine);
        List<Review> wineReviews = reviewManager.getReviewsByWineId(wineID);
        ObservableList<Review> observableWineReviews = FXCollections.observableArrayList(wineReviews);

        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        reviewColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        reportColumn.setCellFactory(column -> new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    checkBox.setSelected(item != null && item);
                    checkBox.setOnAction(event -> {
                        boolean isSelected = checkBox.isSelected();
                        getTableRow().getItem().setReported(checkBox.isSelected());
                        if (isSelected) {
                            reviewManager.markAsReported(getTableRow().getItem().getReviewID());
                        } else {
                            reviewManager.markAsUnreported(getTableRow().getItem().getReviewID());
                        }
                    });
                    setGraphic(checkBox);
                }
            }
        });

        setReviewCellFactory(observableWineReviews);

    }

    /**
     * Helper method to set the wine reviews table to click to expand
     * @param observableWineReviews observable list of all reviews on the wine
     */
    private void setReviewCellFactory(ObservableList<Review> observableWineReviews) {
        reviewColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                }
            }

            {
                this.setOnMouseClicked(event -> {
                    if (!isEmpty()) {
                        String fullText = getItem();
                        selectedReviewText.setText(fullText);
                        selectedReviewScrollPane.setContent(selectedReviewText);
                    }
                });
            }
        });

        ratingTable.setItems(observableWineReviews);
    }

}
