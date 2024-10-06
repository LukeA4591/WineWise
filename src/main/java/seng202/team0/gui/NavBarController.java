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
    private Button homeButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button adminButton;
    @FXML
    private Button mapButton;

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
        homeButton.setStyle("-fx-background-color: indigo; -fx-text-fill: white");
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
            System.out.println("YAY");
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
     * OnAction method for the Home button
     */
    @FXML
    private void homePressed() {
        loadHomePage();
        setAllButtonsGrey();
        homeButton.setStyle("-fx-background-color: indigo; -fx-text-fill: white");
    }

    @FXML
    private void helpPressed() {
        loadHelpPage();
        setAllButtonsGrey();
        helpButton.setStyle("-fx-background-color: indigo; -fx-text-fill: white");
    }

    /**
     * OnAction method for the Search button
     */
    @FXML
    private void searchPressed() {
        loadSearchPage();
        setAllButtonsGrey();
        searchButton.setStyle("-fx-background-color: indigo; -fx-text-fill: white");
    }

    @FXML
    void mapPressed() {
        loadMapPage();
        setAllButtonsGrey();
        mapButton.setStyle("-fx-background-color: indigo; -fx-text-fill: white");
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
        mapButton.setStyle("");
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