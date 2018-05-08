/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scene;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author danie
 */
public class SceneBasquete extends Application {

    private static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/FXMLBasquete.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Basquete");
        stage.setScene(scene);
        stage.show();
        setStage(stage);
        stage.setFullScreen(true);
    }

    public Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        ScenePrincipal.stage = stage;
    }

}
