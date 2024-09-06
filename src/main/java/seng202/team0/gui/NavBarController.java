package seng202.team0.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import seng202.team0.services.WineEnvironment;

import java.io.IOException;

public class NavBarController {

    private WineEnvironment wineEnvironment;
    @FXML
    private BorderPane mainWindow;
    @FXML
    private AnchorPane navWindow;
    private Stage stage;
    @FXML
    private Button homeButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button graphsButton;
    @FXML
    private Button mapsButton;
    @FXML
    private Button adminButton;

    /**
     * NavBarController initializer, needs to be empty for FXML
     */
    public NavBarController() {
    }

    /**
     * FXML initialize method, loads home page
     */
    @FXML
    private void initialize() {

        homeButton.setStyle("-fx-background-color: indigo; -fx-text-fill: white");
        Platform.runLater(() -> {
            Scene scene = mainWindow.getScene();
            if (scene != null) {
                scene.getStylesheets().add(getClass().getResource("/style/navbar.css").toExternalForm());
            }
        });
        loadHomePage(stage);

    }

    /**
     * Loads the home page into the border pane
     * @param stage stage
     */
    private void loadHomePage(Stage stage) {
        try {
            FXMLLoader homePageLoader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"));
            Parent mainParent = homePageLoader.load();
            HomePageController homePageController = homePageLoader.getController();
            homePageController.init(stage);
            mainWindow.setCenter(mainParent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the search page into the border pane
     * @param stage stage
     */
    private void loadSearchPage(Stage stage) {
        try {
            FXMLLoader searchPageLoader = new FXMLLoader(getClass().getResource("/fxml/search_screen.fxml"));
            Parent searchParent = searchPageLoader.load();
            SearchPageController searchPageController = searchPageLoader.getController();
            mainWindow.setCenter(searchParent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the graphs page into the border pane
     * @param stage
     */
    private void loadGraphsPage(Stage stage) {
        try {
            FXMLLoader homePageLoader = new FXMLLoader(getClass().getResource("/fxml/graphs_page.fxml"));
            Parent graphsParent = homePageLoader.load();
            GraphsPageController graphsPageController = homePageLoader.getController();
            graphsPageController.init(stage);
            mainWindow.setCenter(graphsParent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the maps page into the border pane
     * @param stage
     */
    private void loadMapsPage(Stage stage) {
        loadHomePage(this.stage);
        setAllButtonsGrey();
        mapsButton.setStyle("-fx-background-color: indigo; -fx-text-fill: white");
    }

    /**
     * On Action method for the Home button
     */
    @FXML
    void homePressed() {
        loadHomePage(this.stage);
        setAllButtonsGrey();
        homeButton.setStyle("-fx-background-color: indigo; -fx-text-fill: white");
    }

    /**
     * On Action method for the Search button
     */
    @FXML
    void searchPressed() {
        loadSearchPage(this.stage);
        setAllButtonsGrey();
        searchButton.setStyle("-fx-background-color: indigo; -fx-text-fill: white");
    }

    /**
     * On Action method for the Graphs button
     */
    @FXML
    void graphsPressed() {
        loadGraphsPage(this.stage);
        setAllButtonsGrey();
        graphsButton.setStyle("-fx-background-color: indigo; -fx-text-fill: white");
    }

    /**
     * On Action method for the Maps button
     */
    @FXML
    void mapsPressed() {
        loadMapsPage(this.stage);
        setAllButtonsGrey();
        mapsButton.setStyle("-fx-background-color: indigo; -fx-text-fill: white");
    }

    @FXML
    void loginPressed() {
        try {
            // load a new fxml file
            FXMLLoader newStageLoader = new FXMLLoader(getClass().getResource("/fxml/admin_login_popup.fxml"));
            AnchorPane root = newStageLoader.load();

            AdminLoginPopupController controller = newStageLoader.getController();
            controller.init(wineEnvironment);
            Scene modalScene = new Scene(root);
            Stage modalStage = new Stage();
            modalStage.setScene(modalScene);
            modalStage.setWidth(600);
            modalStage.setHeight(400);
            modalStage.setResizable(false);
            modalStage.setTitle("Admin Login Popup");
            // If we want the modal to not block the other window we can change modality to Modality.NONE
            modalStage.initModality(Modality.WINDOW_MODAL);
            modalStage.initOwner(adminButton.getScene().getWindow());
            // Show the modal and wait for it to be closed
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Helper function to set all the buttons grey
     */
    private void setAllButtonsGrey() {
        homeButton.setStyle("");
        searchButton.setStyle("");
        graphsButton.setStyle("");
        mapsButton.setStyle("");
    }

    /**
     * The Setter method for the wineEnvironment
     * @param wineEnvironment wine environment
     */
    public void setWineEnvironment(WineEnvironment wineEnvironment) {
        this.wineEnvironment = wineEnvironment;
    }

    /**
     * The Setter method for the stage
     * @param stage stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}