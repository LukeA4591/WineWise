package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import seng202.team0.services.WineEnvironment;

import java.io.IOException;

public class NavBarController {

    private WineEnvironment wineEnvironment;
    @FXML
    private BorderPane mainWindow;
    private Stage stage;
    @FXML
    private Button homeButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button graphsButton;
    @FXML
    private Button mapsButton;

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
        loadHomePage(stage);
    }

    /**
     * Loads the home page into the border pane
     * @param stage stage
     */
    private void loadHomePage(Stage stage) {
        try {
            FXMLLoader homePageLoader = new FXMLLoader(getClass().getResource("/fxml/setup_screen.fxml"));
            Parent mainParent = homePageLoader.load();
            SetupScreenController homePageController = homePageLoader.getController();
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
            searchPageController.init(stage);
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