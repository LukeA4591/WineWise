package seng202.team0.services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class LoadingScreenService {

    private Stage loadingStage;

    public LoadingScreenService() {
        this.loadingStage = new Stage();
        this.loadingStage.initModality(Modality.APPLICATION_MODAL); //block using application TODO might cause threading issues
        this.loadingStage.initStyle(StageStyle.UNDECORATED); //remove "x" button
        this.loadingStage.setResizable(false);
    }

    public void positionScreen(Stage parentStage) {
        this.loadingStage.setX(parentStage.getX());
        this.loadingStage.setY(parentStage.getY());
    }
    public void loadLoadingScreen() {
        try {
            FXMLLoader newStageLoader = new FXMLLoader(getClass().getResource("/fxml/loading_screen.fxml"));
            AnchorPane loadingScreen = newStageLoader.load();
            Scene loadingScene = new Scene(loadingScreen);
            loadingStage.setScene(loadingScene);
            System.out.println("showing loading screen");
        } catch (IOException e) {
            System.out.println("service error");
            e.printStackTrace();
        }
    }

    public void showLoadingScreen() {
        loadingStage.show();
    }

    public void hideLoadingScreen() {
        if (loadingStage.isShowing()) {
            loadingStage.close();
            System.out.println("loading screen hidden");
        } else {
            System.out.println("loading screen never showing");
        }
    }
}
