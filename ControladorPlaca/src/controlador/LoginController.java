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
import javafx.scene.control.TextInputDialog;
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

    private static int count = 0;
    private static String nome = "";
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
            Main.LOGGER.severe("Erro ao desconectar usando o método fecharTela");
            System.out.println(ex.toString());
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

    /**
     * Mesma função que o método valida login, quando precionar o tecla ENTER
     * chama o login. Se a tecla ESC for precionada fecha a stage
     *
     * @param event
     */
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
                Main.LOGGER.severe("Erro no processo de validação de Login");
                System.out.println(ex.toString());
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
            String msg = Main.conectar();
            if (msg.equals("#ERRO_IP")) {
                if (count < 3) {
                    TextInputDialog dialogoNome = new TextInputDialog();

                    dialogoNome.setTitle("ERRO DE IP DO SERVIDOR");
                    dialogoNome.setHeaderText("ENTRE COM A IP NOVA DO SERVIDOR");
                    dialogoNome.setContentText("IP:");
                    dialogoNome.showAndWait().ifPresent(v -> nome = v);
                    Main.setIPServidor(nome);
                } else {
                    Alert dialogoErro = new Alert(Alert.AlertType.ERROR);
                    dialogoErro.setTitle("ERRO DE IP");
                    dialogoErro.setHeaderText("ERRO AO CONECTAR AO SERVIDOR");
                    dialogoErro.setContentText("USUARIO MONGOL VEJA SE O SERVIDOR ESTÁ ATIVO!");
                    dialogoErro.showAndWait();
                }
            }
            try {
                if (Main.getSocket().isConnected()) {
                    chave = false;
                }
            } catch (RuntimeException ex) {
                Main.LOGGER.severe("Erro ao tentar fazer a conexão, usando o método chamaLogin");
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

                        } else if (msg[0].equals("#JA_LOGOU")) {

                            Alert alert = new Alert(AlertType.WARNING);
                            alert.setTitle("WARNING");
                            alert.setHeaderText(null);
                            alert.setContentText("ESTE USUÁRIO JÁ ESTÁ LOGADO!");
                            alert.show();
                        } else if (msg[0].equals("#JA_LOGOU_P")) {
                            Alert alert = new Alert(AlertType.WARNING);
                            alert.setTitle("WARNING");
                            alert.setHeaderText(null);
                            alert.setContentText("UM USUÁRIO PLACAR JÁ ESTÁ LOGADO! VOLTE MAIS TARDE!");
                            alert.show();
                        } else {
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("ERRO DE LOGIN");
                            alert.setHeaderText(null);
                            alert.setContentText("LOGIN OU SENHA INVALIDA!");
                            alert.show();
                        }
                    } catch (IOException ex) {
                        Main.LOGGER.severe("Erro ao chamar o método 'mandaMSG' dentro do chamaLogin");
                    }

                }
            }

        } catch (RuntimeException ex) {
            Main.LOGGER.warning("Erro de Runtime durante a execução do socket, dentro do método chamaLogin");
        }

    }
}
