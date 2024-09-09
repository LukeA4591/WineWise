package seng202.team0.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.apache.commons.lang3.ObjectUtils;
import seng202.team0.models.Rating;
import seng202.team0.models.Wine;
import seng202.team0.models.Winery;
import seng202.team0.repository.DatabaseManager;
import seng202.team0.repository.WineDAO;
import seng202.team0.services.WineEnvironment;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class SearchPageController {

    @FXML
    private TableView<Wine> table;

    static WineDAO wineDAO;
    static DatabaseManager databaseManager;

    public SearchPageController() {
        databaseManager = DatabaseManager.getInstance();
        wineDAO = new WineDAO();
        // TODO Init table on start instead of filter button click but error:
        // Cannot invoke "javafx.scene.control.TableView.getColumns()" because "this.table" is null
//        initTable();
    }

    @FXML
    public void initialize() {
        initTable();
    }

    @FXML
    void filterClick(){
        System.out.println("Filter Running...");
    }

    private void initTable(){
        table.getColumns().removeAll();

        System.out.println("Initing Table...");
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

        table.getColumns().add(typeCol);
        table.getColumns().add(nameCol);
        table.getColumns().add(wineryCol);
        table.getColumns().add(vintageCol);
        table.getColumns().add(scoreCol);
        table.getColumns().add(regionCol);
        table.getColumns().add(descCol);

        table.setItems(FXCollections.observableArrayList(wineDAO.getAll()));
    }



    private List<Wine> getData() {
        return null;
    }

}

