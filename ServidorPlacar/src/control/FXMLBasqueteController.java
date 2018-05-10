/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

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

    Server server1;

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
    private Button jButtonTeste;

    @FXML
    void iniciaThread(ActionEvent event) {
        new Thread(new Server(this)).start();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // TODO
    }

    public void mudaMensagem(String msg) {
        Timeline tlCronometro = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            jLCronometroCentral.setText("00:00:" + msg);
            jLTimeDireitoPontos.setText(msg);
            jLTimeEsquerdoFaltas.setText(msg);
            jLTimeDireitoFaltas.setText(msg);
            jLTimeEsquerdoFaltas.setText(msg);

        }),
                new KeyFrame(Duration.seconds(1))
        );
        tlCronometro.play();
    }

}
