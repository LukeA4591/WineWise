package seng202.team0.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team0.io.Importable;
import seng202.team0.io.WineCSVImporter;
import seng202.team0.models.Wine;

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
    Importable<Wine> importer;
    String[] headers;
    List<String[]> data;

    public void init(File file) {
        this.file = file;
        this.importer = new WineCSVImporter();
        this.data = new ArrayList<>();
        getStringFromFile(file);
        initTable();
        initComboBoxes();
    }

    //TODO put into service class
    private void getStringFromFile(File file) {
        List<String[]> lines = importer.readSixLinesFromFile(file);
        if (!lines.isEmpty()) {
            headers = lines.getFirst();
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
}
