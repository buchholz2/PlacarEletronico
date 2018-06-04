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

//        Main.loadScene("/view/FXMLControladorPlacar.fxml");
//        AnchorPane p = FXMLLoader.load(thisClass.getClass().getResource("/view/FXMLControladorPlacar.fxml"));
//        jPLogin.getScene().setRoot(p);
//        ;
        if (Main.mandaMSG("#LOGIN").equals("LOGADO")) {
            Main.loadScene("/view/FXMLControladorPlacar.fxml");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public String mandaMensagem(String msg) throws IOException {

        Socket cliente = new Socket("localhost", 12345);

        ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
        ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());

        saida.writeUTF(msg);
        saida.flush();

        String retorno = entrada.readUTF();
        System.out.println(retorno);

        return retorno;
    }
    
}
