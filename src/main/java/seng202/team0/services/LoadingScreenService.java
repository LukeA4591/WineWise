package seng202.team0.services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class LoadingScreenService {

    private Stage loadingStage;

    public LoadingScreenService() {
        this.loadingStage = new Stage();
        this.loadingStage.initModality(Modality.APPLICATION_MODAL);
        this.loadingStage.initStyle(StageStyle.UNDECORATED);
        this.loadingStage.setResizable(false);
    }

    public void positionScreen(Stage parentStage) {
        this.loadingStage.setX(parentStage.getX());
        this.loadingStage.setY(parentStage.getY());
        this.loadingStage.setWidth(parentStage.getWidth());
        this.loadingStage.setHeight(parentStage.getHeight());
    }
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

    public void showLoadingScreen() {
        loadingStage.show();
    }

    public void hideLoadingScreen() {
        if (loadingStage.isShowing()) {
            loadingStage.close();
        }
    }
}
