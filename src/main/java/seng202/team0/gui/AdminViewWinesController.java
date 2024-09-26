package seng202.team0.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team0.business.WineManager;
import seng202.team0.models.Wine;

import java.io.IOException;
import java.util.List;

/**
 * Controller class for the admin_view_wines.fxml page.
 */
public class AdminViewWinesController {

    @FXML
    private TableView<Wine> wineTable;

    private static final Logger log = LogManager.getLogger(AdminViewWinesController.class);
    private WineManager wineManager;
    private List<Wine> wines;

    /**
     * Initializes the wineManager and then gets all the wines from the wine database table as wine objects. Using the
     * wine objects, a table is filled with the details of all the wines.
     */
    @FXML
    private void initialize() {
        wineManager = new WineManager();
        wines = wineManager.getAll();
        initTables();
    }

    /**
     * The table is initialized with the details of all the wines from the database. When a row is selected and either
     * the BACKSPACE or DELETE button is pressed, that wine will be deleted from the database by calling the
     * wineManager.
     */
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
                    log.warn("No wine selected for delete");
                    log.warn(nullPointerException.getMessage());
                }
            }
        });

        wineTable.setRowFactory(tableview -> {
            TableRow<Wine> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Wine wineClicked = row.getItem();
                    onWineClicked(wineClicked);
                }
            });
            return row;
        });
    }

    public void onWineClicked(Wine wineClicked) {
        try {
            FXMLLoader newStageLoader = new FXMLLoader(getClass().getResource("/fxml/edit_wine.fxml"));
            newStageLoader.setControllerFactory(param -> new EditWineController(wineClicked));
            AnchorPane root = newStageLoader.load();
            Scene modalScene = new Scene(root);
            Stage modalStage = new Stage();
            modalStage.setScene(modalScene);
            modalStage.setWidth(600);
            modalStage.setHeight(500);
            modalStage.setResizable(false);
            modalStage.setTitle("Edit Wine");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            Stage primaryStage = (Stage) wineTable.getScene().getWindow();
            modalStage.initOwner(primaryStage);
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
