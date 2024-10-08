package seng202.team0.gui;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team0.business.WineManager;
import seng202.team0.io.Importable;
import seng202.team0.io.WineCSVImporter;
import seng202.team0.models.Wine;
import seng202.team0.services.AppEnvironment;
import seng202.team0.services.ImportPreviewService;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ImportPreviewController {
    private static final Logger log = LogManager.getLogger(seng202.team0.gui.ImportPreviewController.class);

    @FXML
    Button changeTableButton;
    @FXML
    Button exitButton;
    @FXML
    Button saveDatasetButton;
    @FXML
    TableView<String[]> dataTable;
    @FXML
    ComboBox<String> typeComboBox;
    @FXML
    ComboBox<String> nameComboBox;
    @FXML
    ComboBox<String> wineryComboBox;
    @FXML
    ComboBox<String> vintageComboBox;
    @FXML
    ComboBox<String> scoreComboBox;
    @FXML
    ComboBox<String> regionComboBox;
    @FXML
    ComboBox<String> descriptionComboBox;
    @FXML
    Label errorMessageLabel;

    ComboBox<String>[] comboBoxList;

    File file;
    Importable<Wine> csvImporter;
    String[] headers;
    List<String[]> data;
    ImportPreviewService importPreviewService;
    WineManager wineManager;
    AppEnvironment appEnvironment;

    public void init(File file, AppEnvironment appEnvironment) {
        this.file = file;
        this.csvImporter = new WineCSVImporter();
        this.data = new ArrayList<>();
        this.importPreviewService = new ImportPreviewService();
        this.wineManager = new WineManager();
        this.appEnvironment = appEnvironment;
        getStringFromFile(file);
        initTable();
        initComboBoxes();
    }

    private void goBackToAdmin() {
        Stage stage = (Stage) dataTable.getScene().getWindow();
        stage.close();
    }

    //TODO put into service class
    private void getStringFromFile(File file) {
        List<String[]> lines = csvImporter.readSixLinesFromFile(file);
        if (!lines.isEmpty()) {
            headers = importPreviewService.modifyHeaders(lines.getFirst());
        }
        for (int i = 1; i < lines.size(); i++) {
            data.add(lines.get(i));
        }
    }

    private void initTable() {
        dataTable.getColumns().clear();
        for (int i = 0; i < headers.length; i++) {
            final int index = i;
            TableColumn<String[], String> column = new TableColumn<>(headers[i]);
            column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[index]));
            dataTable.getColumns().add(column);
        }
        for (String[] line : data) {
            dataTable.getItems().add(line);
        }
    }

    private void initComboBoxes() {
        comboBoxList = (ComboBox<String>[]) new ComboBox<?>[] {typeComboBox, nameComboBox, wineryComboBox, vintageComboBox, scoreComboBox, regionComboBox, descriptionComboBox};
        for (ComboBox<String> comboBox : comboBoxList) {
           comboBox.getItems().addAll(headers);
        }
    }

    public void exitInputPreview() {
        goBackToAdmin();
    }

    public void saveDataset() {
        String typeComboBoxHeader = typeComboBox.getValue();
        String nameComboBoxHeader = nameComboBox.getValue();
        String wineryComboBoxHeader = wineryComboBox.getValue();
        String vintageComboBoxHeader = vintageComboBox.getValue();
        String scoreComboBoxHeader = scoreComboBox.getValue();
        String regionComboBoxHeader = regionComboBox.getValue();
        String descriptionComboBoxHeader = descriptionComboBox.getValue();
        String headerMessage;
        List<String> headerArray = Arrays.asList(typeComboBoxHeader, nameComboBoxHeader, wineryComboBoxHeader,
                vintageComboBoxHeader, scoreComboBoxHeader, regionComboBoxHeader,
                descriptionComboBoxHeader);
        Stage stage = (Stage) dataTable.getScene().getWindow();
        if ((headerMessage = importPreviewService.checkHeaders(headerArray)).isEmpty()) {
            Platform.runLater(() -> {
                appEnvironment.setLoadingScreenOwner(stage);
                appEnvironment.showLoadingScreen();
            });

            //add batch on background thread.
            Thread addBatchThread = new Thread(() -> {

                wineManager.addBatch(new WineCSVImporter(), file, importPreviewService.getHeaderIndexes(Arrays.asList(headers), headerArray));

                Platform.runLater(() -> appEnvironment.hideLoadingScreen());
            });

            addBatchThread.start();

            goBackToAdmin();

        } else {
            errorMessageLabel.setText(headerMessage);
        }
    }
}
