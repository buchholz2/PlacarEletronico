/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scene;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author danie
 */
public class SceneDialog extends Application {

    private ScenePrincipal primaryStage;

    private static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {

        StackPane secondaryLayout = new StackPane();

        Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/view/FXMLDialog.fxml"));

        secondaryLayout.getChildren().add(newLoadedPane);

        Scene secondScene = new Scene(secondaryLayout);

        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("AGUARDANDO...");
        newWindow.setScene(secondScene);

        // Specifies the modality for new window.
        newWindow.initModality(Modality.WINDOW_MODAL);

        // Specifies the owner Window (parent) for new window
        newWindow.initOwner(primaryStage.getStage());

        // Set position of second window, related to primary window.
        primaryStage.getStage().centerOnScreen();

        newWindow.show();
    }

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        SceneDialog.stage = stage;
    }

}
