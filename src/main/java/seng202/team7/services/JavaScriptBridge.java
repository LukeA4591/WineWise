package seng202.team7.services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import seng202.team7.gui.SetWineryController;
import seng202.team7.io.SetWineryInterface;
import seng202.team7.io.GetWineryInterface;

import java.io.IOException;
import java.util.Objects;

/**
 * JavaScriptBridge class, used to communicate with JavaScript for the maps page
 * originally from  https://eng-git.canterbury.ac.nz/men63/seng202-advanced-fx-public"
 *
 */
public class JavaScriptBridge {
    private SetWineryInterface setWineryInterface;
    private GetWineryInterface getWineryInterface;
    private Stage parentStage;

    /**
     * Constructor for the JavaScriptBridge
     * @param setWineryInterface Interface for setting wineries
     * @param getWineryInterface Interface for getting wineries
     * @param parentStage Stage of the controller calling the bridge
     */
    public JavaScriptBridge(SetWineryInterface setWineryInterface, GetWineryInterface getWineryInterface, Stage parentStage) {
        this.setWineryInterface = setWineryInterface;
        this.getWineryInterface = getWineryInterface;
        this.parentStage = parentStage;
    }

    /**
     * Function called from js when map clicked and opens a modal to ask for sale information
     * @param latlng co-ordinates to add sale at JSON object format {"lat":number, "lng":number}
     */
    public void setWineryFromClick(String latlng){
        JSONParser parser = new JSONParser();
        try {
            JSONObject latlng_json = (JSONObject) parser.parse(latlng);
            float lat = ((Double) latlng_json.get("lat")).floatValue();
            float lng = ((Double) latlng_json.get("lng")).floatValue();
            FXMLLoader setWineryLoader = new FXMLLoader(getClass().getResource("/fxml/set_winery.fxml"));
            AnchorPane root = setWineryLoader.load();
            SetWineryController setWineryController = setWineryLoader.getController();
            setWineryController.init(setWineryInterface, lat, lng);
            Scene modalScene = new Scene(root);
            Stage modal = new Stage();
            modal.setScene(modalScene);
            modal.setWidth(220);
            modal.setHeight(620);
            modal.setResizable(false);
            modal.setTitle("Choose Winery");
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/whiteIconBG(1).png")));
            parentStage.getIcons().add(icon);
            modal.initOwner(parentStage);
            modal.initModality(Modality.WINDOW_MODAL);
            modal.showAndWait();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Used by JavaScript to get the winery from the name of the winery from the map
     * @param wineryName name of the winery
     * @return boolean of success rate
     */
    public boolean getWineryFromClick(String wineryName) {
        return getWineryInterface.operation(wineryName);
    }

}
