/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
public class EscolheModalidadeController implements Initializable {

    @FXML
    private JFXButton jBLogout;

    @FXML
    private JFXButton jBBasquete;

    @FXML
    private JFXButton jBVolei;

    @FXML
    private JFXButton jBPadrao;

    /**
     * Evento botão basquete Envia comando da modalidade escolhida para o
     * servidor se retorno = escolhida carrega view controlador placar basquete
     *
     * @param event
     */
    @FXML
    void iniciaBasquete(MouseEvent event) {
        try {
            if (Main.mandaMSG("#ESCOLHE_MODALIDADE$BASQUETE").equals("ESCOLHIDA")) {
                Main.loadScene("/view/FXMLControlBasquete.fxml");
            }
        } catch (IOException ex) {
            Main.LOGGER.severe("Erro ao fazer a inicialização da modalidade 'Basquete'");
            System.out.println(ex.toString());
        }
    }

    /**
     * Evento botão padrão Envia comando da modalidade escolhida para o servidor
     * se retorno = escolhida carrega view controlador placar padrão
     *
     * @param event
     */
    @FXML
    void iniciaPadrao(MouseEvent event) {
        try {
            if (Main.mandaMSG("#ESCOLHE_MODALIDADE$PADRAO").equals("ESCOLHIDA")) {
                Main.loadScene("/view/FXMLControlPadrao.fxml");
            }
        } catch (IOException ex) {
            //IMPLEMENTAR LOG
        }
    }

    /**
     * Evento botão basquete Envia comando da modalidade escolhida para o
     * servidor se retorno = escolhida carrega view controlador placar vôlei
     *
     * @param event
     */
    @FXML
    void iniciaVolei(MouseEvent event) {
        try {
            if (Main.mandaMSG("#ESCOLHE_MODALIDADE$VOLEI").equals("ESCOLHIDA")) {
                Main.loadScene("/view/FXMLControlVolei.fxml");
            }
        } catch (IOException ex) {
            //IMPLEMENTAR LOG
        }
    }

    /**
     * Evento se o botão ESC for precionado. Manda mensagem de desconectar ao
     * servidor. Fecha a janela e retorna a tela de login.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void pressedKey(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ESCAPE) {
            Main.mandaMSG("#DESCONECTAR");
            Main.loadScene("/view/FXMLLogin.fxml");
        }
    }

    /**
     * Evento botão logout precionado. Manda mensagem de desconectar ao
     * servidor. Fecha a janela e retorna a tela de login.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void sairJanela(MouseEvent event) throws IOException {
        Main.mandaMSG("#DESCONECTAR");
        Main.loadScene("/view/FXMLLogin.fxml");
    }

    /**
     * Initializar.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
