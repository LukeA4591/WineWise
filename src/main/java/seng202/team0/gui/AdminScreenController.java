package seng202.team0.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import seng202.team0.business.WineManager;
import seng202.team0.io.WineCSVImporter;
import seng202.team0.models.Rating;
import seng202.team0.models.Wine;
import seng202.team0.repository.ReviewDAO;
import seng202.team0.services.AdminLoginService;
import seng202.team0.services.WineEnvironment;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class AdminScreenController {

    @FXML
    private TableView<Rating> ratingTable;

    @FXML
    private TableColumn<Rating, Integer> ratingColumn;

    @FXML
    private TableColumn<Rating, String> reviewColumn;

    @FXML
    private TableColumn<Rating, Boolean> flaggedColumn;

    @FXML
    Button addWine;
    private final WineEnvironment winery;
    private final WineManager wineManager;
    private Stage stage;

    private ReviewDAO reviewDAO;

    public AdminScreenController(WineEnvironment winery) {
        this.winery = winery;
        wineManager = new WineManager();
        reviewDAO = new ReviewDAO();
    }

    @FXML
    public void initialize() {
        displayFlaggedReviews();
    }

    @FXML
    public void displayFlaggedReviews() {
        List<Rating> flaggedReviews = reviewDAO.getFlaggedReviews();
        ObservableList<Rating> observableWineReviews = FXCollections.observableArrayList(flaggedReviews);

        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        reviewColumn.setCellValueFactory(new PropertyValueFactory<>("review"));
        flaggedColumn.setCellFactory(column -> new TableCell<Rating, Boolean>() {
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
//                        reviewDAO.markAsReported(getTableRow().getItem().getReviewID());
                    });
                    setGraphic(checkBox);
                }
            }
        });
        ratingTable.setItems(observableWineReviews);


    }

    @FXML
    public void onAddWine() {
        try {
            FXMLLoader newStageLoader = new FXMLLoader(getClass().getResource("/fxml/add_wine.fxml"));
            AnchorPane root = newStageLoader.load();
            Scene modalScene = new Scene(root);
            Stage modalStage = new Stage();
            modalStage.setScene(modalScene);
            modalStage.setWidth(600);
            modalStage.setHeight(500);
            modalStage.setResizable(false);
            modalStage.setTitle("Wine Popup");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            Stage primaryStage = (Stage) addWine.getScene().getWindow();
            modalStage.initOwner(primaryStage);
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Allows csv file to be chosen when the add dataset button is pressed.
     */
    @FXML
    private void addDataSet() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        try {
            fileChooser.setInitialDirectory(new File(MainController.class.getProtectionDomain().getCodeSource()
                    .getLocation().toURI()).getParentFile());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        stage = (Stage) addWine.getScene().getWindow(); // Need to have scene variable, has to find scene through addWine button
        File file = fileChooser.showOpenDialog(stage);
        wineManager.addAllWinesFromFile(new WineCSVImporter(), file);
    }

    /**
     * Chooses data type selected in combo box
     * @return The string of the data type
     */

    @FXML
    void adminLogout() {
        winery.getClearRunnable().run();
        winery.launchNavBar();
    }
}