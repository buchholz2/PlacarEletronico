/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import main.Main;

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
        Main.mandaMSG("#ENVIAR_PROPAGANDA");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecione a planilha");
        //        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XLS", "*.xls"));
        file = fileChooser.showOpenDialog(null);
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
                // Checa se a transferencia foi completada com sucesso
                OutputStream socketOut = null;
                
                FileInputStream fileIn = null;

                try {
                    // Criando tamanho de leitura
                    byte[] cbuffer = new byte[1024];
                    int bytesRead;

                    // Criando arquivo que sera transferido pelo servidor
                    File f1 = new File(file.getPath());
                    fileIn = new FileInputStream(f1);
                    System.out.println("Lendo arquivo...");

                    // Criando canal de transferencia
                    socketOut = Main.getSocket().getOutputStream();

                    // Lendo arquivo criado e enviado para o canal de transferencia
                    System.out.println("Enviando Arquivo...");
                    while ((bytesRead = fileIn.read(cbuffer)) != -1) {
                        socketOut.write(cbuffer, 0, bytesRead);
                        socketOut.flush();
                    }

                    System.out.println("Arquivo Enviado!");
                } catch (Exception e) {
                    // Mostra erro no console
                    e.printStackTrace();
                } finally {
                    if (socketOut != null) {
                        try {
                            socketOut.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (fileIn != null) {
                        try {
                            fileIn.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

//                System.out.println("Esperando2");
//                FileInputStream in = new FileInputStream(file);
//                System.out.println("Esperando3");
//                OutputStream out = Main.getSocket().getOutputStream();
//                OutputStreamWriter osw = new OutputStreamWriter(out);
//                BufferedWriter writer = new BufferedWriter(osw);
//                writer.write(file.getName() + "\n");
//                writer.flush();
//                int tamanho = 4096;
//                byte[] buffer = new byte[tamanho];
//                int lidos = -1;
//                while ((lidos = in.read(buffer, 0, tamanho)) != -1) {
//                    out.write(buffer, 0, lidos);
//                }
//                System.out.println("Termino");
                return null;
            }
        };
        return task;
    }

}
