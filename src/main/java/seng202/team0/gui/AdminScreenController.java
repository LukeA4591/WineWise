package seng202.team0.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import seng202.team0.business.WineManager;
import seng202.team0.io.WineCSVImporter;
import seng202.team0.models.Wine;
import seng202.team0.services.AdminLoginService;
import seng202.team0.services.WineEnvironment;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class AdminScreenController {
    @FXML
    Button addWine;
    private final WineEnvironment winery;
    private final WineManager wineManager;
    private Stage stage;

    public AdminScreenController(WineEnvironment winery) {
        this.winery = winery;
        wineManager = new WineManager();
    }

    @FXML
    public void onAddWine() {
        try {
            FXMLLoader newStageLoader = new FXMLLoader(getClass().getResource("/fxml/add_wine.fxml"));
            AnchorPane root = newStageLoader.load();
            Scene modalScene = new Scene(root);
            Stage modalStage = new Stage();
            modalStage.setScene(modalScene);
            modalStage.setWidth(600);
            modalStage.setHeight(500);
            modalStage.setResizable(false);
            modalStage.setTitle("Wine Popup");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            Stage primaryStage = (Stage) addWine.getScene().getWindow();
            modalStage.initOwner(primaryStage);
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onViewWines() {
        try {
            FXMLLoader newStageLoader = new FXMLLoader(getClass().getResource("/fxml/admin_view_wines.fxml"));
            AnchorPane root = newStageLoader.load();
            Scene modalScene = new Scene(root);
            Stage modalStage = new Stage();
            modalStage.setScene(modalScene);
            modalStage.setWidth(900);
            modalStage.setHeight(624);
            modalStage.setResizable(false);
            modalStage.setTitle("View Wines");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            Stage primaryStage = (Stage) addWine.getScene().getWindow();
            modalStage.initOwner(primaryStage);
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Allows csv file to be chosen when the add dataset button is pressed.
     */
    @FXML
    private void addDataSet() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        try {
            fileChooser.setInitialDirectory(new File(MainController.class.getProtectionDomain().getCodeSource()
                    .getLocation().toURI()).getParentFile());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        stage = (Stage) addWine.getScene().getWindow(); // Need to have scene variable, has to find scene through addWine button
        File file = fileChooser.showOpenDialog(stage);
        wineManager.addAllWinesFromFile(new WineCSVImporter(), file);
    }

    /**
     * Chooses data type selected in combo box
     * @return The string of the data type
     */

    @FXML
    void adminLogout() {
        winery.getClearRunnable().run();
        winery.launchNavBar();
    }
}