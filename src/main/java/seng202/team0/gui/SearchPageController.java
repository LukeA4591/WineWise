package seng202.team0.gui;

import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import seng202.team0.models.Wine;
import seng202.team0.repository.DatabaseManager;
import seng202.team0.repository.WineDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class SearchPageController {

    @FXML
    private TableView<Wine> table;
    @FXML
    private MenuButton categoryMenuButton;
    @FXML
    private MenuButton regionMenuButton;
    private List<Wine> wines;
    private List<String> regions;
    private Map<String, String> filters = new HashMap<>();
    static WineDAO wineDAO;
    static DatabaseManager databaseManager;

    public SearchPageController() {
        databaseManager = DatabaseManager.getInstance();
        wineDAO = new WineDAO();
    }

    @FXML
    private void initialize() {
        wines = wineDAO.getAll();
        initTable(wines);
        regions = wineDAO.getDistinctRegions();
        for (String region : regions) {
            if (!Objects.equals(region, "Not Applicable")) {
                MenuItem menu = new MenuItem();
                menu.setText(region);
                menu.setOnAction(this::regionFilterClicked);
                regionMenuButton.getItems().add(menu);
            }
        }

        filters.put("type", "ALL");
        filters.put("score", "ALL");
        filters.put("winery", "ALL");
        filters.put("vintage", "ALL");
        filters.put("region", "ALL");
    }

    @FXML
    private void filterClick(){
        wines = wineDAO.getFilteredWines(filters);
        initTable(wines);
    }

    @FXML
    private void categoryFilterClicked(Event event) {
        MenuItem clickedItem = (MenuItem) event.getSource();
        categoryMenuButton.setText("Category: " + clickedItem.getText());
        filters.put("type", clickedItem.getText());
    }

    @FXML
    private void regionFilterClicked(Event event) {
        MenuItem clickedItem = (MenuItem) event.getSource();
        regionMenuButton.setText("Region: " + clickedItem.getText());
        filters.put("region", clickedItem.getText());
    }

    private void initTable(List<Wine> wines){
        table.setItems(null);

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

        table.setItems(FXCollections.observableArrayList(wines));
    }

    private List<Wine> getData() {
        return null;
    }

}

