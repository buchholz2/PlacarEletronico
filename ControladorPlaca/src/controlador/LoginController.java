/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javafx.event.ActionEvent;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
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
    void validaLogin(ActionEvent event) throws IOException {
        chamaLogin();
    }

    /**
     * Mesma função que o método valida login, quando precionar o tecla ENTER
     * chama o login. Se a tecla ESC for precionada fecha a stage
     *
     * @param event
     */
    @FXML
    void validaLoginK(KeyEvent event) throws IOException {
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

    /**
     * Método utilizado para realizar a conexão com o servidor. Envia mensagem
     * ao servidor retorna resposta
     */
    private void chamaLogin() throws IOException {
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
                    escritor(nome);
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
                if (!msg.equals("#ERRO_IP")) {
                    Main.LOGGER.severe("Erro ao tentar fazer a conexão, usando o método chamaLogin");
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("ERRO SERVIDOR");
                    alert.setHeaderText(null);
                    alert.setContentText("SEM CONEXÃO AO SERVIDOR ");
                    alert.show();
                }
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

    public String leitor() throws IOException {
        String path = "/lib/ip.txt";
        BufferedReader buffRead = new BufferedReader(new FileReader(path));
        String linha = "";
        while (true) {
            if (linha != null) {
                System.out.println(linha);

            } else {
                break;
            }
            linha = buffRead.readLine();
        }
        buffRead.close();
        return linha;
    }

    public void escritor(String msg) throws IOException {
        File file = new File(System.getProperty("user.home") + "\\Documents\\Placar\\IP\\ip.txt");
        if (!file.exists()) {
            Path p = Paths.get(System.getProperty("user.home") + "\\Documents\\Placar\\IP");
            try {
                Files.createDirectories(p);
            } catch (IOException ex) {
            }
        }
        BufferedWriter buffWrite = new BufferedWriter(new FileWriter(file.toString()));
        buffWrite.append(msg);
        buffWrite.close();
    }
}
