package seng202.team7.gui;

import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team7.business.WineryManager;
import seng202.team7.models.Winery;

import java.util.List;


public class MapPageController {
    private static final Logger log = LogManager.getLogger(seng202.team7.gui.MapPageController.class);
    @FXML
    private WebView webView;
    private WebEngine webEngine;
    private JSObject javaScriptConnector;
    private WineryManager wineryManager;

    void init() {
        initMap();
        wineryManager = new WineryManager();
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
}
