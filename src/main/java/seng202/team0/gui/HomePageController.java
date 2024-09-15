package seng202.team0.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import seng202.team0.models.Wine;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import seng202.team0.repository.WineDAO;
import javafx.scene.control.Label;

public class HomePageController {

    @FXML
    private Label desc1;

    @FXML
    private Label desc2;

    @FXML
    private Label desc3;

    @FXML
    private Label wine1;

    @FXML
    private Label wine2;

    @FXML
    private Label wine3;


    private Stage stage;

    private WineDAO wineDAO;
    /**
     * Init method for HomePageController
     * @param stage stage from NavBarController
     */
    public void init(Stage stage){
        this.stage = stage;
        wineDAO = new WineDAO();
        displayWines();
        displayDescriptions();
    }

    public void displayWines(){
        List<Wine> wines = wineDAO.getAll();
        wine1.setText(wines.get(0).getWineName());
        wine2.setText(wines.get(1).getWineName());
        wine3.setText(wines.get(2).getWineName());
    }

    public void displayDescriptions(){
        List<Wine> wines = wineDAO.getAll();
        desc1.setText(wines.get(0).getDescription());
        desc2.setText(wines.get(1).getDescription());
        desc3.setText(wines.get(2).getDescription());
    }

    @FXML
    void wine1Pressed() {
        List<Wine> wines = wineDAO.getAll();
        winePressed(wines.get(0));
    }

    @FXML
    void wine2Pressed() {
        List<Wine> wines = wineDAO.getAll();
        winePressed(wines.get(1));
    }

    @FXML
    void wine3Pressed() {
        List<Wine> wines = wineDAO.getAll();
        winePressed(wines.get(2));
    }


    void winePressed(Wine wine) {
        try {
            // load a new fxml file
            FXMLLoader newStageLoader = new FXMLLoader(getClass().getResource("/fxml/wine_popup.fxml"));
            BorderPane root = newStageLoader.load();

            winePopupController controller = newStageLoader.getController();
            controller.init(wine);
            Scene modalScene = new Scene(root);
            Stage modalStage = new Stage();
            modalStage.setScene(modalScene);
            modalStage.setWidth(600);
            modalStage.setHeight(400);
            modalStage.setResizable(false);
            modalStage.setTitle("Wine Popup");
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
