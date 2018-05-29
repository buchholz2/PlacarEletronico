/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;
import rede.ComunicacaoSocketServidor;
import scene.SceneBasquete;

import scene.ScenePrincipal;

/**
 *
 * @author danie
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        SceneBasquete p = new SceneBasquete();
        p.start(stage);
      //  new Thread(new ComunicacaoSocketServidor(this)).start();
        new Thread(new ComunicacaoSocketServidor(p)).start();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
