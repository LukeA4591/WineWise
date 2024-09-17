package seng202.team0.gui;

import javafx.beans.property.BooleanProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
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

    public WineReviewsScreenController() {
    }

    @FXML
    public void init(Wine wine) throws DuplicateExc {
        this.wine = wine;
        reviewDAO = new ReviewDAO();

//        Rating rating = new Rating(75, "PRETTY darn good", wine.getWineName(), wine.getWineryString(), wine.getVintage());
//        Rating rating2 = new Rating(25, "shit", wine.getWineName(), wine.getWineryString(), wine.getVintage());
//        Rating rating3 = new Rating(75, "yeah it was alright.", wine.getWineName(), wine.getWineryString(), wine.getVintage());
//
//        reviewDAO.add(rating);
//        reviewDAO.add(rating2);
//        reviewDAO.add(rating3);

        displayAllReviews();
    }


    //TODO make it so that checkboxes are inside the table and they correspond to the isReported value.
    @FXML
    public void displayAllReviews() {
        int wineID = reviewDAO.getWineID(wine);
        List<Rating> wineReviews = reviewDAO.getReviewsByWineId(wineID);
        wineNameLabel.setText(wineNameLabel.getText() + wine.getWineName());
        wineryLabel.setText(wineryLabel.getText() + wine.getWineryString());
        vintageLabel.setText(vintageLabel.getText() + wine.getVintage());
        criticRatingLabel.setText("Critic rating: " + wine.getScore() + " / 100");

        ObservableList<Rating> observableWineReviews = FXCollections.observableArrayList(wineReviews);

        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        reviewColumn.setCellValueFactory(new PropertyValueFactory<>("review"));
//        reportColumn.setCellValueFactory(new PropertyValueFactory<>("isReported"));

//        reportColumn = new TableColumn<>("isReported");
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
                        System.out.println(getTableRow().getItem());
                        getTableRow().getItem().setReported(checkBox.isSelected());
                        System.out.println(getTableRow().getItem().getReported());
                        reviewDAO.markAsReported(getTableRow().getItem().getReviewID());
                    });
                    setGraphic(checkBox);
                }
            }
        });
        ratingTable.setItems(observableWineReviews);


    }

}
