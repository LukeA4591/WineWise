package seng202.team0.gui;

import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
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

    /**
     * Constructor for search page controller
     */
    public SearchPageController() {
        databaseManager = DatabaseManager.getInstance();
        wineDAO = new WineDAO();
    }

    /**
     * Initialize method for the fx elements of the page, sets up the filter menus with all the valid data add a
     * listener for all rows and provide more information if a wine is clicked
     * @author Oliver Barclay
     * @author Alex Wilson
     * @author Luke Armstrong
     */
    @FXML
    private void initialize() {
        wines = wineDAO.getAll();
        if (wines.size() > 0) {
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

            List<String> vintageStrings = wineDAO.getDistinct("vintage");
            List<Integer> vintages = new ArrayList<>();
            for (int i = 0; i < vintageStrings.size(); i++) {
                try {
                    vintages.add(Integer.parseInt(vintageStrings.get(i)));
                } catch (NumberFormatException ignored) {
                }
            }

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
        table.setRowFactory(tableview -> {
            TableRow<Wine> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1) {
                    Wine wineClicked = row.getItem();
                    onWineClicked(wineClicked);
                }
            });
            return row;
        });
    }

    /**
     * Called from the event listener when a wine is clicked to create wine popup
     * @param wine
     * @author Luke Armstrong
     */
    private void onWineClicked(Wine wine) {
        HomePageController wineMethods = new HomePageController();
        Image image = wineMethods.getImage(wine);
        wineMethods.winePressed(wine, image, errorLabel);
    }

    /**
     * On Action method for the filter button, initializes the table depending on the filters
     * @Author Alex Wilson
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
            scoreFilters.put("score", Arrays.asList(criticScoreMinText.getText(), criticScoreMaxText.getText()));
            wines = wineDAO.getFilteredWines(filters, scoreFilters);
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
     * @author Alex Wilson
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
     * @author Alex Wilson
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
     * @author Alex Wilson
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
     * @author Alex Wilson
     */
    @FXML
    private void wineryFilterClicked(Event event) {
        MenuItem clickedItem = (MenuItem) event.getSource();
        wineryMenuButton.setText("Winery: " + clickedItem.getText());
        filters.put("winery", clickedItem.getText());
    }

    /**
     * On Action method for the reset button, sets everything back to the original state
     * @author Alex Wilson
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
        wines = wineDAO.getAll();
        initTable(wines);
    }

    /**
     * Method to initialize the table from a list of wines
     * @param wines list of wines to load into the table
     * @author Oliver Barclay
     */
    private void initTable(List<Wine> wines){
        table.getColumns().clear();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

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

        //TableColumn<Wine, String> descCol = new TableColumn<>("Desc.");
        //descCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        table.getColumns().add(typeCol);
        table.getColumns().add(nameCol);
        table.getColumns().add(wineryCol);
        table.getColumns().add(vintageCol);
        table.getColumns().add(scoreCol);
        table.getColumns().add(regionCol);
        //table.getColumns().add(descCol);

        table.setItems(FXCollections.observableArrayList(wines));
    }



    private List<Wine> getData() {
        return null;
    }

}

