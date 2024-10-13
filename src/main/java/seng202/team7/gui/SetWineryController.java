package seng202.team7.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import seng202.team7.business.WineryManager;
import seng202.team7.io.SetWineryInterface;
import seng202.team7.models.Winery;
import javafx.stage.Stage;

import java.util.List;

/**
 * Controller for the set_winery.fxml file
 */
public class SetWineryController {
    @FXML
    private ListView<String> wineryList;
    @FXML
    private Label selectedWineryLabel;
    @FXML
    private TextField searchText;
    String search;

    private WineryManager wineryManager;
    float lat;
    float lon;
    SetWineryInterface setWineryInterface;
    String selectedWinery = null;

    /**
     * Default constructor for the SetWineryController class
     */
    public SetWineryController() {

    }

    /**
     * initializes the page with the wineryInterface and the lat/long floats
     * @param setWineryInterface interface for the winery
     * @param lat lat coordinate as a float
     * @param lon lon cordinate as a float
     */
    public void init(SetWineryInterface setWineryInterface, float lat, float lon) {
        wineryManager = new WineryManager();
        List<Winery> wineries = wineryManager.getAllWithNullLocation("");
        this.setWineryInterface = setWineryInterface;
        this.lat = lat;
        this.lon = lon;
        displayWineries(wineries);
        wineryList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedWinery = newValue;
                selectedWineryLabel.setText(selectedWinery);
            }
        });

    }

    /**
     * displays the wineries from the observable array list by adding them to the wineryList
     * @param wineries
     */
    void displayWineries(List<Winery> wineries) {
        ObservableList<String> wineryNames = FXCollections.observableArrayList();
        for (Winery winery : wineries) {
            wineryNames.add(winery.getWineryName());
        }
        wineryList.setItems(wineryNames);
    }

    /**
     * On Action confirm button to add the selected winery to the map
     */
    @FXML
    void onConfirmButton() {
        if (selectedWinery == null) {
            selectedWineryLabel.setText("Please select a Winery to add");
        } else {
            wineryManager.updateLocationByWineryName(selectedWinery, lat, lon);
            setWineryInterface.operation(wineryManager.getWineryByName(selectedWinery));
            Stage stage = (Stage) selectedWineryLabel.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * On Action cancel the action and close the current pop-up
     */
    @FXML
    void cancelPressed() {
        Stage stage = (Stage) selectedWineryLabel.getScene().getWindow();
        stage.close();
    }

    /**
     * Updates the search to the new text in the search field
     */
    @FXML
    void updateSearch() {
        List<Winery> wineries;
        search = searchText.getText();
        wineries = wineryManager.getAllWithNullLocation(search);
        displayWineries(wineries);
    }
}
