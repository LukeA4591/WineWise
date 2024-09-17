package seng202.team0.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import seng202.team0.business.ReviewManager;
import seng202.team0.business.WineManager;
import seng202.team0.exceptions.DuplicateExc;
import seng202.team0.models.Review;
import seng202.team0.models.Wine;

import java.util.List;

public class WineReviewsScreenController {

    private ReviewManager reviewManager;
    private Wine wine;

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
    private WineManager wineManager = new WineManager();

    /**
     * Init method for wine reviews screen
     * @param wine wine which the screen is displaying
     * @throws DuplicateExc
     */
    @FXML
    public void init(Wine wine) throws DuplicateExc {
        this.wine = wine;
        reviewManager = new ReviewManager();
        displayAllReviews();
    }

    /**
     * Method to display all reviews in the review table
     */
    @FXML
    public void displayAllReviews() {
        int wineID = wineManager.getWineID(wine);
        List<Review> wineReviews = reviewManager.getReviewsByWineId(wineID);
        wineNameLabel.setText(wineNameLabel.getText() + wine.getWineName());
        wineryLabel.setText(wineryLabel.getText() + wine.getWineryString());
        vintageLabel.setText(vintageLabel.getText() + wine.getVintage());
        criticRatingLabel.setText("Critic rating: " + wine.getScore() + " / 100");

        ObservableList<Review> observableWineReviews = FXCollections.observableArrayList(wineReviews);

        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        reviewColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        reportColumn.setCellFactory(column -> new TableCell<Review, Boolean>() {
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
        ratingTable.setItems(observableWineReviews);

    }

}
