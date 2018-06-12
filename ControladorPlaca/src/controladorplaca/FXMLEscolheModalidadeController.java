/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladorplaca;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import main.Main;

/**
 * FXML Controller class
 *
 * @author Cristiano Künas
 * @author Daniel Buchholz
 * @author Douglas Hoffmann
 * @author Leandro Heck
 */
public class FXMLEscolheModalidadeController implements Initializable {

    @FXML
    private Button jBBasquete;

    @FXML
    private Button jBPadrao;

    @FXML
    private Button jBVolei;

    /**
     * Evento botão basquete
     * Envia comando da modalidade escolhida para o servidor
     * se retorno = escolhida
     * carrega view controlador placar basquete
     * @param event 
     */
    @FXML
    void iniciaBasquete(MouseEvent event){
        try {
            if (Main.mandaMSG("#ESCOLHE_MODALIDADE$BASQUETE").equals("ESCOLHIDA")) {
                Main.loadScene("/view/FXMLControladorPlacar.fxml");
            }
        } catch (IOException ex) {
            //IMPLEMENTAR LOG
        }
    }

    /**
     * Evento botão padrão
     * Envia comando da modalidade escolhida para o servidor
     * se retorno = escolhida
     * carrega view controlador placar padrão
     * @param event 
     */
    @FXML
    void iniciaPPadrao(MouseEvent event) {

    }

    /**
     * Evento botão basquete
     * Envia comando da modalidade escolhida para o servidor
     * se retorno = escolhida
     * carrega view controlador placar vôlei
     * @param event 
     */
    @FXML
    void iniciaPVolei(MouseEvent event) {

    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
