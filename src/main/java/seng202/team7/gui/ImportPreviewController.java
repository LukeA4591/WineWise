package seng202.team7.gui;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import seng202.team7.business.WineManager;
import seng202.team7.io.WineCSVImporter;
import seng202.team7.services.AppEnvironment;
import seng202.team7.services.DatasetUploadFeedbackService;
import seng202.team7.services.ImportPreviewService;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Controller class for the import_preview.fxml file
 */

public class ImportPreviewController {
    private DatasetUploadFeedbackService datasetUploadFeedbackService = new DatasetUploadFeedbackService();

    @FXML
    AnchorPane mainAnchor;
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
    @FXML
    Label tableErrorMessageLabel;
    @FXML
    Text importErrorMessage;

    List<ComboBox<String>> comboBoxList;

    File file;
    String[] headers;
    List<String[]> data;
    ImportPreviewService importPreviewService;
    WineManager wineManager;
    AppEnvironment appEnvironment;

    /**
     * Default constructor for the ImportPreviewController
     */
    public ImportPreviewController() {

    }

    /**
     * Init method for the ImportPreviewController, shows the first six lines of the file which the admin is changing th
     * @param file file which is being uploaded
     * @param appEnvironment instance of AppEnvironment
     */
    public void init(File file, AppEnvironment appEnvironment) {
        this.file = file;
        this.data = new ArrayList<>();
        this.importPreviewService = new ImportPreviewService();
        this.wineManager = new WineManager();
        this.appEnvironment = appEnvironment;
        headers = importPreviewService.getStringFromFile(file, data);
        initCSVTable();
        initComboBoxes();
    }

    /**
     * On Action that closes the current Pop-up/window
     */
    private void goBackToAdmin() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Initializes the CSV table by clearing everything and resetting the values then adding the data
     */
    private void initCSVTable() {
        dataTable.getColumns().clear();
        dataTable.getItems().clear();
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

    /**
     * Init function to set the preview table headers
     * @param headerIndexes indexes of the headers
     */
    private void initPreviewTable(List<Integer> headerIndexes) {
        String[] wineHeaders = new String[]{"Type", "Name", "Winery", "Vintage", "Score", "Region", "Description"};
        dataTable.getColumns().clear();
        dataTable.getItems().clear();
        for (int i = 0; i < headerIndexes.size(); i++) {
            int index = headerIndexes.get(i);
            TableColumn<String[], String> column = new TableColumn<>(wineHeaders[i]);
            column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[index]));
            dataTable.getColumns().add(column);
        }
        for (String[] line : data) {
            dataTable.getItems().add(line);
        }
    }

    /**
     * Initializes the option boxes for the options to import
     */
    private void initComboBoxes() {
        comboBoxList = Arrays.asList(typeComboBox, nameComboBox, wineryComboBox, vintageComboBox, scoreComboBox, regionComboBox, descriptionComboBox);
        for (ComboBox<String> comboBox : comboBoxList) {
           comboBox.getItems().addAll(headers);
        }
    }

    /**
     * Returns a list of the headers currently user in the combo boxes
     * @return List<String> of used headers
     */
    private List<String> getComboBoxHeaders() {
        String typeComboBoxHeader = typeComboBox.getValue();
        String nameComboBoxHeader = nameComboBox.getValue();
        String wineryComboBoxHeader = wineryComboBox.getValue();
        String vintageComboBoxHeader = vintageComboBox.getValue();
        String scoreComboBoxHeader = scoreComboBox.getValue();
        String regionComboBoxHeader = regionComboBox.getValue();
        String descriptionComboBoxHeader = descriptionComboBox.getValue();
        return Arrays.asList(typeComboBoxHeader, nameComboBoxHeader, wineryComboBoxHeader,
                vintageComboBoxHeader, scoreComboBoxHeader, regionComboBoxHeader,
                descriptionComboBoxHeader);
    }

    /**
     * On Action to call the function to return to the admin page
     */
    public void onExitPopup() {
        goBackToAdmin();
    }

    /**
     * Imports the given data to the correct headers on a background thread to not lock UI elements
     */
    public void onSaveDataset() {
        List<String> headerArray = getComboBoxHeaders();
        String headerMessage;
        Stage stage = (Stage) dataTable.getScene().getWindow();
        List<Integer> headerIndexes = importPreviewService.getHeaderIndexes(Arrays.asList(headers), headerArray);
        if ((headerMessage = importPreviewService.checkHeaders(headerArray, data, headerIndexes)).isEmpty()) {
            Platform.runLater(() -> {
                appEnvironment.setLoadingScreenOwner(stage);
                appEnvironment.showLoadingScreen();
            });

            //add batch on background thread.
            Thread addBatchThread = new Thread(() -> {

                wineManager.addBatch(new WineCSVImporter(), file, importPreviewService.getHeaderIndexes(Arrays.asList(headers), headerArray));

                Platform.runLater(() -> {
                    datasetUploadResponse();
                    appEnvironment.hideLoadingScreen();
                });
            });

            addBatchThread.start();

        } else {
            errorMessageLabel.setText(headerMessage);
        }
    }

    /**
     * Displays the result of the dataset upload.
     * Cleans pane with upload buttons and table.
     * Sets text to green if no errors.
     * Red if there are some errors.
     */
    private void datasetUploadResponse() {
        String uploadMessage = datasetUploadFeedbackService.getUploadMessage();
        importErrorMessage.setText(uploadMessage);
        mainAnchor.getChildren().clear();
        mainAnchor.getChildren().add(importErrorMessage);
        if (uploadMessage.equals("Wines uploaded")) {
            importErrorMessage.setFill(Color.GREEN);
        } else {
            importErrorMessage.setFill(Color.RED);
        }


    }

    /**
     * Using a button it toggles between CSV preview and import preview.
     * Includes error handling.
     */
    public void onChangeTable() {
        if (Objects.equals(changeTableButton.getText(), "Preview import")) {
            List<String> headerArray = getComboBoxHeaders();
            String headerMessage;
            List<Integer> headerIndexes = importPreviewService.getHeaderIndexes(Arrays.asList(headers), headerArray);
            if ((headerMessage = importPreviewService.checkHeaders(headerArray, data, headerIndexes)).isEmpty()) {
                initPreviewTable(headerIndexes);
                changeTableButton.setText("Preview CSV");
            }
            tableErrorMessageLabel.setText(headerMessage);
        } else {
            initCSVTable();
            tableErrorMessageLabel.setText("");
            changeTableButton.setText("Preview import");
        }
    }
}
