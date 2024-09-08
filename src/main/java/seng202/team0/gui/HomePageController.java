package seng202.team0.gui;

import seng202.team0.models.Wine;
import java.util.List;
import java.util.ArrayList;
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
        // displayWines();
        // displayDescriptions();
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

}
