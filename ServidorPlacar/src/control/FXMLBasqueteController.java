/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 *
 * FXML Controller class
 *
 * @author danie
 */
public class FXMLBasqueteController implements Initializable {

    @FXML
    private Label jLRodada1;

    @FXML
    private Label jLRodada2;

    @FXML
    private Label jLRodada3;

    @FXML
    private Label jLRodada4;

    @FXML
    private Label jLRodadaExtra;

    @FXML
    private Label jLCronometroCentral;

    @FXML
    private Label jLTimeEsquerdoPontos;

    @FXML
    private Label jLTimeEsquerdo;

    @FXML
    private Label jLTimeDireitoPontos;

    @FXML
    private Label jLTimeDireito;

    @FXML
    private Label jLTimeEsquerdoFaltas;

    @FXML
    private Label jLTimeDireitoFaltas;

    @FXML
    private Label jLSeguraBola;

    private Task iniciaCronos(Label l, int inicio, int tempo) {

        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                int i = inicio;
                while (true) {
                    final int finalI = i++;
                    Platform.runLater(() -> l.setText("" + finalI));
                    Thread.sleep(tempo);
                }
            }
        };
        return task;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
