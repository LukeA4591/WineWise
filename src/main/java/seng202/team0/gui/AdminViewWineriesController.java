package seng202.team0.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team0.business.WineManager;
import seng202.team0.business.WineryManager;
import seng202.team0.models.Wine;
import seng202.team0.models.Winery;

import java.util.List;

public class AdminViewWineriesController {

    @FXML
    private TableView<Winery> wineryTable;

    private static final Logger log = LogManager.getLogger(AdminViewWinesController.class);
    private WineryManager wineryManager;
    private List<Winery> wineries;

    @FXML
    private void initialize() {
        wineryManager = new WineryManager();
        wineries = wineryManager.getAll();
        initTables();
    }

    private void initTables() {
        wineries = wineryManager.getAll();
        wineryTable.getColumns().clear();

        TableColumn<Winery, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("wineryName"));

        TableColumn<Winery, Float> longitudeCol = new TableColumn<>("Longitude");
        longitudeCol.setCellValueFactory(new PropertyValueFactory<>("longitude"));

        TableColumn<Winery, Float> latitudeCol = new TableColumn<>("Latitude");
        latitudeCol.setCellValueFactory(new PropertyValueFactory<>("wineryString"));

        wineryTable.getColumns().add(nameCol);
        wineryTable.getColumns().add(longitudeCol);
        wineryTable.getColumns().add(latitudeCol);

        wineryTable.setItems(FXCollections.observableArrayList(wineries));
        wineryTable.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.DELETE || e.getCode() == KeyCode.BACK_SPACE) {
                try {
                    Winery selectedWinery = wineryTable.getSelectionModel().getSelectedItem();
                    wineryTable.getItems().remove(selectedWinery);
                    wineryManager.delete(selectedWinery.getWineryName());
                } catch (NullPointerException nullPointerException) {
                    log.warn("No wine selected for delete");
                    log.warn(nullPointerException.getMessage());
                }
            }
        });
    }
}
