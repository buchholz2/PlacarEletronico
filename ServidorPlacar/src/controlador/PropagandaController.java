/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
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
import static main.Main.LOGGER;

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

    @FXML
    private Label jLVS;

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
        try {
            iniciaPropaganda();
        } catch (IOException ex) {
            Logger.getLogger(PropagandaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(PropagandaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Ação de buscar os arquivos no diretorio de midia
     *
     * @return
     * @throws InterruptedException
     * @throws MalformedURLException
     */
    public URL buscaArquivos(){
        try {
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
        } catch (MalformedURLException ex) {
            try {
                gravaMidiaPadrao();
            } catch (IOException ex1) {
                Logger.getLogger(PropagandaController.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return null;
    }

    /**
     * Método para iniciar as propagandas. Onde são passadas para uma lista, as
     * propagadas são sorteadas, para não ocorrer de iniciar sempre a mesma
     * propaganda
     *
     * @param urls
     */
    private void initMediaPlayer(final Iterator<String> urls) {
        if (urls.hasNext()) {

            String endereco = urls.next().toString();
            Media m = new Media(endereco);
            mediaPlayer = new MediaPlayer(m);
            mediaPlayer.play();
            chamaPausa(mediaPlayer);
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

    /**
     * Método para iniciar a Thread, para pausar as propagandas
     *
     * @param current
     */
    public void chamaPausa(MediaPlayer current) {
        Thread th = new Thread(iniciaPausa(current));
        th.setDaemon(true);
        th.start();
    }

    /**
     * Método para inicia a pausa da propaganda
     *
     * @param curr
     * @return
     */
    private Task iniciaPausa(MediaPlayer curr) {

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

    /**
     * Método responsável por inicia a propaganda
     */
    public void iniciaPropaganda() throws IOException, InterruptedException {
        buscaArquivos();
        System.out.println("Vai entra");
        if (lista.isEmpty()) {
            
        }
        Collections.shuffle(lista);
        initMediaPlayer(lista.iterator());
        final DoubleProperty width = jMidiaView.fitWidthProperty();
        final DoubleProperty height = jMidiaView.fitHeightProperty();
        width.bind(Bindings.selectDouble(jMidiaView.sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(jMidiaView.sceneProperty(), "height"));
        jMidiaView.setPreserveRatio(true);
        if (Main.getTipo().equals("PADRAO")) {
            jLVS.setText("X");
            jLVS.setStyle("-fx-font-size: 96");
        } else {
            jLVS.setText("SETS");
            jLVS.setStyle("-fx-font-size: 70");
        }
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
    }

    public void gravaMidiaPadrao() throws FileNotFoundException, IOException {
        File origem = new File("C:\\z\\VideoPadrao.mp4");
        File destino = new File(Main.getPath() + "\\Midia\\VideoPadrao.mp4");
        if (destino.exists()) {
            destino.delete();
        }
        FileChannel sourceChannel = null;
        FileChannel destinationChannel = null;
        try {
            sourceChannel = new FileInputStream(origem).getChannel();
            destinationChannel = new FileOutputStream(destino).getChannel();
            sourceChannel.transferTo(0, sourceChannel.size(),
                    destinationChannel);
        } finally {
            if (sourceChannel != null && sourceChannel.isOpen()) {
                sourceChannel.close();
            }
            if (destinationChannel != null && destinationChannel.isOpen()) {
                destinationChannel.close();
            }
        }
//        BufferedOutputStream output = null;
//        URL url = getClass().getResource("/imagens/VedeoPadrao.mp4");
//        File origem = new File(url.toString());
//        File destino = new File(Main.getPath() + "\\Midia\\VideoPadrao.mp4");
//        if (destino.exists()) {
//            FileReader fis = new FileReader(origem);
//            BufferedReader bufferedReader = new BufferedReader(fis);
//            StringBuilder buffer = new StringBuilder();
//            String line = "";
//            while ((line = bufferedReader.readLine()) != null) {
//                buffer.append(line).append("\n");
//            }
//            fis.close();
//            bufferedReader.close();
//            FileWriter writer = new FileWriter(destino);
//            writer.write(buffer.toString());
//            writer.flush();
//            writer.close();

//        try {
//            URL url = getClass().getResource("/imagens/VedeoPadrao.mp4");
//            String source = url.toString();
//            InputStream resource = getClass().getClassLoader().getResourceAsStream(source);
//            if (resource == null) {
//                throw new IOException("Resource not found: " + source);
//            }
//            BufferedInputStream input = new BufferedInputStream(resource);
//            output = new BufferedOutputStream(new FileOutputStream(destino));
//            byte[] buffer = new byte[1024];
//            int length;
//            while ((length = input.read(buffer)) > 0) {
//                output.write(buffer, 0, length);
//            }
//            output.close();
//            input.close();
//        } catch (FileNotFoundException ex) {
//            LOGGER.warning("Erro! File not found!");
//        } catch (IOException ex) {
//            LOGGER.warning("Erro de I/O Video Padrão");
//        }
    }
}
