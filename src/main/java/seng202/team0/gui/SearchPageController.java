package seng202.team0.gui;

import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import seng202.team0.business.WineManager;
import seng202.team0.models.Wine;
import seng202.team0.services.WinePopupService;

import java.util.*;

/**
 * Controller for the search_screen.fxml file, displays all wine in a table and allows the user to filter
 */
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
    private WineManager wineManager;
    private WinePopupService wineMethods = new WinePopupService();

    /**
     * Constructor for search page controller
     */
    public SearchPageController() {
        wineManager = new WineManager();
    }

    /**
     * Initialize method for the fx elements of the page, sets up the filter menus with all the valid data add a
     * listener for all rows and provide more information if a wine is clicked
     */
    @FXML
    private void initialize() {
        wines = wineManager.getAll();
        if (!wines.isEmpty()) {
            initTable(wines);

            MenuItem allRegion = new MenuItem();
            allRegion.setOnAction(this::regionFilterClicked);
            allRegion.setText("ALL");
            regionMenuButton.getItems().add(allRegion);

            MenuItem allWinery = new MenuItem();
            allWinery.setOnAction(this::wineryFilterClicked);
            allWinery.setText("ALL");
            wineryMenuButton.getItems().add(allWinery);

            MenuItem allYear = new MenuItem();
            allYear.setOnAction(this::vintageFilterClicked);
            allYear.setText("ALL");
            yearMenuButton.getItems().add(allYear);

            List<String> regions = wineManager.getDistinct("region");
            for (String region : regions) {
                if (!Objects.equals(region, "Not Applicable")) {
                    MenuItem menu = new MenuItem();
                    menu.setText(region);
                    menu.setOnAction(this::regionFilterClicked);
                    regionMenuButton.getItems().add(menu);
                }
            }

            List<String> wineries = wineManager.getDistinct("winery");
            for (String winery : wineries) {
                MenuItem menu = new MenuItem();
                menu.setText(winery);
                menu.setOnAction(this::wineryFilterClicked);
                wineryMenuButton.getItems().add(menu);
            }

            List<String> vintages = wineManager.getDistinct("vintage");
            vintages.sort((s1, s2) -> Integer.compare(Integer.parseInt(s1), Integer.parseInt(s2)));
            for (String vintage : vintages) {
                MenuItem menu = new MenuItem();
                menu.setText(vintage);
                menu.setOnAction(this::vintageFilterClicked);
                yearMenuButton.getItems().add(menu);
            }

            scoreFilters.put("score", new ArrayList<>());

            filters.put("type", "ALL");
            filters.put("winery", "ALL");
            filters.put("vintage", "ALL");
            filters.put("region", "ALL");
        }
        table.setRowFactory(tableview -> {
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

    /**
     * Called from the event listener when a wine is clicked to create wine popup
     * @param wine wine clicked on
     */
    private void onWineClicked(Wine wine) {
        Image image = wineMethods.getImage(wine);
        wineMethods.winePressed(wine, image, errorLabel);
    }

    /**
     * On Action method for the filter button, initializes the table depending on the filters
     */
    @FXML
    private void filterClick(){
        if ((Objects.equals(criticScoreMinText.getText(), "") && !(Objects.equals(criticScoreMaxText.getText(), ""))) || (!(Objects.equals(criticScoreMinText.getText(), "")) && Objects.equals(criticScoreMaxText.getText(), ""))) {
            errorLabel.setText("Please input both scores");
            errorLabel.setStyle("-fx-text-fill: red;");
        } else if (!validScore(criticScoreMaxText.getText()) || !validScore(criticScoreMinText.getText())){
            errorLabel.setText("Please enter integers");
            errorLabel.setStyle("-fx-text-fill: red");
        } else if (!Objects.equals(criticScoreMaxText.getText(), "") && !Objects.equals(criticScoreMinText.getText(), "") && (Integer.parseInt(criticScoreMaxText.getText()) <= Integer.parseInt(criticScoreMinText.getText()))) {
            errorLabel.setText("Please have from <= to");
            errorLabel.setStyle("-fx-text-fill: red;");
        } else {
            errorLabel.setText("");
            scoreFilters.put("score", Arrays.asList(criticScoreMinText.getText(), criticScoreMaxText.getText()));
            wines = wineManager.getFilteredWines(filters, scoreFilters);
            initTable(wines);
        }
    }

    /**
     * Helper method to determine whether a string contains just numbers
     * @param testString string to be tested
     * @return true is only digits otherwise false
     */
    private boolean validScore(String testString) {
        boolean valid = true;
        for (int i = 0; i < testString.length(); i++) {
            if (!Character.isDigit(testString.charAt(i))) {
                valid = false;
            }
        }
        return valid;
    }

    /**
     * On Action method for the category filter
     * @param event MenuItem clicked
     */
    @FXML
    private void categoryFilterClicked(Event event) {
        MenuItem clickedItem = (MenuItem) event.getSource();
        categoryMenuButton.setText("Category: " + clickedItem.getText());
        filters.put("type", clickedItem.getText());
    }

    /**
     * On Action method for the region filter
     * @param event MenuItem clicked
     */
    @FXML
    private void regionFilterClicked(Event event) {
        MenuItem clickedItem = (MenuItem) event.getSource();
        regionMenuButton.setText("Region: " + clickedItem.getText());
        filters.put("region", clickedItem.getText());
    }

    /**
     * On Action method for the vintage filter
     * @param event MenuItem clicked
     */
    @FXML
    private void vintageFilterClicked(Event event) {
        MenuItem clickedItem = (MenuItem) event.getSource();
        yearMenuButton.setText("Year: " + clickedItem.getText());
        filters.put("vintage", clickedItem.getText());
    }

    /**
     * On Action method for the winery filter
     * @param event MenuItem clicked
     */
    @FXML
    private void wineryFilterClicked(Event event) {
        MenuItem clickedItem = (MenuItem) event.getSource();
        wineryMenuButton.setText("Winery: " + clickedItem.getText());
        filters.put("winery", clickedItem.getText());
    }

    /**
     * On Action method for the reset button, sets everything back to the original state
     */
    @FXML
    private void resetClicked() {
        filters.replaceAll((k, v) -> "ALL");
        categoryMenuButton.setText("Category: ALL");
        regionMenuButton.setText("Region: ALL");
        yearMenuButton.setText("Year: ALL");
        wineryMenuButton.setText("Winery: ALL");
        criticScoreMinText.setText("");
        criticScoreMaxText.setText("");
        wines = wineManager.getAll();
        initTable(wines);
    }

    /**
     * Method to initialize the table from a list of wines
     * @param wines list of wines to load into the table
     */
    private void initTable(List<Wine> wines){
        table.getColumns().clear();

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

        table.getColumns().add(typeCol);
        table.getColumns().add(nameCol);
        table.getColumns().add(wineryCol);
        table.getColumns().add(vintageCol);
        table.getColumns().add(scoreCol);
        table.getColumns().add(regionCol);

        table.setItems(FXCollections.observableArrayList(wines));
    }

}

