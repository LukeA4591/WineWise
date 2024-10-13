package seng202.team7.gui;

import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import seng202.team7.business.WineManager;
import seng202.team7.models.Wine;
import seng202.team7.services.WinePopupService;

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
    private TextField searchText;
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
            regions.remove(""); //incase region is blank
            regions.sort(Comparator.comparingInt(s -> Character.toLowerCase(s.charAt(0))));
            for (String region : regions) {
                if (!Objects.equals(region, "Not Applicable")) {
                    MenuItem menu = new MenuItem();
                    menu.setText(region);
                    menu.setOnAction(this::regionFilterClicked);
                    regionMenuButton.getItems().add(menu);
                }
            }

            List<String> wineries = wineManager.getDistinct("winery");
            wineries.sort(Comparator.comparingInt(s -> Character.toLowerCase(s.charAt(0))));
            for (String winery : wineries) {
                MenuItem menu = new MenuItem();
                menu.setText(winery);
                menu.setOnAction(this::wineryFilterClicked);
                wineryMenuButton.getItems().add(menu);
            }

            List<String> vintages = wineManager.getDistinct("vintage");
            vintages.sort(Comparator.comparingInt(Integer::parseInt));
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

        TextFormatter<String> textFormatterMax = new TextFormatter<>(text -> {
            if (text.getControlNewText().length() > 3) {
                return null;
            }
            return text;
        });

        TextFormatter<String> textFormatterMin = new TextFormatter<>(text -> {
            if (text.getControlNewText().length() > 3) {
                return null;
            }
            return text;
        });

        criticScoreMaxText.setTextFormatter(textFormatterMax);
        criticScoreMinText.setTextFormatter(textFormatterMin);
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
        search();
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
        searchText.setText("");
        criticScoreMinText.setText("");
        criticScoreMaxText.setText("");
        wines = wineManager.getAll();
        initTable(wines);
    }

    /**
     * On Action method call for the search button
     */
    @FXML
    private void searchPressed() {
        search();
    }

    /**
     * Runs search along with the filters to get a result from a combination of the search box & the filters
     */
    private void search() {
        String search = searchText.getText();
        String criticScoreMax = criticScoreMaxText.getText();
        String criticScoreMin = criticScoreMinText.getText();
        if ((Objects.equals(criticScoreMin, "") && !(Objects.equals(criticScoreMax, ""))) || (!(Objects.equals(criticScoreMin, "")) && Objects.equals(criticScoreMax, ""))) {
            errorLabel.setText("Please input both scores");
        } else if (!validScore(criticScoreMax) || !validScore(criticScoreMin)) {
            errorLabel.setText("Please enter integers");
        } else if (!Objects.equals(criticScoreMax, "") && !Objects.equals(criticScoreMin, "") && (Integer.parseInt(criticScoreMax) < Integer.parseInt(criticScoreMin))) {
            errorLabel.setText("Please have from <= to");
        } else if ((!Objects.equals(criticScoreMax, "") && !Objects.equals(criticScoreMin, "")) && (Integer.parseInt(criticScoreMax) > 100 || Integer.parseInt(criticScoreMin) > 100)) {
            errorLabel.setText("Please have both scores <= 100");
        } else {
            errorLabel.setText("");
            scoreFilters.put("score", Arrays.asList(criticScoreMinText.getText(), criticScoreMaxText.getText()));
            wines = wineManager.getFilteredWines(filters, scoreFilters, search);
            initTable(wines);
            wines = wineManager.getAll();
        }
    }

    /**
     * Method to initialize the table from a list of wines
     * @param wines list of wines to load into the table
     */
    private void initTable(List<Wine> wines){
        table.getColumns().clear();

        TableColumn<Wine, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("color"));
        typeCol.setPrefWidth(50);
        typeCol.setMinWidth(50);
        typeCol.setMaxWidth(50);

        TableColumn<Wine, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("wineName"));
        nameCol.setPrefWidth(250);
        nameCol.setMinWidth(250);
        nameCol.setMaxWidth(250);

        TableColumn<Wine, String> wineryCol = new TableColumn<>("Winery");
        wineryCol.setCellValueFactory(new PropertyValueFactory<>("wineryString"));
        wineryCol.setPrefWidth(200);
        wineryCol.setMaxWidth(200);
        wineryCol.setMinWidth(200);

        TableColumn<Wine, Integer> vintageCol = new TableColumn<>("Vintage");
        vintageCol.setCellValueFactory(new PropertyValueFactory<>("vintage"));
        vintageCol.setPrefWidth(65);
        vintageCol.setMinWidth(65);
        vintageCol.setMaxWidth(65);


        TableColumn<Wine, Integer> scoreCol = new TableColumn<>("Score");
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
        scoreCol.setPrefWidth(50);
        scoreCol.setMinWidth(50);
        scoreCol.setMaxWidth(50);

        TableColumn<Wine, String> regionCol = new TableColumn<>("Region");
        regionCol.setCellValueFactory(new PropertyValueFactory<>("region"));
        regionCol.setPrefWidth(120);
        regionCol.setMinWidth(120);
        regionCol.setMaxWidth(120);

        table.getColumns().add(typeCol);
        table.getColumns().add(nameCol);
        table.getColumns().add(wineryCol);
        table.getColumns().add(vintageCol);
        table.getColumns().add(scoreCol);
        table.getColumns().add(regionCol);
        table.setItems(FXCollections.observableArrayList(wines));
    }

}

