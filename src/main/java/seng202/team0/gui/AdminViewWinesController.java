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
import seng202.team0.models.Wine;


import java.util.List;


public class AdminViewWinesController {
    @FXML
    private TableView<Wine> wineTable;
    private static final Logger log = LogManager.getLogger(AdminViewWinesController.class);
    private WineManager wineManager;
    private List<Wine> wines;


    @FXML
    private void initialize() {
        wineManager = new WineManager();
        wines = wineManager.getAll();
        initTables();
    }


    private void initTables() {
        wineTable.getColumns().clear();


        TableColumn<Wine, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("color"));


        TableColumn<Wine, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("wineName"));


        TableColumn<Wine, String> wineryCol = new TableColumn<>("Winery");
        wineryCol.setCellValueFactory(new PropertyValueFactory<>("wineryString"));


        TableColumn<Wine, Integer> vintageCol = new TableColumn<>("Vintage");
        vintageCol.setCellValueFactory(new PropertyValueFactory<>("vintage"));


        TableColumn<Wine, Integer> scoreCol = new TableColumn<>("Score");
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));


        TableColumn<Wine, String> regionCol = new TableColumn<>("Region");
        regionCol.setCellValueFactory(new PropertyValueFactory<>("region"));


        TableColumn<Wine, String> descCol = new TableColumn<>("Desc.");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));


        wineTable.getColumns().add(typeCol);
        wineTable.getColumns().add(nameCol);
        wineTable.getColumns().add(wineryCol);
        wineTable.getColumns().add(vintageCol);
        wineTable.getColumns().add(scoreCol);
        wineTable.getColumns().add(regionCol);
        wineTable.getColumns().add(descCol);


        wineTable.setItems(FXCollections.observableArrayList(wines));
        wineTable.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.DELETE || e.getCode() == KeyCode.BACK_SPACE) {
                try {
                    Wine selectedWine = wineTable.getSelectionModel().getSelectedItem();
                    wineTable.getItems().remove(selectedWine);
                    wineManager.delete(selectedWine.getWineName(), selectedWine.getWineryString(), selectedWine.getVintage());
                } catch (NullPointerException nullPointerException) {
                    log.warn("No sale selected for delete");
                    log.warn(nullPointerException.getMessage());
                }
            }
        });
    }
}
