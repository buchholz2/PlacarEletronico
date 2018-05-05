/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladorplaca;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author danie
 */
public class ScenePrincipal extends Application {

    private static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("LoginCadastro.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Principal");
        stage.setScene(scene);
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
