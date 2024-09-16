package seng202.team0.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import seng202.team0.business.WineManager;
import seng202.team0.io.WineCSVImporter;
import seng202.team0.models.Wine;
import seng202.team0.services.AdminLoginService;
import seng202.team0.services.WineEnvironment;

import java.io.File;
import java.net.URISyntaxException;

public class AdminScreenController {
    @FXML
    Button addWine;
    @FXML
    private ComboBox<String> dataTypeComboBox;
    @FXML
    private Label errorLabel;

    private final WineEnvironment winery;
    private final WineManager wineManager;
    private Stage stage; // Make final and add void init(Stage stage)

    // Could make it an init function with the stage.
    public void initialize() {
        dataTypeComboBox.setItems(FXCollections.observableArrayList("Wine", "Winery"));
    }

    public AdminScreenController(WineEnvironment winery) {
        this.winery = winery;
        wineManager = new WineManager();
    }

    @FXML
    public void onAddWine(){
        String dataType = getDataType();
        if (dataType == "Wine") {
            winery.getClearRunnable().run();
            winery.launchAddWineScreen();
        } else if (dataType == "Winery") {
            winery.getClearRunnable().run();
            winery.launchAddWineryScreen();
        } else {
            errorLabel.setText("Please select data type");
            errorLabel.setStyle("-fx-text-fill: red");
        }
    }

    /**
     * Allows csv file to be chosen when the add dataset button is pressed.
     */
    @FXML
    private void addDataSet() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        try {
            fileChooser.setInitialDirectory(new File(MainController.class.getProtectionDomain().getCodeSource()
                    .getLocation().toURI()).getParentFile());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        stage = (Stage) addWine.getScene().getWindow(); // Need to have scene variable, has to find scene through addWine button
        File file = fileChooser.showOpenDialog(stage);
        wineManager.addAllWinesFromFile(new WineCSVImporter(), file);
    }

    /**
     * Chooses data type selected in combo box
     * @return The string of the data type
     */
    private String getDataType() {
        return dataTypeComboBox.getValue();
    }

    @FXML
    void adminLogout() {
        winery.getClearRunnable().run();
        winery.launchNavBar();
    }
}