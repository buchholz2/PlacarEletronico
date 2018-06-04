/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import br.com.fandrauss.fx.gui.WindowControllerFx;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 *
 * FXML Controller class
 *
 * @author danie
 */
public class FXMLBasqueteController extends WindowControllerFx {

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

    @Override
    public String getFXML() {
        return "/view/FXMLBasquete.fxml";
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
