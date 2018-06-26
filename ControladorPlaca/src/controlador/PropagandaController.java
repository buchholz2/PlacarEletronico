/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import main.Main;
import model.ClientPropaganda;
import model.Propaganda;

/**
 *
 * @author Cristiano Künas
 * @author Daniel Buchholz
 * @author Douglas Hoffmann
 * @author Leandro Heck
 */
public class PropagandaController implements Initializable {

    @FXML
    private AnchorPane jAPPropaganda;

    @FXML
    private TableView<Propaganda> jTVListaPropaganda;

    @FXML
    private TableColumn<Propaganda, String> jTCTituloProp;

    @FXML
    private Button jBExcluirPropaganda;

    @FXML
    private Button jBProcuraPropaganda;

    @FXML
    private Button jBEnviaProp;

    @FXML
    private TextField jTFCaminhoPropaganda;

    @FXML
    private Button jBListaPropaganda;

    @FXML
    private Button jBLogout;

    @FXML
    private ProgressIndicator jPIEnviarArquivo;

    @FXML
    private Label jLEnviandoArquivo;

    private File file = null;

    ClientPropaganda c = new ClientPropaganda();

    ObservableList<Propaganda> propData = FXCollections.observableArrayList();

    @FXML
    void enviaPropaganda(MouseEvent event) {
        iniciaTransferencia();

    }

    @FXML
    void listaPropagandas(MouseEvent event) {
        chamaListarPropaganda();
    }

    public void chamaListarPropaganda() {
        try {
            propData.clear();
            String retorno = "";
            String[] arquivos;
            retorno = Main.mandaMSG("#LISTAR_PROPAGANDA");
            if (retorno.equals("#NOT$OK")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("LISTAGEM");
                alert.setHeaderText(null);
                alert.setContentText("NENHUM ARQUIVO ENCONTRADO!");
                alert.show();
            } else {
                arquivos = retorno.split("\\$");
                for (String url : arquivos) {
                    Propaganda prop = new Propaganda(url);
                    Platform.runLater(() -> {
                        propData.add(prop);
                    });
                }
                jTVListaPropaganda.setItems(propData);
            }
        } catch (IOException ex) {
            //IMPLEMENTAR LOGGER
        }
    }

    public void chamaListarPropagandaAtualizar() {
        try {
            propData.clear();
            String retorno = "";
            String[] arquivos;
            retorno = Main.mandaMSG("#LISTAR_PROPAGANDA");
            if (!retorno.equals("#NOT$OK")) {
                arquivos = retorno.split("\\$");
                for (String url : arquivos) {
                    Propaganda prop = new Propaganda(url);
                    Platform.runLater(() -> {
                        propData.add(prop);
                    });
                }
                jTVListaPropaganda.setItems(propData);
            }
        } catch (IOException ex) {
            //IMPLEMENTAR LOGGER
        }
    }

    @FXML
    void excluirPropaganda(MouseEvent event) {
        try {
            String retorno;
            Propaganda p = jTVListaPropaganda.getSelectionModel().getSelectedItem();
            String url = p.getUrl();
            retorno = Main.mandaMSG("#EXCLUIR_PROPAGANDA$" + url);
            if (retorno.equals("#OK")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("EXCLUSÃO");
                alert.setHeaderText(null);
                alert.setContentText("PROPAGANDA EXCLUÍDA COM SUCESSO!");
                alert.show();
            } else if (retorno.equals("#NOT$OK")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("EXCLUSÃO");
                alert.setHeaderText(null);
                alert.setContentText("ERRO AO EXCLUIR PROPAGANDA!");
                alert.show();
            }
        } catch (IOException ex) {
            Logger.getLogger(PropagandaController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            chamaListarPropagandaAtualizar();
        }
    }

    @FXML
    void procuraCaminho(MouseEvent event) throws IOException {
        file = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecione a Propaganda de Video!");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Arquivos de Video","*.mp4", "*.avi","*.mkv", "*.wmv", "*.mpeg");
        fileChooser.getExtensionFilters().add(filter);
        file = fileChooser.showOpenDialog(null);
        if (file != null) {
            Main.mandaMSG("#ENVIAR_PROPAGANDA$" + file.getName());
            jTFCaminhoPropaganda.setText(file.getPath());
        }
        jBEnviaProp.setDisable(false);
    }

    @FXML
    void sairJanela(MouseEvent event) {
        try {
            Main.mandaMSG("#DESCONECTAR");
            Main.loadScene("/view/FXMLLogin.fxml");
        } catch (IOException ex) {
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        jTCTituloProp.setCellValueFactory(new PropertyValueFactory<>("url"));
        jTVListaPropaganda.centerShapeProperty().set(true);
        jTFCaminhoPropaganda.setEditable(false);
        jBEnviaProp.setDisable(true);
    }

    public void iniciaTransferencia() {
        System.out.println("Esperando1");
        Thread th = new Thread(transferencia());
        th.setDaemon(true);
        th.start();
    }

    private Task transferencia() {
        System.out.println("Esperando1.2");
        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                jPIEnviarArquivo.setOpacity(1);
                jLEnviandoArquivo.setOpacity(1);
                jAPPropaganda.setDisable(true);
                c.enviaArquivo(file);
                jPIEnviarArquivo.setOpacity(0);
                jLEnviandoArquivo.setOpacity(0);
                jAPPropaganda.setDisable(false);
                jTFCaminhoPropaganda.setText("");
                chamaListarPropagandaAtualizar();
                return null;
            }
        };
        return task;
    }

}
