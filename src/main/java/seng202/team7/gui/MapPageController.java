package seng202.team7.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import seng202.team7.business.WineManager;
import seng202.team7.business.WineryManager;
import seng202.team7.models.Wine;
import seng202.team7.models.Winery;
import seng202.team7.services.WinePopupService;

import java.util.Comparator;
import java.util.List;

/**
 * Controller class for the map_page.fxml file
 */
public class MapPageController {
    @FXML
    private WebView webView;
    @FXML
    private ListView<Winery> wineryList;
    @FXML
    private ListView<Wine> wineListView;
    @FXML
    private TextField searchWinery;
    @FXML
    private Label wineriesLabel;
    private WebEngine webEngine;
    private JSObject javaScriptConnector;
    private WineryManager wineryManager;
    private WineManager wineManager;
    private WinePopupService winePopupService;

    /**
     * Default constructor for the MapPageController
     */
    public MapPageController() {

    }

    /**
     * Initializes the FX objects on the page
     */
    void init() {
        wineryManager = new WineryManager();
        wineManager = new WineManager();
        winePopupService = new WinePopupService();
        initMap();
        setWineryList(wineryManager.getAll());
    }

    /**
     * Initializes the map with the objects it needs to contain
     */
    private void initMap() {
        webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        webEngine.load(getClass().getClassLoader().getResource("html/maps.html").toExternalForm());
        webEngine.getLoadWorker().stateProperty().addListener(
                (ov, oldState, newState) -> {
                    if (newState == Worker.State.SUCCEEDED) {
                        javaScriptConnector = (JSObject) webEngine.executeScript("jsConnector");
                        javaScriptConnector.call("initMap");
                        addWineryMarkers();
                    }
                });
    }

    /**
     * gets all valid winery locations and places them on the map
     */

    private void addWineryMarkers() {
        List<Winery> wineries = wineryManager.getAllWithValidLocation();
        for (Winery winery : wineries) {
            javaScriptConnector.call("addMarker", winery.getWineryName(), winery.getLatitude(), winery.getLongitude());
        }
        displayMarkers();
    }

    /**
     * calls the javaScriptConnector displayMarkers function to show the markers
     */
    private void displayMarkers() {
        javaScriptConnector.call("displayMarkers");
    }

    /**
     * Adds a list of wineries to the wineryList for users to select from. Each row in the listView will be initialised
     * to change colour if they are selected/hovered over, plotted, or neither. When selected, it will also show the
     * wines that are produced by the winery and zoom into the location of the winery on the map.
     * @param wineries A list of wineries to be added to wineListView
     */
    void setWineryList(List<Winery> wineries) {
        wineries.sort(Comparator.comparing(Winery::getWineryName));
        ObservableList<Winery> wineryNames = FXCollections.observableArrayList(wineries);
        wineryList.setItems(wineryNames);
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
                            setWineList(winery);
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
     * Adds a list of wines from a winery to the wineListView for users to select from. When selected, it will open a
     * popup which shows the wine's information and also allows users to read, write, and flag reviews.
     * @param winery The winery where the wines are from
     */
    void setWineList(Winery winery) {
        ObservableList<Wine> wineList = FXCollections.observableArrayList(wineManager.getWineWithWinery(winery));
        wineListView.setItems(wineList);
        wineList.sort(Comparator.comparing(Wine::getWineName).thenComparing(Wine::getVintage));
        wineListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Wine wine, boolean empty) {
                super.updateItem(wine, empty);
                if (empty || winery == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(wine.getWineName() + " " + wine.getVintage());
                    this.setOnMouseClicked(event -> {
                        Image image = winePopupService.getImage(wine);
                        winePopupService.winePressed(wine, image, wineriesLabel);
                    });
                }
            }
        });
    }

    /**
     * Whenever something is written into searchWinery, this method will be called and will change the list of wineries
     * shown based off the input from the user.
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
