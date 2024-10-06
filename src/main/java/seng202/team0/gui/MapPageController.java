package seng202.team0.gui;

import com.sun.javafx.webkit.WebConsoleListener;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team0.models.Wine;
import seng202.team0.models.Winery;
import seng202.team0.repository.WineryDAO;

import java.util.List;


public class MapPageController {
    private static final Logger log = LogManager.getLogger(seng202.team0.gui.MapPageController.class);
    @FXML
    private WebView webView;
    private WebEngine webEngine;
    private JSObject javaScriptConnector;
    private WineryDAO wineryDAO;

    void init() {
        initMap();
        wineryDAO = new WineryDAO();
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
        List<Winery> wineries = wineryDAO.getAllWithValidLocation();
        for (Winery winery : wineries) {
            javaScriptConnector.call("addMarker", winery.getWineryName(), winery.getLatitude(), winery.getLongitude());
        }
        displayMarkers();
    }

    private void displayMarkers() {
        javaScriptConnector.call("displayMarkers");
    }
}
