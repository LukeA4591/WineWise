package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.apache.commons.lang3.ObjectUtils;
import seng202.team0.models.Rating;
import seng202.team0.models.Wine;
import seng202.team0.models.Winery;
import seng202.team0.services.WineEnvironment;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class SearchPageController implements Initializable {
    @FXML
    private GridPane grid;

    @FXML
    private ScrollPane scroll;

    private Stage stage;

    private int sizeOfRows = 2;
    private int sizeOfColumns = 4;

    private List<Wine> wines_displayed = new ArrayList<>();

    private List<Wine> getData() {
        List<Wine> wines_displayed = new ArrayList<>();
        Wine wine;

        for (int i = 0; i < 20; i++) {
            wine = new Wine("Clean Skin");
            wines_displayed.add(wine);
        }

        return wines_displayed;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        wines_displayed.addAll(getData());
        int column = 0;
        int row = 0;
        try {
            for (int i = 0; i < wines_displayed.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/fxml/wine_displayed.fxml"));

                AnchorPane anchorPane = fxmlLoader.load();


                DisplayedWineController displayedWineController = fxmlLoader.getController();
                displayedWineController.setData(wines_displayed.get(i));

                if (column == sizeOfColumns) {
                    column = 0;
                    row++;
                }

                grid.add(anchorPane, column++, row);
                GridPane.setMargin(anchorPane, new Insets(20));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void init(Stage stage){
        this.stage = stage;
    }
}

