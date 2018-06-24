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
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import main.Main;
import model.ClientPropaganda;

public class PropagandaController implements Initializable {

    @FXML
    private TableView<?> jTVListaPropaganda;

    @FXML
    private TextField jTFEscolhePropExcluir;

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

    private File file = null;

    private int i;

    @FXML
    void enviaPropaganda(MouseEvent event) {
        iniciaTransferencia();
    }

    @FXML
    void listaPropagandas(MouseEvent event) {

    }

    @FXML
    void procuraCaminho(MouseEvent event) throws IOException {       
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecione a planilha");
        file = fileChooser.showOpenDialog(null);
         Main.mandaMSG("#ENVIAR_PROPAGANDA$"+ file.getName());
        jTFCaminhoPropaganda.setText(file.getPath());
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
        // TODO
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
                ClientPropaganda c = new ClientPropaganda();
                c.enviaArquivo(file);
                return null;
            }
        };
        return task;
    }

}
