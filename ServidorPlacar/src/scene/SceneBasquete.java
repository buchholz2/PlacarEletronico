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
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author danie
 */
public class SceneBasquete extends Application {

    private Stage stage;
    private Scene scene;
private Parent root;
    @Override
    public void start(Stage stage) throws Exception {
        root = FXMLLoader.load(getClass().getResource("/view/FXMLBasquete.fxml"));
        scene = new Scene(root);
        Font.loadFont(this.getClass().getResource("/estilos/fontes/digi.ttf").toExternalForm(), 23.8);
        stage.setTitle("Basquete");
        stage.setScene(scene);
        stage.show();
        setStage(stage);
        stage.setFullScreen(true);
    }

    public Parent getRoot() {
        return root;
    }

    public void setRoot(Parent root) {
        this.root = root;
    }

    
    
    
    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

      
    public Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        ScenePrincipal.stage = stage;
    }

}
