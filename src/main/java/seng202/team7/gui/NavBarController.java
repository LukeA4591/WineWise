package seng202.team7.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.ImageView;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import seng202.team7.business.WineManager;
import seng202.team7.services.AppEnvironment;

import java.io.IOException;

/**
 * Controller class for the nav_bar.fxml file, launches a border pane to load other panes inside
 */
public class NavBarController {

    @FXML
    private BorderPane mainWindow;
    @FXML
    private ImageView homeLogo;
    @FXML
    private Pane logoSelectPane;
    private Stage stage;
    private AppEnvironment appEnvironment;
    private WineManager wineManager = new WineManager();

    /**
     * Integer between 0-3 to indicate which page user is on to avoid
     * loading same page user is on when UI button is clicked
     * 0 - Home, 1 - Search, 2 - Map, 3 - Help
     */
    private int currentPage = 0;

    /**
     * Used to calculate how many ms the loading screen should approximatly run for.
     */
    private static final int SLEEP_DIVIDER = 10;

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
        setLogoHover();
    }

    /**
     * Loads the home page into the border pane
     */
    private void loadHomePage() {
        try {
            FXMLLoader homePageLoader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"));
            Parent mainParent = homePageLoader.load();
            HomePageController homePageController = homePageLoader.getController();
            homePageController.init();
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

    private int sleepTimeMS() {
        return 500 + (wineManager.getTotalWinesInDB() / SLEEP_DIVIDER);
    }

    /**
     * Displays the loading screen while loading a page in the background.
     * This ensures that the UI is responsive even when there are intensive tasks when changing pages.
     * @param loadPageMethod a {@code Runnable} type that contains the logic for loading a specific page.
     */
    private void showPageWithLoadingScreen(Runnable loadPageMethod, boolean isSearchPage) {
        Stage stage = (Stage) mainWindow.getScene().getWindow();

        //show loading screen on JAVAFX thread
        Platform.runLater(() -> {
            appEnvironment.setLoadingScreenOwner(stage);
            appEnvironment.showLoadingScreen();
        });

        //background thread
        Thread switchPageThread = new Thread(() -> {

            try {
                if (isSearchPage) {
                    Thread.sleep(sleepTimeMS()); //extended time for searchpage, and large databases.
                } else {
                    Thread.sleep(500); //default time of 500ms for all screens and database size.
                }
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
        if (currentPage != 0) {
            currentPage = 0;
            showPageWithLoadingScreen(this::loadHomePage, false);
        }
    }

    /**
     * OnAction method that loads the help page as long as not already on said page
     */
    @FXML
    private void helpPressed() {
        if (currentPage != 3) {
            currentPage = 3;
            showPageWithLoadingScreen(this::loadHelpPage, false);
        }
    }

    /**
     * OnAction method for the Search button
     */
    @FXML
    private void searchPressed() {
        if (currentPage != 1) {
            currentPage = 1;
            showPageWithLoadingScreen(this::loadSearchPage, true);
        }
    }

    /**
     * OnAction method that loads the map page as long as not already on said page
     */
    @FXML
    void mapPressed() {
        if (currentPage != 2) {
            currentPage = 2;
            showPageWithLoadingScreen(this::loadMapPage, false);
        }
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
            modalStage.setTitle("WineWise Admin Login");
            // If we want the modal to not block the other window we can change modality to Modality.NONE
            modalStage.initModality(Modality.WINDOW_MODAL);
            modalStage.initOwner(mainWindow.getScene().getWindow());
            // Show the modal and wait for it to be closed
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    /**
     * adjusts the brightness of the logo when the user hovers over the icon
     */
    private void setLogoHover() {
        ColorAdjust colorAdjust = new ColorAdjust();

        logoSelectPane.setOnMouseEntered(event -> {colorAdjust.setBrightness(0.5);
            homeLogo.setEffect(colorAdjust);});
        logoSelectPane.setOnMouseExited(event -> {colorAdjust.setBrightness(0);
            homeLogo.setEffect(colorAdjust);});
    }
}