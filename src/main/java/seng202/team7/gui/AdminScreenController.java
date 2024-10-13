package seng202.team7.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import seng202.team7.business.ReviewManager;
import seng202.team7.models.Review;
import seng202.team7.services.AppEnvironment;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for the admin_screen.fxml page.
 */
public class AdminScreenController {

    @FXML
    private TableView<Review> reviewTable;
    @FXML
    private TableColumn<Review, Integer> ratingColumn;
    @FXML
    private TableColumn<Review, String> reviewColumn;
    @FXML
    private TableColumn<Review, Boolean> flaggedColumn;
    @FXML
    private Button addWine;
    @FXML
    private Button helpButton;
    @FXML
    private Button addWinery;
    @FXML
    private Text selectedReviewText;
    @FXML
    private ScrollPane selectedReviewScrollPane;
    @FXML
    private Button deleteReviewButton;
    @FXML
    private Button unflagReviewButton;
    @FXML
    private Stage helpStage;
    @FXML
    private Text importErrorMessage;
    private final AppEnvironment appEnvironment;
    private final ReviewManager reviewManager;
    private final List<Review> selectedReviews = new ArrayList<>();



    /**
     * Constructor for AdminScreenController. Sets the AppEnvironment, wineManager, and reviewDAO variables so the
     * wines and reviews can be accessed and so the pages can be changed.
     * @param appEnvironment The AppEnvironment to let us launch other pages.
     */
    public AdminScreenController(AppEnvironment appEnvironment) {
        this.appEnvironment = appEnvironment;
        reviewManager = new ReviewManager();
    }

    /**
     * Initializes the controller by calling the displayFlaggedReviews method which sets a list of all the flagged
     * reviews.
     */
    @FXML
    public void initialize() {
        displayFlaggedReviews();
        deleteReviewButton.setDisable(true);
        unflagReviewButton.setDisable(true);
    }

    /**
     * Gets all the flagged reviews by calling the ReviewManager which will get the data from the DAO. It will then
     * fill the table up with all the flagged reviews with a tick box to the right of each one so the users can select
     * reviews to be unflagged or deleted.
     */
    @FXML
    public void displayFlaggedReviews() {

        List<Review> flaggedReviews = reviewManager.getFlaggedReviews();

        ObservableList<Review> observableWineReviews = FXCollections.observableArrayList(flaggedReviews);

        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        ratingColumn.setMinWidth(60);
        ratingColumn.setMaxWidth(60);
        ratingColumn.setPrefWidth(60);
        reviewColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        reviewColumn.setPrefWidth(370);
        reviewColumn.setMaxWidth(370);
        reviewColumn.setMinWidth(370);

        flaggedColumn.setMinWidth(60);
        flaggedColumn.setMaxWidth(60);
        flaggedColumn.setPrefWidth(60);

        setFlaggedColumnCellFactory();
        setReviewColumnCellFactory();

        reviewTable.setItems(observableWineReviews);
    }

    /**
     * Enables/Disables buttons if they're reviews in the table or not
     */
    private void checkReviewCount() {
        if (selectedReviews.isEmpty()) {
            deleteReviewButton.setDisable(true);
            unflagReviewButton.setDisable(true);
        } else {
            deleteReviewButton.setDisable(false);
            unflagReviewButton.setDisable(false);
        }
    }

    /**
     * making so checkbox is actually selecting a wine
     */
    private void setFlaggedColumnCellFactory() {
        flaggedColumn.setCellFactory(column -> new TableCell<>() {
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
                        if (isSelected && !selectedReviews.contains(getTableRow().getItem())) {
                            selectedReviews.add(getTableRow().getItem());
                        } else if (selectedReviews.contains((getTableRow().getItem()))) {
                            selectedReviews.remove(getTableRow().getItem());
                        }
                        checkReviewCount();
                    });
                    setGraphic(checkBox);
                }
            }
        });
    }

    /**
     * Updates the reviews in the Review columns if there is/should be one there
     */
    private void setReviewColumnCellFactory() {
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
    }

    /**
     * When delete reviews is pressed, it checks all the flagged reviews that have been selected and will then call the
     * ReviewManager to delete the entries.
     */
    @FXML
    public void deleteFlaggedReviews() {
        for (int i = selectedReviews.size() - 1; i >= 0; i--) { //iterate backwards to remove reviews from the selected reviews
            reviewManager.delete(selectedReviews.get(i).getReviewID());
            selectedReviews.remove(i);
        }
        displayFlaggedReviews();
        checkReviewCount();
        selectedReviewText.setText("Select a review to expand!");
    }

    /**
     * When delete reviews is pressed, it checks all the flagged reviews that have been selected and will then call the
     * ReviewManager to unflag the entries.
     */
    @FXML
    public void unflagFlaggedReviews() {
        for (int i = 0; i < selectedReviews.size(); i++) { //iterate backwards to remove reviews from the selected reviews
            reviewManager.markAsUnreported(selectedReviews.get(i).getReviewID());
            selectedReviews.remove(i);
        }
        displayFlaggedReviews();
        checkReviewCount();
        selectedReviewText.setText("Select a review to expand!");

    }

    /**
     * When the add wine button is pressed, it will open up the add_wine.fxml page as a pop-up. The pop-up will
     * restrict the user from doing any other actions on the admin page until they have closed the pop-up.
     */
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
            modalStage.setTitle("Add Wine");
            modalStage.initModality(Modality.WINDOW_MODAL);
            Stage primaryStage = (Stage) addWine.getScene().getWindow();
            modalStage.initOwner(primaryStage);
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * When the view wines button is pressed, it will open up the admin_view_wines.fxml page as a pop-up. The pop-up
     * will restrict the user from doing any other actions on the admin page until they have closed the pop-up.
     */
    @FXML
    public void onViewWines() {
        try {
            FXMLLoader newStageLoader = new FXMLLoader(getClass().getResource("/fxml/admin_view_wines.fxml"));
            AnchorPane root = newStageLoader.load();
            Scene modalScene = new Scene(root);
            Stage modalStage = new Stage();
            modalStage.setScene(modalScene);
            modalStage.setWidth(900);
            modalStage.setHeight(655);
            modalStage.setResizable(false);
            modalStage.setTitle("View Wines");
            modalStage.initModality(Modality.WINDOW_MODAL);
            Stage primaryStage = (Stage) addWine.getScene().getWindow();
            modalStage.initOwner(primaryStage);
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * On click of the help button launch the admin help screen
     */
    @FXML
    private void onHelp() {
        try {
            if (helpStage == null || !helpStage.isShowing()) {
                FXMLLoader newStageLoader = new FXMLLoader(getClass().getResource("/fxml/admin_help_popup.fxml"));
                BorderPane root = newStageLoader.load();
                Scene modalScene = new Scene(root);
                helpStage = new Stage();
                helpStage.setScene(modalScene);
                helpStage.setResizable(false);
                helpStage.setTitle("Admin Help Screen");
                helpStage.initModality(Modality.NONE);

                Stage primaryStage = (Stage) addWine.getScene().getWindow();
                helpStage.initOwner(primaryStage);
                helpStage.show();
            } else {
                helpStage.requestFocus();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Allows a csv file to be chosen from the file manager when the add dataset button is pressed. It will then send
     * this file to the wineManager along with the WineCSVImporter so that the file can be processed into individual
     * wines.
     */
    @FXML
    private void addDataSet() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        try {
            fileChooser.setInitialDirectory(new File(MainWindow.class.getProtectionDomain().getCodeSource()
                    .getLocation().toURI()).getParentFile());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) addWine.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null && file.exists()) {
            try {
                FXMLLoader newStageLoader = new FXMLLoader(getClass().getResource("/fxml/import_preview.fxml"));
                AnchorPane root = newStageLoader.load();
                Scene modalScene = new Scene(root);
                Stage modalStage = new Stage();
                ImportPreviewController importPreviewController = newStageLoader.getController();
                importPreviewController.init(file, appEnvironment);
                modalStage.setScene(modalScene);
                modalStage.setResizable(false);
                modalStage.setTitle("Add a Dataset");
                modalStage.initModality(Modality.WINDOW_MODAL);
                Stage primaryStage = (Stage) addWine.getScene().getWindow();
                modalStage.initOwner(primaryStage);
                modalStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Allows the admin to change the password by launching the change password window/modal
     */
    @FXML
    void adminChangePassword() {
        try {
            FXMLLoader newStageLoader = new FXMLLoader(getClass().getResource("/fxml/admin_change_password_popup.fxml"));
            AnchorPane root = newStageLoader.load();

            ChangePasswordPopupController controller = newStageLoader.getController();
            controller.init(appEnvironment);
            Scene modalScene = new Scene(root);
            Stage modalStage = new Stage();
            modalStage.setScene(modalScene);
            modalStage.setResizable(false);
            modalStage.setTitle("Change Password");
            modalStage.initModality(Modality.WINDOW_MODAL);
            modalStage.initOwner(addWine.getScene().getWindow());
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * On click of the add winery button, launches the add winery window/modal
     */
    @FXML
    void onAddWinery() {
        try {
            Stage primaryStage = (Stage) addWine.getScene().getWindow();
            FXMLLoader newStageLoader = new FXMLLoader(getClass().getResource("/fxml/admin_map_page.fxml"));
            AnchorPane root = newStageLoader.load();
            AdminMapPageController controller = newStageLoader.getController();
            Scene modalScene = new Scene(root);
            Stage modalStage = new Stage();
            controller.init(modalStage);
            modalStage.setScene(modalScene);
            modalStage.setWidth(900);
            modalStage.setHeight(624);
            modalStage.setResizable(false);
            modalStage.setTitle("Place Wineries");
            modalStage.initModality(Modality.WINDOW_MODAL);
            modalStage.initOwner(primaryStage);
            modalStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * When the logout button is pressed, the admin is logged out and taken back to the normal user homepage.
     */
    @FXML
    void adminLogout() {
        if (helpStage != null && helpStage.isShowing()) {
            helpStage.close();
        }
        appEnvironment.getClearRunnable().run();
        appEnvironment.launchNavBar();
    }
}