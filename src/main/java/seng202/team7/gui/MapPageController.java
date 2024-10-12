package seng202.team7.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team7.business.WineryManager;
import seng202.team7.models.Winery;

import java.util.Comparator;
import java.util.List;


public class MapPageController {
    private static final Logger log = LogManager.getLogger(seng202.team7.gui.MapPageController.class);
    @FXML
    private WebView webView;
    @FXML
    private ListView<Winery> wineryList;
    @FXML
    private TextField searchWinery;
    private WebEngine webEngine;
    private JSObject javaScriptConnector;
    private WineryManager wineryManager;

    void init() {
        wineryManager = new WineryManager();
        initMap();
        setWineryList(wineryManager.getAll());
    }

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

    private void addWineryMarkers() {
        List<Winery> wineries = wineryManager.getAllWithValidLocation();
        for (Winery winery : wineries) {
            javaScriptConnector.call("addMarker", winery.getWineryName(), winery.getLatitude(), winery.getLongitude());
        }
        displayMarkers();
    }

    private void displayMarkers() {
        javaScriptConnector.call("displayMarkers");
    }

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
                        if (winery == selectedWinery) {
                            setStyle("-fx-background-color: #eccca2");
                            if (winery.getLatitude() != null && winery.getLongitude() != null) {
                                javaScriptConnector.call("zoomToLocation", winery.getLatitude(), winery.getLongitude(), 13);
                            }
                        } else if (winery.getLatitude() == null || winery.getLongitude() == null) {
                            setStyle("-fx-background-color: #ffb3b3");
                        } else {
                            setStyle("-fx-background-color: #a6f2ad");
                        }
                    }
                }
            };
            cell.hoverProperty().addListener((observer, wasHovered, isNowHovered) -> {
                if (isNowHovered && !cell.isEmpty()) {
                    cell.setStyle("-fx-background-color: #eccca2");
                } else {
                    cell.updateSelected(true);
                }
            });
            return cell;
        });
    }

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
