package seng202.team0.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import seng202.team0.business.WineManager;
import seng202.team0.exceptions.DuplicateExc;
import seng202.team0.models.Rating;
import seng202.team0.models.Wine;
import seng202.team0.repository.ReviewDAO;

import java.util.List;

public class WineReviewsScreenController {

    private ReviewDAO reviewDAO;
    private Wine wine;

    @FXML
    private TableView<Rating> ratingTable;

    @FXML
    private TableColumn<Rating, Integer> ratingColumn;

    @FXML
    private TableColumn<Rating, String> reviewColumn;

    @FXML
    private TableColumn<Rating, Boolean> reportColumn;

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
        reviewDAO = new ReviewDAO();
        displayAllReviews();
    }

    /**
     * Method to display all reviews in the review table
     */
    @FXML
    public void displayAllReviews() {
        int wineID = wineManager.getWineId(wine);
        List<Rating> wineReviews = reviewDAO.getReviewsByWineId(wineID);
        wineNameLabel.setText(wineNameLabel.getText() + wine.getWineName());
        wineryLabel.setText(wineryLabel.getText() + wine.getWineryString());
        vintageLabel.setText(vintageLabel.getText() + wine.getVintage());
        criticRatingLabel.setText("Critic rating: " + wine.getScore() + " / 100");

        ObservableList<Rating> observableWineReviews = FXCollections.observableArrayList(wineReviews);

        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        reviewColumn.setCellValueFactory(new PropertyValueFactory<>("review"));
        reportColumn.setCellFactory(column -> new TableCell<Rating, Boolean>() {
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
                            reviewDAO.markAsReported(getTableRow().getItem().getReviewID());
                        } else {
                            reviewDAO.markAsUnreported(getTableRow().getItem().getReviewID());
                        }
                    });
                    setGraphic(checkBox);
                }
            }
        });
        ratingTable.setItems(observableWineReviews);

    }

}
