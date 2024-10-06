package seng202.team0.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import seng202.team0.io.SetWineryInterface;
import seng202.team0.models.Winery;
import seng202.team0.repository.WineryDAO;
import javafx.stage.Stage;

import java.util.List;
import java.util.Set;

public class SetWineryController {
    @FXML
    private ListView<String> wineryList;
    @FXML
    private Button confirmButton;
    @FXML
    private Label selectedWineryLabel;
    private WineryDAO wineryDAO;
    float lat;
    float lon;
    SetWineryInterface setWineryInterface;
    String selectedWinery = null;

    public void init(SetWineryInterface setWineryInterface, float lat, float lon) {
        wineryDAO = new WineryDAO();
        this.setWineryInterface = setWineryInterface;
        this.lat = lat;
        this.lon = lon;
        displayWines();
        wineryList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedWinery = newValue;
                selectedWineryLabel.setText(selectedWinery);
            }
        });

    }

    void displayWines() {
        List<Winery> wineries = wineryDAO.getAllWithNullLocation();
        ObservableList<String> wineryNames = FXCollections.observableArrayList();
        for (Winery winery : wineries) {
            wineryNames.add(winery.getWineryName());
        }
        wineryList.setItems(wineryNames);
    }

    @FXML
    void onConfirmButton() {
        if (selectedWinery == null) {
            selectedWineryLabel.setText("Please select a Winery to add");
        } else {
            wineryDAO.updateLocationByWineryName(selectedWinery, lat, lon);
            Stage stage = (Stage) selectedWineryLabel.getScene().getWindow();
            stage.close();
        }
    }
}
