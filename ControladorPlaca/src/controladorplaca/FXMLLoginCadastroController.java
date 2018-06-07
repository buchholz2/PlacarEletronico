/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladorplaca;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.Main;

/**
 * FXML Controller class
 *
 * @author daniel
 */
public class FXMLLoginCadastroController implements Initializable {

    private Stage stage;

    @FXML
    private Pane jPLogin;

    @FXML
    private PasswordField jTFSenha;

    @FXML
    private Button jBSair;

    @FXML
    private TextField jTFUsuario;

    @FXML
    private Button jBEntrar;

    @FXML
    void fecharTela(ActionEvent event) {
        Main.getStage().close();
    }

    @FXML
    void validaLogin(ActionEvent event) throws IOException, Exception {

        String login = jTFUsuario.getText();
        String senha = jTFSenha.getText();
        String[] msg = Main.mandaMSG("#LOGIN$" + login + "$" + senha).split("\\$");
        if (msg[0].equals("#LOGADO")) {
            if (msg[1].equals("ADM")) {

            } else if (msg[1].equals("PLACAR")) {
                Main.loadScene("/view/FXMLControladorPlacar.fxml");
            } else {

            }

        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ERRO DE LOGIN");
            alert.setHeaderText(null);
            alert.setContentText("LOGIN OU SENHA INVALIDA! OTRARIO");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
