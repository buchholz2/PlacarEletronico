/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import main.Main;

/**
 * FXML Controller class
 *
 * @author Cristiano Künas
 * @author Daniel Buchholz
 * @author Douglas Hoffmann
 * @author Leandro Heck
 */
 
public class PropagandaController implements Initializable {

    private String diretorio;

    @FXML
    private Label jLLocal;

    @FXML
    private Label jLPontosLocal;

    @FXML
    private Label jLVisitante;

    @FXML
    private Label jLPontosVisitante;

    @FXML
    private MediaView jMidiaView;

    private int i = 0;

    private ArrayList<String> lista;

    private MediaPlayer mediaPlayer;

    /**
     * Controladoo da classe
     */
    public PropagandaController() {
        this.diretorio = (Main.getPath() + "Midia");
        this.lista = new ArrayList();
    }

    public ArrayList<String> getLista() {
        return lista;
    }

    public String getDiretorio() {
        return diretorio;
    }

    public void setDiretorio(String diretorio) {
        this.diretorio = diretorio;
    }

    /**
     * Inicializar
     * 
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        iniciaPropaganda();
    }

    /**
     * Ação de buscar os arquivos no diretorio de midia
     * 
     * @return
     * @throws InterruptedException
     * @throws MalformedURLException 
     */
    public URL buscaArquivos() throws InterruptedException, MalformedURLException {
        File file = new File(diretorio);
        File afile[] = file.listFiles();
        URL url = new URL(afile[i].toURL().toString());
        if (afile.length != 0) {
            for (int j = afile.length; i < j; i++) {
                File arquivos = afile[i];
                String u = arquivos.toURI().toURL().toString();
                lista.add(u);
                System.out.println(arquivos.toURI().toURL().toString());
            }
        }
        return url;
    }

    private void initMediaPlayer(final Iterator<String> urls) {
        if (urls.hasNext()) {

            String endereco = urls.next().toString();
            Media m = new Media(endereco);
            mediaPlayer = new MediaPlayer(m);
            mediaPlayer.play();
            chamaCronos(mediaPlayer);
            mediaPlayer.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    initMediaPlayer(urls);
                }
            });
            Platform.runLater(() -> {
                jMidiaView.setMediaPlayer(mediaPlayer);
            });
        } else {
            Collections.shuffle(lista);
            initMediaPlayer(lista.iterator());
        }
    }

    public void chamaCronos(MediaPlayer current) {
        Thread th = new Thread(iniciaCronos(current));
        th.setDaemon(true);
        th.start();
    }

    private Task iniciaCronos(MediaPlayer curr) {

        Task task = new Task<Void>() {
            boolean chave = true;

            @Override
            public Void call() throws Exception {
                while (chave) {
                    if (Main.fechaPropaganda()) {
                        curr.stop();
                        chave = false;
                    }
                    Thread.sleep(100);
                }
                return null;

            }
        };
        return task;
    }

    public void iniciaPropaganda() {
        try {
            buscaArquivos();
            Collections.shuffle(lista);
            initMediaPlayer(lista.iterator());
            final DoubleProperty width = jMidiaView.fitWidthProperty();
            final DoubleProperty height = jMidiaView.fitHeightProperty();

            width.bind(Bindings.selectDouble(jMidiaView.sceneProperty(), "width"));
            height.bind(Bindings.selectDouble(jMidiaView.sceneProperty(), "height"));

            jMidiaView.setPreserveRatio(true);

            jLLocal.setText(Main.getNomeLocal());
            jLVisitante.setText(Main.getNomeVisitante());
            int pL = Main.getpLocal();
            if (pL > 9) {
                jLPontosLocal.setText("" + pL);
            } else {
                jLPontosLocal.setText("0" + pL);
            }
            int pV = Main.getpVisitante();
            if (pV > 9) {
                jLPontosVisitante.setText("" + pV);
            } else {
                jLPontosVisitante.setText("0" + pV);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(PropagandaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(PropagandaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
