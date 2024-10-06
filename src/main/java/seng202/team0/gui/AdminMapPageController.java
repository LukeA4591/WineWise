package seng202.team0.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import seng202.team0.business.WineryManager;
import seng202.team0.models.Winery;
import seng202.team0.repository.WineryDAO;
import seng202.team0.services.AppEnvironment;
import seng202.team0.services.JavaScriptBridge;

import java.io.IOException;
import java.util.List;

public class AdminMapPageController {
    private AppEnvironment appEnvironment;
    @FXML
    private WebView webView;
    private WebEngine webEngine;
    private JSObject javaScriptConnector;
    private WineryManager wineryManager;
    private JavaScriptBridge javaScriptBridge;
    @FXML
    private Button backButton;
    @FXML
    private ListView<String> wineryList;

    void init(AppEnvironment appEnvironment, Stage stage) {
        this.appEnvironment = appEnvironment;
        wineryManager = new WineryManager();
        setWineryList();
        initMap();
        javaScriptBridge = new JavaScriptBridge(this::addWineryMarker, this::getWineryFromClick, stage);
    }

    @FXML
    void onBackButton() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    void setWineryList() {
        List<Winery> wineries = wineryManager.getAllWithNullLocation();
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
                        window.setMember("javaScriptBridge", javaScriptBridge);
                        javaScriptConnector = (JSObject) webEngine.executeScript("jsConnector");
                        javaScriptConnector.call("initMap");
                        addWineryMarkers();
                    }
                });
    }

    private void addWineryMarkers() {
        List<Winery> wineries = wineryManager.getAllWithValidLocation();
        for (Winery winery : wineries) {
            addWineryMarker(winery);
        }
        displayMarkers();
    }

    private void addWineryMarker(Winery winery) {
        javaScriptConnector.call("addMarker", winery.getWineryName(), winery.getLatitude(), winery.getLongitude());

    }

    private void removeMarker(String wineryName) {
        javaScriptConnector.call("removeMarker", wineryName);
    }

    private void displayMarkers() {
        javaScriptConnector.call("displayMarkers");
    }

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
            modalStage.setTitle("Remove Winery Popup");
            modalStage.initModality(Modality.WINDOW_MODAL);
            modalStage.initOwner(backButton.getScene().getWindow());
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
