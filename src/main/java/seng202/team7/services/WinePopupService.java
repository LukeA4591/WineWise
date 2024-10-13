package seng202.team7.services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import seng202.team7.gui.WinePopupController;
import seng202.team7.models.Wine;

import java.io.IOException;

/**
 * Service for the wine popup
 */
public class WinePopupService {

    /**
     * Default constructor for the WinePopupService
     */
    public WinePopupService() {

    }

    /**
     * Retrieves the appropriate image based on the wine's color.
     *
     * @param wine The wine whose image is to be fetched.
     * @return The image representing the wine.
     */
    public Image getImage(Wine wine) {
        String imagePath;
        switch (wine.getColor()) {
            case "Red":
                imagePath = "/images/redwine.png";
                break;
            case "White":
                imagePath = "/images/whitewine.png";
                break;
            case "Rosé":
                imagePath = "/images/rose.png";
                break;
            case "Rose":
                imagePath = "/images/rose.png";
                break;
            default:
                imagePath = "/images/defaultwine.png";
        }
        return new Image(getClass().getResourceAsStream(imagePath));
    }

    /**
     * Opens a detailed popup window displaying more information about the selected wine.
     *
     * @param wine  The wine whose details are to be displayed.
     * @param image The image associated with the wine.
     * @param label A label from the main scene to set the owner of the popup stage.
     */
    public void winePressed(Wine wine, Image image, Label label) {
        try {
            FXMLLoader newStageLoader = new FXMLLoader(getClass().getResource("/fxml/wine_popup.fxml"));
            BorderPane root = newStageLoader.load();

            WinePopupController controller = newStageLoader.getController();
            controller.init(wine, image);
            Scene modalScene = new Scene(root);
            Stage modalStage = new Stage();
            modalStage.setScene(modalScene);
            modalStage.setResizable(false);
            modalStage.setTitle("Wine Popup");
            modalStage.initModality(Modality.WINDOW_MODAL);
            modalStage.initOwner(label.getScene().getWindow());
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
