package seng202.team0.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import seng202.team0.models.Winery;
import seng202.team0.repository.WineryDAO;
import seng202.team0.services.AppEnvironment;

import java.util.List;

public class AdminMapPageController {
    private AppEnvironment appEnvironment;
    @FXML
    private WebView webView;
    private WebEngine webEngine;
    private JSObject javaScriptConnector;
    private WineryDAO wineryDAO;
    @FXML
    private Button backButton;
    @FXML
    private ListView<String> wineryList;

    void init(AppEnvironment appEnvironment) {
        this.appEnvironment = appEnvironment;
        wineryDAO = new WineryDAO();
        setWineryList();
        initMap();
    }

    @FXML
    void onBackButton() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    void setWineryList() {
        List<Winery> wineries = wineryDAO.getAllWithNullLocation();
        ObservableList<String> wineryNames = FXCollections.observableArrayList();
        for (Winery winery : wineries) {
            wineryNames.add(winery.getWineryName());
        }
        wineryList.setItems(wineryNames);
    }

    void initMap() {
        webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        webEngine.load(getClass().getClassLoader().getResource("html/maps.html").toExternalForm());
        webEngine.getLoadWorker().stateProperty().addListener(
                (ov, oldState, newState) -> {
                    if (newState == Worker.State.SUCCEEDED) {
                        JSObject window = (JSObject) webEngine.executeScript("window");
                        window.setMember("javaScriptBridge", this);
                        javaScriptConnector = (JSObject) webEngine.executeScript("jsConnector");
                        javaScriptConnector.call("initMap");
                    }
                });
    }
}
