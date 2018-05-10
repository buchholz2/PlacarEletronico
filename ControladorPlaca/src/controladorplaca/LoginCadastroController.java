/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladorplaca;

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
import scene.ScenePrincipal;

/**
 * FXML Controller class
 *
 * @author daniel
 */
public class LoginCadastroController implements Initializable {

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
        ScenePrincipal s = new ScenePrincipal();
        s.getStage().close(); //Fechando o Stage
    }
    
     @FXML
    void validaLogin(ActionEvent event) {
//       String usuario = jTFUsuario.getText();
//       String senha = jTFSenha.getText();
//       
//       if ((usuario))
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
