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

    public void chamaCronos(int min, int seg, int mili) {
        Thread th = new Thread(iniciaCronos(jLCronometroCentral, min, seg, mili));
        th.setDaemon(true);
        th.start();
    }

    private Task iniciaCronos(Label l, int min, int seg, int mili) {

        Task task = new Task<Void>() {
            int m = min;
            int s = seg;
            int ms = mili;
            String minutos;
            String segundos;
            String milisegundos;

            @Override
            public Void call() throws Exception {
                while (true) {
                    
                    if (m > 9) {
                        minutos = "" + m;
                    } else {
                        minutos = "0" + m;
                    }
                    if (s > 9) {
                        segundos = "" + s;
                    } else {
                        segundos = "0" + s;
                    }
                    if (ms > 9) {
                        milisegundos = "" + ms;
                    } else {
                        milisegundos = "0" + ms;
                    }
                    
                    if (ms == 0){
                        ms = 99;
                        s = s--;
                    }
                    if (s == 0){
                        s = 59;
                        m = m--;
                    }
                    
                    ms = ms--;
                    
                    Platform.runLater(() -> l.setText(minutos + segundos + milisegundos));
                    Thread.sleep(10);
                }

            }
        };

        return task;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
