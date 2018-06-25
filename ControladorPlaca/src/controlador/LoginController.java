/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.Main;

/**
 * FXML Controller class
 *
 * @author Cristiano Künas
 * @author Daniel Buchholz
 * @author Douglas Hoffmann
 * @author Leandro Heck
 */
public class LoginController implements Initializable {

    private Stage stage;

    @FXML
    private Pane jPLogin;

    @FXML
    private JFXButton jBEntrar;

    @FXML
    private JFXTextField jTFUsuario;

    @FXML
    private JFXButton jBSair;

    @FXML
    private JFXPasswordField jTFSenha;

    public static boolean chave = true;

    /**
     * Evento do botão Sair. Fecha stage
     *
     * @param event
     */
    @FXML
    void fecharTela(ActionEvent event) {
        try {
            if (chave != true) {
                Main.mandaMSG("#DESCONECTAR");
            }
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Main.getStage().close();
    }

    /**
     * Evento do botão Entrar Se campos de login ou senha estão vazios - alerta
     * usuário senão envia comando para o servidor. retorno = #LOGADO$ADM -
     * carrega view cadastro. retorno = #LOGADO$PLACAR - carrega view escolhe
     * modalidade. retorno = #LOGADO$PROPAGANDA - carrega view propaganda.
     *
     * @param event
     * @throws IOException
     * @throws Exception
     */
    @FXML
    void validaLogin(ActionEvent event) {
        chamaLogin();
    }

    @FXML
    void validaLoginK(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            chamaLogin();
        } else if (event.getCode() == KeyCode.ESCAPE) {
            try {
                if (chave != true) {
                    Main.mandaMSG("#DESCONECTAR");
                }
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
            Main.getStage().close();
        }
    }

    /**
     * Inicializar.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chave = true;
    }

    private void chamaLogin() {
        System.out.println("Tentou Conectar222");
        if (chave) {
            System.out.println("Tentou Conectar");
            Main.conectar();
            try {
                if (Main.getSocket().isConnected()) {
                    chave = false;
                }
            } catch (RuntimeException ex) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("ERRO SERVIDOR");
                alert.setHeaderText(null);
                alert.setContentText("SEM CONEXÃO AO SERVIDOR ");
                alert.show();
            }
        }

        try {
            if (Main.getSocket().isConnected() == true) {
                String login = jTFUsuario.getText();
                String senha = jTFSenha.getText();
                if (login.isEmpty() || senha.isEmpty()) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("ERRO DE LOGIN");
                    alert.setHeaderText(null);
                    alert.setContentText("CAMPO DE LOGIN OU SENHA VAZIOS!");
                    alert.show();
                } else {
                    try {
                        String[] msg = Main.mandaMSG("#LOGIN$" + login + "$" + senha).split("\\$");
                        if (msg[0].equals("#LOGADO")) {
                            if (msg[1].equals("ADM")) {
                                Main.loadScene("/view/FXMLCadastro.fxml");
                            } else if (msg[1].equals("PLACAR")) {
                                Main.loadScene("/view/FXMLEscolheModalidade.fxml");
                            } else if (msg[1].equals("PROPAGANDA")) {
                                Main.loadScene("/view/FXMLPropaganda.fxml");
                            }
                        } else if (msg[0].equals("#MAXIMO_USER")) {
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("SERVIDOR LOTADO");
                            alert.setHeaderText(null);
                            alert.setContentText("MAXIMO DE USUARIOS CONECTADOS!");
                            alert.show();
                            chave = true;
                        } else if (msg[0].equals("#NOT_DATA")) {
                            Alert alert = new Alert(AlertType.WARNING);
                            alert.setTitle("WARNING");
                            alert.setHeaderText(null);
                            alert.setContentText("BASE DE DADOS NÃO ENCONRADA!");
                            alert.show();
                        } else if (msg[0].equals("#JA_LOGOU")) {
                            Alert alert = new Alert(AlertType.WARNING);
                            alert.setTitle("WARNING");
                            alert.setHeaderText(null);
                            alert.setContentText("ESTE USUÁRIO JÁ ESTÁ LOGADO!");
                            alert.show();
                        } else {
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("ERRO DE LOGIN");
                            alert.setHeaderText(null);
                            alert.setContentText("LOGIN OU SENHA INVALIDA!");
                            alert.show();
                        }
                    } catch (IOException ex) {
                        //IMPLEMENTAR LOG
                    }

                }
            }

        } catch (RuntimeException ex) {

        }

    }
}
