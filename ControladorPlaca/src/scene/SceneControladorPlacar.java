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
import javafx.stage.StageStyle;

/**
 *
 * @author danie
 */
public class SceneControladorPlacar extends Application {

    static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/FXMLControladorPlacar.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Controlador Basquete");
        Font.loadFont(this.getClass().getResource("/estilos/fontes/DS-DIGIB.TTF").toExternalForm(), 23.8);
        Font.loadFont(this.getClass().getResource("/estilos/fontes/SoccerLeague.ttf").toExternalForm(), 23.8);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UTILITY);
        stage.setResizable(false);
        stage.show();

        setStage(stage);
    }

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        ScenePrincipal.stage = stage;
    }

}
