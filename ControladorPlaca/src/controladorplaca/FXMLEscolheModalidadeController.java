/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladorplaca;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import main.Main;

/**
 * FXML Controller class
 *
 * @author danie
 */
public class FXMLEscolheModalidadeController implements Initializable {

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @FXML
    private Button jBBasquete;

    @FXML
    private Button jBPadrao;

    @FXML
    private Button jBVolei;

    @FXML
    void iniciaBasquete(MouseEvent event) throws IOException {
        if(Main.mandaMSG("#ESCOLHE_MODALIDADE$BASQUETE").equals("ESCOLHIDA")){
            Main.loadScene("/view/FXMLControladorPlacar.fxml");
        }
    }

    @FXML
    void iniciaPPadrao(MouseEvent event) {

    }

    @FXML
    void iniciaPVolei(MouseEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
