package seng202.team7.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team7.business.WineryManager;
import seng202.team7.models.Winery;
import seng202.team7.services.Geolocator;
import seng202.team7.services.JavaScriptBridge;
import seng202.team7.models.Position;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Controller class for the admin_map_page.fxml file
 * some map functionality is originally from  https://eng-git.canterbury.ac.nz/men63/seng202-advanced-fx-public
 */
public class AdminMapPageController {
    private Geolocator geolocator;
    @FXML
    private WebView webView;
    @FXML
    private TextField addressText;
    @FXML
    private TextField searchWinery;
    private WebEngine webEngine;
    private JSObject javaScriptConnector;
    private WineryManager wineryManager;
    private JavaScriptBridge javaScriptBridge;
    private static final Logger log = LogManager.getLogger(AdminMapPageController.class);
    @FXML
    private Button backButton;
    @FXML
    private ListView<Winery> wineryList;
    @FXML
    private Label addressErrorLabel;

    /**
     * Default constructor for the AdminMapPageController
     */
    public AdminMapPageController() {

    }

    /**
     * Initializes the AdminMapPageController with the given AppEnvironment and Stage.
     * Sets up the map, winery list, and JavaScript bridge to interact with the web map.
     *
     * @param stage The current stage.
     */
    void init(Stage stage) {
        wineryManager = new WineryManager();
        geolocator = new Geolocator();
        initMap();
        setWineryList(wineryManager.getAll());
        javaScriptBridge = new JavaScriptBridge(this::addWineryMarker, this::getWineryFromClick, stage);
    }

    /**
     * Handles the action when the back button is pressed.
     * Closes the current window.
     */
    @FXML
    void onBackButton() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Opens a new modal window to add a winery.
     * After adding the winery, updates the winery list to include the new entry.
     */
    @FXML
    public void onAddWinery() {
        try {
            FXMLLoader newStageLoader = new FXMLLoader(getClass().getResource("/fxml/add_winery.fxml"));
            AnchorPane root = newStageLoader.load();
            Scene modalScene = new Scene(root);
            Stage modalStage = new Stage();
            modalStage.setScene(modalScene);
            modalStage.setWidth(600);
            modalStage.setHeight(454);
            modalStage.setResizable(false);
            modalStage.setTitle("Add Wine");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            Stage primaryStage = (Stage) backButton.getScene().getWindow();
            modalStage.initOwner(primaryStage);
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setWineryList(wineryManager.getAll());
    }

    /**
     * Sets up a list of wineries in a sorted order, displays them with customized colors and click behavior,
     * and allows users to delete wineries or zoom to their location on a map.
     * @param wineries a list of winery objects.
     */
    void setWineryList(List<Winery> wineries) {
        wineries.sort(Comparator.comparing(Winery::getWineryName));
        ObservableList<Winery> wineryNames = FXCollections.observableArrayList(wineries);
        wineryList.setItems(wineryNames);
        setWineryListCellFactory();
        wineryList.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.DELETE || e.getCode() == KeyCode.BACK_SPACE) {
                try {
                    Winery selectedWinery = wineryList.getSelectionModel().getSelectedItem();
                    if (selectedWinery.getLongitude() != null && selectedWinery.getLatitude() != null) {
                        removeMarker(selectedWinery.getWineryName());
                    }
                    wineryList.getItems().remove(selectedWinery);
                    wineryManager.delete(selectedWinery.getWineryName());
                } catch (NullPointerException nullPointerException) {
                    log.warn("No wine selected for delete");
                    log.warn(nullPointerException.getMessage());
                }
            }
        });
    }

    /**
     * Helper method to set the winery list cell factory so that they zoom to the location when they are clicked
     */
    private void setWineryListCellFactory() {
        wineryList.setCellFactory(lv -> {
            ListCell<Winery> cell = new ListCell<>() {
                @Override
                protected void updateItem(Winery winery, boolean empty) {
                    super.updateItem(winery, empty);
                    if (empty || winery == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(winery.getWineryName());
                        Winery selectedWinery = wineryList.getSelectionModel().getSelectedItem();
                        this.setOnMouseClicked(event -> {
                            if (winery.getLatitude() != null && winery.getLongitude() != null) {
                                javaScriptConnector.call("zoomToLocation", winery.getLatitude(), winery.getLongitude(), 13);
                            }
                        });
                        if (winery == selectedWinery) {
                            setStyle("-fx-background-color: #eccca2");
                        } else if (winery.getLatitude() == null || winery.getLongitude() == null) {
                            setStyle("-fx-background-color: #ffb3b3");
                        } else {
                            setStyle("-fx-background-color: #a6f2ad");
                        }
                    }
                }
            };
            addHoverListenerToCell(cell);
            return cell;
        });
    }

    /**
     * Helper method to add the hover listener to each cell of the winery list
     * @param cell current cell of the winery list
     */
    private void addHoverListenerToCell(ListCell<Winery> cell) {
        cell.hoverProperty().addListener((observer, wasHovered, isNowHovered) -> {
            if (isNowHovered && !cell.isEmpty()) {
                cell.setStyle("-fx-background-color: #eccca2");
            } else {
                cell.updateSelected(true);
            }
        });
    }

    /**
     * Initializes the web map using WebView's WebEngine.
     * Sets up the JavaScript bridge to allow interaction between JavaFX and JavaScript.
     */
    void initMap() {
        webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        webEngine.load(getClass().getClassLoader().getResource("html/maps.html").toExternalForm());
        webEngine.getLoadWorker().stateProperty().addListener(
                (ov, oldState, newState) -> {
                    if (newState == Worker.State.SUCCEEDED) {
                        JSObject window = (JSObject) webEngine.executeScript("window");
                        window.setMember("javaScriptBridge", javaScriptBridge);
                        javaScriptConnector = (JSObject) webEngine.executeScript("jsConnector");
                        javaScriptConnector.call("initMap");
                        addWineryMarkers();
                    }
                });
    }

    /**
     * Adds markers for all wineries that have valid locations on the map.
     * Calls the addWineryMarker method for each winery.
     */
    private void addWineryMarkers() {
        List<Winery> wineries = wineryManager.getAllWithValidLocation();
        for (Winery winery : wineries) {
            addWineryMarker(winery);
        }
        displayMarkers();
    }

    /**
     * Adds a marker for the given winery on the map at the winery's latitude and longitude.
     *
     * @param winery The winery to add a marker for.
     */
    private void addWineryMarker(Winery winery) {
        javaScriptConnector.call("addMarker", winery.getWineryName(), winery.getLatitude(), winery.getLongitude());
        setWineryList(wineryManager.getAll());
    }

    /**
     * Removes the marker for the given winery from the map.
     *
     * @param wineryName The name of the winery whose marker should be removed.
     */
    private void removeMarker(String wineryName) {
        javaScriptConnector.call("removeMarker", wineryName);
    }

    /**
     * Displays all winery markers on the map.
     */
    private void displayMarkers() {
        javaScriptConnector.call("displayMarkers");
    }

    /**
     * Handles the selection of a winery from the map click event.
     * Opens a popup window to confirm the removal of the winery marker.
     *
     * @param wineryName The name of the winery selected from the map click.
     * @return true if the winery is successfully retrieved and handled, false otherwise.
     */
    public boolean getWineryFromClick(String wineryName) {
        try {
            FXMLLoader newStageLoader = new FXMLLoader(getClass().getResource("/fxml/remove_winery_location.fxml"));
            AnchorPane root = newStageLoader.load();
            RemoveWineryPopupController controller = newStageLoader.getController();
            controller.init(wineryName, () -> removeMarker(wineryName));
            Scene modalScene = new Scene(root);
            Stage modalStage = new Stage();
            modalStage.setScene(modalScene);
            modalStage.setResizable(false);
            modalStage.setTitle("Remove Winery");
            modalStage.initModality(Modality.WINDOW_MODAL);
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/whiteIconBG(1).png")));
            modalStage.getIcons().add(icon);
            modalStage.initOwner(backButton.getScene().getWindow());
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setWineryList(wineryManager.getAll());
        return true;
    }

    /**
     * Handles the search button press.
     * Queries the geolocator service to find the position of the entered address
     * and updates the map with the location if found.
     */
    @FXML
    private void searchPressed() {
        addressErrorLabel.setText("");
        String address = addressText.getText();
        Position coords = geolocator.queryAddress(address);
        if (coords.getLng() != -1000) {
            javaScriptBridge.setWineryFromClick(coords.toString());
            setWineryList(wineryManager.getAll());
        } else {
            addressErrorLabel.setText("Address not found.");
        }
    }

    /**
     * Updates the displayed list of wineries based on the search query entered.
     * If no query is entered, it resets the list to display all wineries.
     */
    @FXML
    void updateSearch() {
        String wineryName = searchWinery.getText();
        if (wineryName.isBlank()) {
            setWineryList(wineryManager.getAll());
        } else {
            setWineryList(wineryManager.getAllLikeSearch(wineryName));
        }
    }
}
