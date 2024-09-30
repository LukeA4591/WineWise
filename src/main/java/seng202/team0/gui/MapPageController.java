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


public class MapPageController {
    private static final Logger log = LogManager.getLogger(seng202.team0.gui.MapPageController.class);
    @FXML
    private WebView webView;
    private WebEngine webEngine;
    private JSObject javaScriptConnector;

    void init() {
        System.out.println("YAY");
        initMap();
    }

    private void initMap() {
        webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        webEngine.load(getClass().getClassLoader().getResource("html/maps.html").toExternalForm());
        webEngine.getLoadWorker().stateProperty().addListener(
                (ov, oldState, newState) -> {
                    if (newState == Worker.State.SUCCEEDED) {
                        JSObject window = (JSObject) webEngine.executeScript("window");
                        window.setMember("javaScriptBridge", this);
                        javaScriptConnector = (JSObject) webEngine.executeScript("jsConnector");
                        Platform.runLater(() -> {
                            if (javaScriptConnector != null) {
                                javaScriptConnector.call("initMap");
                            }
                        });
                    }
                });
    }

}
