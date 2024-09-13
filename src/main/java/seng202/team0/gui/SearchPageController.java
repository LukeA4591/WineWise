package seng202.team0.gui;

import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import seng202.team0.models.Wine;
import seng202.team0.repository.DatabaseManager;
import seng202.team0.repository.WineDAO;

import java.util.*;

import static java.util.Collections.max;
import static java.util.Collections.min;


public class SearchPageController {

    @FXML
    private TableView<Wine> table;
    @FXML
    private MenuButton categoryMenuButton;
    @FXML
    private MenuButton regionMenuButton;
    @FXML
    private MenuButton yearMenuButton;
    @FXML
    private MenuButton wineryMenuButton;
    @FXML
    private TextField criticScoreMinText;
    @FXML
    private TextField criticScoreMaxText;
    @FXML
    private Label errorLabel;
    private List<Wine> wines;
    private Map<String, String> filters = new HashMap<>();
    private Map<String, List<String>> scoreFilters = new HashMap<>();
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
        List<String> regions = wineDAO.getDistinct("region");
        for (String region : regions) {
            if (!Objects.equals(region, "Not Applicable")) {
                MenuItem menu = new MenuItem();
                menu.setText(region);
                menu.setOnAction(this::regionFilterClicked);
                regionMenuButton.getItems().add(menu);
            }
        }

        List<String> wineries = wineDAO.getDistinct("winery");
        for (String winery : wineries) {
            MenuItem menu = new MenuItem();
            menu.setText(winery);
            menu.setOnAction(this::wineryFilterClicked);
            wineryMenuButton.getItems().add(menu);
        }

        List<Integer> vintages = wineDAO.getDistinctVintages();

        int i = min(vintages);
        while (i < max(vintages)) {
            MenuItem menu = new MenuItem();
            menu.setText(String.valueOf(i));
            menu.setOnAction(this::vintageFilterClicked);
            yearMenuButton.getItems().add(menu);
            i++;
        }

        scoreFilters.put("score", new ArrayList<>()); //will need to change when user scores come in

        filters.put("type", "ALL");
        filters.put("winery", "ALL");
        filters.put("vintage", "ALL");
        filters.put("region", "ALL");
    }

    @FXML
    private void filterClick(){
        if ((Objects.equals(criticScoreMinText.getText(), "") && !(Objects.equals(criticScoreMaxText.getText(), ""))) || (!(Objects.equals(criticScoreMinText.getText(), "")) && Objects.equals(criticScoreMaxText.getText(), ""))) {
            errorLabel.setText("Error");
        } else {
            scoreFilters.put("score", Arrays.asList(criticScoreMinText.getText(), criticScoreMaxText.getText()));
            wines = wineDAO.getFilteredWines(filters, scoreFilters);
            initTable(wines);
        }
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

    @FXML
    private void vintageFilterClicked(Event event) {
        MenuItem clickedItem = (MenuItem) event.getSource();
        yearMenuButton.setText("Year: " + clickedItem.getText());
        filters.put("vintage", clickedItem.getText());
    }

    @FXML
    private void wineryFilterClicked(Event event) {
        MenuItem clickedItem = (MenuItem) event.getSource();
        wineryMenuButton.setText("Winery: " + clickedItem.getText());
        filters.put("winery", clickedItem.getText());
    }

    @FXML
    private void resetClicked() {
        filters.replaceAll((k, v) -> "ALL");
        categoryMenuButton.setText("Category: ALL");
        regionMenuButton.setText("Region: ALL");
        yearMenuButton.setText("Year: ALL");
        wineryMenuButton.setText("Winery: ALL");
        wines = wineDAO.getAll();
        initTable(wines);
    }

    private void initTable(List<Wine> wines){
        table.setItems(null);

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

