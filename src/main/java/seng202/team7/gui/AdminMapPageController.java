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
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team7.business.WineryManager;
import seng202.team7.models.Wine;
import seng202.team7.models.Winery;
import seng202.team7.services.AppEnvironment;
import seng202.team7.services.Geolocator;
import seng202.team7.services.JavaScriptBridge;
import seng202.team7.services.Position;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class AdminMapPageController {
    private AppEnvironment appEnvironment;
    private Geolocator geolocator;
    @FXML
    private WebView webView;
    @FXML
    private TextField addressText;
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

    void init(AppEnvironment appEnvironment, Stage stage) {
        this.appEnvironment = appEnvironment;
        wineryManager = new WineryManager();
        geolocator = new Geolocator();
        setWineryList();
        initMap();
        javaScriptBridge = new JavaScriptBridge(this::addWineryMarker, this::getWineryFromClick, stage);
    }

    @FXML
    void onBackButton() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

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
        setWineryList();
    }

    void setWineryList() {
//        List<Winery> wineries = wineryManager.getAllWithNullLocation("");
//        ObservableList<String> wineryNames = FXCollections.observableArrayList();
//        for (Winery winery : wineries) {
//            wineryNames.add(winery.getWineryName());
//        }
//        wineryList.setItems(wineryNames);
        List<Winery> wineries = wineryManager.getAll();
        wineries.sort(Comparator.comparing(Winery::getWineryName));
        ObservableList<Winery> wineryNames = FXCollections.observableArrayList(wineries);
        wineryList.setItems(wineryNames);
        wineryList.setCellFactory(cell -> new ListCell<>() {
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
                    }
                    else if (winery.getLatitude() == null || winery.getLongitude() == null) {
                        setStyle("-fx-background-color: #ffb3b3");
                    } else {
                        setStyle("-fx-background-color: #a6f2ad");
                    }
                }
            }
        });
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
        setWineryList();
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
            modalStage.setTitle("Remove Winery");
            modalStage.initModality(Modality.WINDOW_MODAL);
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/whiteIconBG(1).png")));
            modalStage.getIcons().add(icon);
            modalStage.initOwner(backButton.getScene().getWindow());
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setWineryList();
        return true;
    }

    @FXML
    private void searchPressed() {
        addressErrorLabel.setText("");
        String address = addressText.getText();
        Position coords = geolocator.queryAddress(address);
        if (coords.getLng() != -1000) {
            javaScriptBridge.setWineryFromClick(coords.toString());
            setWineryList();
        } else {
            addressErrorLabel.setText("Address not found.");
        }
    }
}
