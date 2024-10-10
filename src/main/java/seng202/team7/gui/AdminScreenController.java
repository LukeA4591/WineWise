package seng202.team7.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
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
import seng202.team7.business.WineManager;
import seng202.team7.io.WineCSVImporter;
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

    private final AppEnvironment appEnvironment;
    private final WineManager wineManager;
    private final ReviewManager reviewManager;
    private final List<Review> selectedReviews = new ArrayList<>();



    /**
     * Constructor for AdminScreenController. Sets the AppEnvironment, wineManager, and reviewDAO variables so the
     * wines and reviews can be accessed and so the pages can be changed.
     * @param appEnvironment The AppEnvironment to let us launch other pages.
     */
    public AdminScreenController(AppEnvironment appEnvironment) {
        this.appEnvironment = appEnvironment;
        wineManager = new WineManager();
        reviewManager = new ReviewManager();
    }

    /**
     * Initializes the controller by calling the displayFlaggedReviews method which sets a list of all the flagged
     * reviews.
     */
    @FXML
    public void initialize() {
        displayFlaggedReviews();
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
        flaggedColumn.setCellFactory(column -> new TableCell<Review, Boolean>() {
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
                    });
                    setGraphic(checkBox);
                }
            }
        });

        reviewColumn.setCellFactory(column -> new TableCell<Review, String>() {
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
        reviewTable.setItems(observableWineReviews);
    }

    /**
     * When delete reviews is pressed, it checks all the flagged reviews that have been selected and will then call the
     * ReviewManager to delete the entries.
     */
    @FXML
    public void deleteFlaggedReviews() {
        for (int i = 0; i < selectedReviews.size(); i++) {
            reviewManager.delete(selectedReviews.get(i).getReviewID());
        }
        displayFlaggedReviews();
        selectedReviewText.setText("Select a review to expand!");
    }

    /**
     * When delete reviews is pressed, it checks all the flagged reviews that have been selected and will then call the
     * ReviewManager to unflag the entries.
     */
    @FXML
    public void unflagFlaggedReviews() {
        for (int i = 0; i < selectedReviews.size(); i++) {
            reviewManager.markAsUnreported(selectedReviews.get(i).getReviewID());
        }
        displayFlaggedReviews();
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
            modalStage.initModality(Modality.APPLICATION_MODAL);
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
     * The loading screen is shown whilst waiting for the app to finish loading in the wines on the background thread.
     */
    @FXML
    public void onViewWines() {
        Stage primaryStage = (Stage) addWine.getScene().getWindow();
        Platform.runLater(() -> {
            appEnvironment.setLoadingScreenOwner(primaryStage);
            appEnvironment.showLoadingScreen();
        });

        //add batch on background thread.
        Thread viewWinesThread = new Thread(() -> {

            Platform.runLater(() -> {
                try {
                    FXMLLoader newStageLoader = new FXMLLoader(getClass().getResource("/fxml/admin_view_wines.fxml"));
                    AnchorPane root = newStageLoader.load();
                    appEnvironment.hideLoadingScreen();
                    Scene modalScene = new Scene(root);
                    Stage modalStage = new Stage();
                    modalStage.setScene(modalScene);
                    modalStage.setWidth(900);
                    modalStage.setHeight(655);
                    modalStage.setResizable(false);
                    modalStage.setTitle("View Wines");
                    modalStage.initModality(Modality.APPLICATION_MODAL);
                    modalStage.initOwner(primaryStage);
                    modalStage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });

        viewWinesThread.start();

    }

    @FXML
    private void onHelp() {
        try {
            FXMLLoader newStageLoader = new FXMLLoader(getClass().getResource("/fxml/admin_help_popup.fxml"));
            BorderPane root = newStageLoader.load();
            Scene modalScene = new Scene(root);
            Stage modalStage = new Stage();
            modalStage.setScene(modalScene);
            modalStage.setResizable(false);
            modalStage.setTitle("Admin Help Screen");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            Stage primaryStage = (Stage) addWine.getScene().getWindow();
            modalStage.initOwner(primaryStage);
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Allows a csv file to be chosen from the file manager when the add dataset button is pressed. It will then send
     * this file to the wineManager along with the WineCSVImporter so that the file can be processed into individual
     * wines.
     * The loading screen is shown whilst waiting for the app to finish reading all the wine tuples from the csv file.
     */
    @FXML
    private void addDataSet() throws InterruptedException {
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
            //show loading screen on JAVAFX thread
            Platform.runLater(() -> {
                appEnvironment.setLoadingScreenOwner(stage);
                appEnvironment.showLoadingScreen();
            });

            //add batch on background thread.
            Thread addBatchThread = new Thread(() -> {

                wineManager.addBatch(new WineCSVImporter(), file);

                Platform.runLater(() -> appEnvironment.hideLoadingScreen());
            });

            addBatchThread.start();
        }

    }

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

    @FXML
    void onAddWinery() {
        Stage primaryStage = (Stage) addWine.getScene().getWindow();
        Platform.runLater(() -> {
            appEnvironment.setLoadingScreenOwner(primaryStage);
            appEnvironment.showLoadingScreen();
        });

        //add batch on background thread.
        Thread viewWinesThread = new Thread(() -> {

            Platform.runLater(() -> {
                try {
                    FXMLLoader newStageLoader = new FXMLLoader(getClass().getResource("/fxml/admin_map_page.fxml"));
                    AnchorPane root = newStageLoader.load();
                    appEnvironment.hideLoadingScreen();
                    AdminMapPageController controller = newStageLoader.getController();
                    Scene modalScene = new Scene(root);
                    Stage modalStage = new Stage();
                    controller.init(appEnvironment, modalStage);
                    modalStage.setScene(modalScene);
                    modalStage.setWidth(900);
                    modalStage.setHeight(624);
                    modalStage.setResizable(false);
                    modalStage.setTitle("Place Wineries");
                    modalStage.initModality(Modality.APPLICATION_MODAL);
                    modalStage.initOwner(primaryStage);
                    modalStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });

        viewWinesThread.start();
    }

    /**
     * When the logout button is pressed, the admin is logged out and taken back to the normal user homepage.
     */
    @FXML
    void adminLogout() {
        appEnvironment.getClearRunnable().run();
        appEnvironment.launchNavBar();
    }
}