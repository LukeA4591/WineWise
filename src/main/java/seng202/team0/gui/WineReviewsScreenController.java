package seng202.team0.gui;

import javafx.beans.property.BooleanProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.apache.poi.ss.usermodel.Table;
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
        List<Rating> wineReviews = reviewDAO.getReviewsByWineId(wine);
        wineNameLabel.setText(wineNameLabel.getText() + wine.getWineName());
        wineryLabel.setText(wineryLabel.getText() + wine.getWineryString());
        vintageLabel.setText(vintageLabel.getText() + wine.getVintage());
        criticRatingLabel.setText(criticRatingLabel.getText() + wine.getScore());

        ObservableList<Rating> observableWineReviews = FXCollections.observableArrayList(wineReviews);

        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        reviewColumn.setCellValueFactory(new PropertyValueFactory<>("review"));
//        reportColumn.setCellValueFactory(new PropertyValueFactory<>("isReported"));


        reportColumn.setCellFactory(column -> new CheckBoxTableCell<>());
        reportColumn.setCellValueFactory(cellData -> {
            Rating cellValue = cellData.getValue();
            BooleanProperty property = cellValue.getIsReported();

            // Add listener to handler change
            property.addListener((observable, oldValue, newValue) -> cellValue.setIsReported(newValue));

            return property;
        });

        ratingTable.setItems(observableWineReviews);


    }

}
