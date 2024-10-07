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
import seng202.team0.services.AppEnvironment;

import java.io.IOException;

/**
 * Controller class for the nav_bar.fxml file, launches a border pane to load other panes inside
 */
public class NavBarController {

    @FXML
    private BorderPane mainWindow;
    private Stage stage;

    @FXML
    private AnchorPane navWindow;
    private AppEnvironment appEnvironment;

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
        Platform.runLater(() -> {
            Scene scene = mainWindow.getScene();
            if (scene != null) {
                scene.getStylesheets().add(getClass().getResource("/style/navbar.css").toExternalForm());
            }
        });
        loadHomePage();

    }

    /**
     * Loads the home page into the border pane
     */
    private void loadHomePage() {
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
     * Loads the help page into the border pane
     */
    private void loadHelpPage() {
        try {
            FXMLLoader helpPageLoader = new FXMLLoader(getClass().getResource("/fxml/user_help_page.fxml"));
            Parent helpParent = helpPageLoader.load();
            mainWindow.setCenter(helpParent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the search page into the border pane
     */
    private void loadSearchPage() {
        try {
            FXMLLoader searchPageLoader = new FXMLLoader(getClass().getResource("/fxml/search_screen.fxml"));
            Parent searchParent = searchPageLoader.load();
            mainWindow.setCenter(searchParent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMapPage() {
        try {
            FXMLLoader mapPageLoader = new FXMLLoader(getClass().getResource("/fxml/map_page.fxml"));
            Parent mapParent = mapPageLoader.load();
            MapPageController mapPageController = mapPageLoader.getController();
            mapPageController.init();
            mainWindow.setCenter(mapParent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the loading screen while loading a page in the background.
     * This ensures that the UI is responsive even when there are intensive tasks when changing pages.
     * @param loadPageMethod a {@code Runnable} type that contains the logic for loading a specific page.
     */
    private void showPageWithLoadingScreen(Runnable loadPageMethod) {
        Stage stage = (Stage) mainWindow.getScene().getWindow();

        //show loading screen on JAVAFX thread
        Platform.runLater(() -> {
            appEnvironment.setLoadingScreenOwner(stage);
            appEnvironment.showLoadingScreen();
        });

        //background thread
        Thread switchPageThread = new Thread(() -> {

            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Platform.runLater(() -> {
                //run and load the specified page
                loadPageMethod.run();
                appEnvironment.hideLoadingScreen();
            });
        });
        switchPageThread.start();
    }

    /**
     * OnAction method for the Home button
     */
    @FXML
    private void homePressed() {
        showPageWithLoadingScreen(this::loadHomePage);
        setAllButtonsGrey();
    }

    @FXML
    private void helpPressed() {
        showPageWithLoadingScreen(this::loadHelpPage);
        setAllButtonsGrey();
    }

    /**
     * OnAction method for the Search button
     */
    @FXML
    private void searchPressed() {
        showPageWithLoadingScreen(this::loadSearchPage);
        setAllButtonsGrey();
    }

    @FXML
    void mapPressed() {
        showPageWithLoadingScreen(this::loadMapPage);
        setAllButtonsGrey();
    }

    /**
     * OnAction method for the login button, initializes the login page
     */
    @FXML
    void loginPressed() {
        try {
            // load a new fxml file
            FXMLLoader newStageLoader = new FXMLLoader(getClass().getResource("/fxml/admin_login_popup.fxml"));
            AnchorPane root = newStageLoader.load();

            AdminLoginPopupController controller = newStageLoader.getController();
            controller.init(appEnvironment);
            Scene modalScene = new Scene(root);
            Stage modalStage = new Stage();
            modalStage.setScene(modalScene);
            modalStage.setWidth(600);
            modalStage.setHeight(420);
            modalStage.setResizable(false);
            modalStage.setTitle("Admin Login Popup");
            // If we want the modal to not block the other window we can change modality to Modality.NONE
            modalStage.initModality(Modality.WINDOW_MODAL);
            modalStage.initOwner(mainWindow.getScene().getWindow());
            // Show the modal and wait for it to be closed
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**TODO IMPLEMENT
     * Helper function to fade out all other buttons when clicking a button
     */
    private void setAllButtonsGrey() {
//        homeButton.setStyle("");
//        searchButton.setStyle("");
//        mapButton.setStyle("");
//        helpButton.setStyle("");
    }

    /**
     * The Setter method for the appEnvironment
     * @param appEnvironment wine environment
     */
    public void setWineEnvironment(AppEnvironment appEnvironment) {
        this.appEnvironment = appEnvironment;
    }

    /**
     * The Setter method for the stage
     * @param stage stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}