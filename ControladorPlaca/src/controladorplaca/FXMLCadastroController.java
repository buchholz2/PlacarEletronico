/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladorplaca;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import main.Main;
import model.ListaUsuarios;
import model.Usuario;

/**
 * FXML Controller class
 *
 * @author Cristiano Künas
 * @author Daniel Buchholz
 * @author Douglas Hoffmann
 * @author Leandro Heck
 */
public class FXMLCadastroController implements Initializable {

    @FXML
    private TableView<Usuario> jTVTabela;

    @FXML
    private TableColumn<Usuario, String> jTCNome;

    @FXML
    private TableColumn<Usuario, String> jTCFuncao;

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

    /**
     * Evento do botão Logout Recarrega view anterior.
     *
     * @param event
     */
    @FXML
    void sairTelaCadastro(MouseEvent event) {
        Main.loadScene("/view/FXMLLoginCadastro.fxml");
    }

    /**
     * Evento do botão excluir usuário. Verifica campo excluir usuario. Se não
     * estiver vazio, executa função de exclusão. retorno = #NOT$OK - usuário
     * não encontrado no lado servidor.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void excluirUsuario(MouseEvent event) {
        String retorno = "";
        try {
            if (jTFExcluirUsuario.getText().equals("adm")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("EXCLUIR");
                alert.setHeaderText(null);
                alert.setContentText("USUÁRIO PADRÃO IMPOSSIVEL EXCLUIR!");
                alert.show();
                limpaCampos();
            } else if (jTFExcluirUsuario.getText().equals("placar")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("EXCLUIR");
                alert.setHeaderText(null);
                alert.setContentText("USUÁRIO PADRÃO IMPOSSIVEL EXCLUIR!");
                alert.show();
                limpaCampos();
            } else if (jTFExcluirUsuario.getText().equals("propaganda")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("EXCLUIR");
                alert.setHeaderText(null);
                alert.setContentText("USUÁRIO PADRÃO IMPOSSIVEL EXCLUIR!");
                alert.show();
                limpaCampos();
            } else if (!jTFExcluirUsuario.getText().isEmpty()) {
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
        } catch (IOException ex) {
            //IMPLEMENTAR LOG
        }
    }

    /**
     * Evento do botão Lista Usuarios. Solicita lista de usuários do servidor,
     * organiza as informações e exibe na Table View.
     *
     * @param event
     */
    @FXML
    void listarUsuarios(MouseEvent event) {
        try {
            String retorno = "";
            String[] div;
            String opcao;
            ObservableList<Usuario> userData = FXCollections.observableArrayList();
            ListaUsuarios lista = new ListaUsuarios();

            retorno = Main.mandaMSG("#LISTAR_USUARIOS");

            if (retorno.equals("#NOT$OK")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("LISTAGEM");
                alert.setHeaderText(null);
                alert.setContentText("NADA ENCONTRADO, NENHUM USUÁRIO LISTADO!");
                alert.show();
            } else {
                div = retorno.split("\\?");

                for (int i = 0; i < div.length; i++) {
                    String[] div2 = div[i].split("\\$");
                    String nome = div2[0];
                    String funcao = div2[1];
                    Usuario user = new Usuario(nome, funcao);
                    userData.add(user);
                    System.out.println("Nome" + nome + " " + funcao);
                    System.out.println(userData.get(i).getFuncao() + " = " + userData.get(i).getUsuario());
                }

                jTVTabela.setItems(userData);

            }
        } catch (IOException ex) {
            //IMPLEMENTAR LOGGER
        }

    }

    /**
     * Evento do botão Adiciona Usuário. Verifica se algum campo ficou vazio.
     * Verifica o conteúdo do campo senha e repita senha se iguais executa ação;
     * ssenão exibe dialog; Verifica o retorno retorno = #usuario_existente não
     * adiciona. retorno = #ok adiociona;
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void adicionarUsuario(MouseEvent event) {
        String retorno = "";
        String senha = jPFSenha.getText();
        String repsenha = jPFRepitaSenha.getText();
        String login = jTFAdicionarUsuario.getText();
        try {
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
                }
            }
        } catch (IOException ex) {
            //IMPLEMENTAR LOG
        }
    }

    /**
     * Método para limpar campos da tela cadastro. Seta campos com String vazia
     * e CheckBox false.
     */
    void limpaCampos() {
        jTFAdicionarUsuario.setText("");
        jTFExcluirUsuario.setText("");
        jPFSenha.setText("");
        jPFRepitaSenha.setText("");
        jCBPlacar.setSelected(false);
        jCBAdministrador.setSelected(false);
        jCBPropaganda.setSelected(false);
    }

    /**
     * Método para limpar campos de senha somente.
     */
    void limpaCampoSenha() {
        jPFSenha.setText("");
        jPFRepitaSenha.setText("");
    }

    /**
     * Evento do CheckBox Ao ser selecionado remove a seleção dos outros
     * CheckBox.
     *
     * @param event
     */
    @FXML
    void selecionadoAdministrador(MouseEvent event
    ) {
        jCBPlacar.setSelected(false);
        jCBPropaganda.setSelected(false);
    }

    /**
     * Evento do CheckBox Ao ser selecionado remove a seleção dos outros
     * CheckBox.
     *
     * @param event
     */
    @FXML
    void selecionadoPlacar(MouseEvent event
    ) {
        jCBAdministrador.setSelected(false);
        jCBPropaganda.setSelected(false);
    }

    /**
     * Evento do CheckBox Ao ser selecionado remove a seleção dos outros
     * CheckBox.
     *
     * @param event
     */
    @FXML
    void selecionadoPropaganda(MouseEvent event
    ) {
        jCBAdministrador.setSelected(false);
        jCBPlacar.setSelected(false);

    }

    /**
     * Inicializar
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        jTCNome.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        jTCFuncao.setCellValueFactory(new PropertyValueFactory<>("funcao"));
        jTVTabela.centerShapeProperty().set(true);
    }

}
