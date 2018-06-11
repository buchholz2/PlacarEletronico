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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import main.Main;

/**
 * FXML Controller class
 *
 * @author danie
 */
public class FXMLCadastroController implements Initializable {

    @FXML
    private Button jBLogout;

    @FXML
    private Button jBListaUsuarios;

    @FXML
    private Button jBExlcuir;

    @FXML
    private TextField jTFExcluirUsuario;

    @FXML
    private Label jLExcluirUsuario;

    @FXML
    private Label jLAdicionarUsuario;

    @FXML
    private TextField jTFAdicionarUsuario;

    @FXML
    private CheckBox jCBPropaganda;

    @FXML
    private CheckBox jCBPlacar;

    @FXML
    private CheckBox jCBAdministrador;

    @FXML
    private PasswordField jPFSenha;

    @FXML
    private PasswordField jPFRepitaSenha;

    @FXML
    private Label jLUsuarioAdd;

    @FXML
    private Label jLUsuarioEx;

    @FXML
    private Label jLSenha;

    @FXML
    private Label jLRepitaSenha;

    @FXML
    private Button jBAdicionar;

    @FXML
    void sairTelaCadastro(MouseEvent event) {
        Main.loadScene("/view/FXMLLoginCadastro.fxml");
    }

    @FXML
    void excluirUsuario(MouseEvent event) throws IOException {
        String retorno = "";
        if (!jTFExcluirUsuario.getText().isEmpty()) {
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("EXCLUIR");
//            alert.setHeaderText(null);
//            alert.setContentText("USUÁRIO EXCLUIDO COM SUCESSO!");
//            alert.show();
//
//        } else {
            retorno = Main.mandaMSG("#EXCLUIR_USUARIO$" + jTFExcluirUsuario.getText());
            if (retorno.equals("#OK")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("EXCLUIR");
                alert.setHeaderText(null);
                alert.setContentText("USUÁRIO EXCLUIDO COM SUCESSO!");
                alert.show();
                limpaCampos();
            } else if (retorno.equals("#NOT$OK")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("EXCLUIR");
                alert.setHeaderText(null);
                alert.setContentText("USUÁRIO NÃO ENCONTRADO!");
                alert.show();
            }
        }
    }

    @FXML
    void listarUsuarios(MouseEvent event) {

    }

    @FXML
    void adicionarUsuario(MouseEvent event) throws IOException {
        String retorno = "";
        String senha = jPFSenha.getText();
        String repsenha = jPFRepitaSenha.getText();
        String login = jTFAdicionarUsuario.getText();

        if (login.isEmpty() | senha.isEmpty() | repsenha.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("CADASTRO");
            alert.setHeaderText(null);
            alert.setContentText("VOCÊ DEIXOU ALGUM CAMPO VAZIO!");
            alert.show();
        } else {
            if (senha.equals(repsenha)) {
                if (jCBAdministrador.isSelected()) {
                    retorno = Main.mandaMSG("#ADICIONAR_USUARIO$ADMINISTRADOR$" + login + "$" + senha);
                } else if (jCBPlacar.isSelected()) {
                    retorno = Main.mandaMSG("#ADICIONAR_USUARIO$PLACAR$" + login + "$" + senha);
                } else if (jCBPropaganda.isSelected()) {
                    retorno = Main.mandaMSG("#ADICIONAR_USUARIO$PROPAGANDA$" + login + "$" + senha);
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("CADASTRO");
                    alert.setHeaderText(null);
                    alert.setContentText("POR FAVOR SELECIONE O TIPO DE USUÁRIO.");
                    alert.show();
                }
                if (retorno.equals("#OK")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("CADASTRO");
                    alert.setHeaderText(null);
                    alert.setContentText("USUÁRIO CADASTRADO COM SUCESSO!");
                    alert.show();
                    limpaCampos();
                } else if (retorno.equals("#USUARIO_EXISTENTE")) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("CADASTRO");
                    alert.setHeaderText(null);
                    alert.setContentText("NOME DE USUÁRIO JÁ CADASTRADO NO SISTEMA!");
                    alert.show();
                    limpaCampoSenha();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERRO");
                alert.setHeaderText(null);
                alert.setContentText("SENHAS NÃO CONFEREM!");
                alert.show();
//            limpaCampoSenha();
            }
        }
    }

    void limpaCampos() {
        jTFAdicionarUsuario.setText("");
        jTFExcluirUsuario.setText("");
        jPFSenha.setText("");
        jPFRepitaSenha.setText("");
        jCBPlacar.setSelected(false);
        jCBAdministrador.setSelected(false);
        jCBPropaganda.setSelected(false);
    }

    void limpaCampoSenha() {
        jPFSenha.setText("");
        jPFRepitaSenha.setText("");
    }

    @FXML
    void selecionadoAdministrador(MouseEvent event
    ) {
        jCBPlacar.setSelected(false);
        jCBPropaganda.setSelected(false);

    }

    @FXML
    void selecionadoPlacar(MouseEvent event
    ) {
        jCBAdministrador.setSelected(false);
        jCBPropaganda.setSelected(false);

    }

    @FXML
    void selecionadoPropaganda(MouseEvent event
    ) {
        jCBAdministrador.setSelected(false);
        jCBPlacar.setSelected(false);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb
    ) {
        // TODO
    }

}
