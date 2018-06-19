/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author danie
 */
public class FXMLPropagandaController implements Initializable {

    private String diretorio;

    @FXML
    private Label jLTitulo;

    @FXML
    private MediaView jMidiaView;

    private int i = 0;

    private ArrayList<String> lista;

    public FXMLPropagandaController() {
        this.diretorio = "E:\\Cursos\\Curso de Go (Golang)\\2. Fundamentos";
        this.lista = new ArrayList();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            buscaArquivos();
            initMediaPlayer(lista.iterator());
        } catch (InterruptedException ex) {
            Logger.getLogger(FXMLPropagandaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(FXMLPropagandaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public URL buscaArquivos() throws InterruptedException, MalformedURLException {
        File file = new File(diretorio);
        File afile[] = file.listFiles();
        URL url = new URL(afile[i].toURL().toString());
        for (int j = afile.length; i < j; i++) {
            File arquivos = afile[i];
            lista.add(arquivos.toURL().toString());
            System.out.println(arquivos.getName());
        }
        return url;
    }

    private void initMediaPlayer(final Iterator<String> urls) {
        if (urls.hasNext()) {
            MediaPlayer mediaPlayer = new MediaPlayer(new Media(urls.next()));
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    initMediaPlayer(mediaView, urls);
                }
            });
            mediaView.setMediaPlayer(mediaPlayer);
        }
        for (int j = afile.length; i < j; i++) {
            File arquivos = afile[i];
            System.out.println(arquivos.getName());
            Media m = new Media(arquivos.toURI().toString());

            MediaPlayer mp = new MediaPlayer(m);

            Duration currentTime = mp.getCurrentTime();
            jMidiaView.setMediaPlayer(mp);
            System.out.println();
            jMidiaView.setPreserveRatio(true);
            jMidiaView.autosize();
            currentTime.negate();
            mp.play();
            //chamaCronos(mp);

        }
        return "";
    }

    public void chamaCronos(MediaPlayer current) {
        Thread th = new Thread(iniciaCronos(current));
        th.setDaemon(true);
        th.start();
    }

    private Task iniciaCronos(MediaPlayer curr) {

        Task task = new Task<Void>() {

            @Override
            public Void call() throws Exception {

                while (true) {

                    System.out.println(curr.getCurrentTime().negate());

                }

            }
        };
        return task;
    }

}
