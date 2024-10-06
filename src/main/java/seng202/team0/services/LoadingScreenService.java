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

    public LoadingScreenService(Stage ownerStage) {
        this.loadingStage = new Stage();
        this.loadingStage.initOwner(ownerStage);
//        this.loadingStage.initModality(Modality.APPLICATION_MODAL); //block using application TODO might cause threading issues
        this.loadingStage.initStyle(StageStyle.UNDECORATED); //remove "x" button
        this.loadingStage.setResizable(false);
        //loadingStage.showAndWait();
    }
    public void showLoadingScreen() {
        try {
            FXMLLoader newStageLoader = new FXMLLoader(getClass().getResource("/fxml/loading_screen.fxml"));
            AnchorPane loadingScreen = newStageLoader.load();
            Scene loadingScene = new Scene(loadingScreen);
            loadingStage.setScene(loadingScene);
            loadingStage.show();
            System.out.println("showing loading screen");
        } catch (IOException e) {
            System.out.println("service error");
            e.printStackTrace();
        }
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
