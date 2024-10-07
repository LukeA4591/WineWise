package seng202.team0.services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

public class LoadingScreenService {

    /**
     * The stage used to display the loading screen.
     */
    private Stage loadingStage;

    public LoadingScreenService() {
        this.loadingStage = new Stage();
        this.loadingStage.initModality(Modality.APPLICATION_MODAL);
        this.loadingStage.initStyle(StageStyle.UNDECORATED);
        this.loadingStage.setResizable(false);
    }

    /**
     * Adjusts the loading screen position and size to match the parent stage.
     * @param parentStage The stage that the loading screen should align with.
     */
    public void positionScreen(Stage parentStage) {
        this.loadingStage.setX(parentStage.getX());
        this.loadingStage.setY(parentStage.getY());
        this.loadingStage.setWidth(parentStage.getWidth());
        this.loadingStage.setHeight(parentStage.getHeight());
    }

    /**
     * Loads the loading screen layout from a fxml file.
     */
    public void loadLoadingScreen() {
        try {
            FXMLLoader newStageLoader = new FXMLLoader(getClass().getResource("/fxml/loading_screen.fxml"));
            StackPane loadingScreen = newStageLoader.load();
            Scene loadingScene = new Scene(loadingScreen);
            loadingStage.setScene(loadingScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the loading screen
     */
    public void showLoadingScreen() {
        loadingStage.show();
    }

    /**
     * Hides the loading screen if it's currently visible
     */
    public void hideLoadingScreen() {
        if (loadingStage.isShowing()) {
            loadingStage.close();
        }
    }
}
