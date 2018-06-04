/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rede;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.Main;

/**
 *
 * @author danie
 */
public class ComunicacaoSocketServidor implements Runnable {

    private boolean fimcrono = true;
    Stage p;

    public ComunicacaoSocketServidor(Stage p) {
        this.p = p;
    }

    public ComunicacaoSocketServidor() {

    }

    @Override
    public void run() {
        try {
            // Instancia o ServerSocket ouvindo a porta 12345
            ServerSocket servidor = new ServerSocket(12345);
            System.out.println("Servidor ouvindo a porta 12345");
            //System.out.println("" + p.getRoot().lookup("#jLCronometroCentral").getId());

            while (true) {
                // o método accept() bloqueia a execução até que
                // o servidor receba um pedido de conexão
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());

                ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
                ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());

                String msg = entrada.readUTF();

                String[] escolha = msg.split("\\$");

                if (escolha[0].equals("#TIME")) {
                    saida.writeUTF(time(escolha));
                    saida.flush();
                } else if (escolha[0].equals("LOGIN")) {
                    saida.writeUTF(login(escolha));
                    saida.flush();
                } else if (escolha[0].equals("#INICIA_CRONO")) {
                    saida.writeUTF(iniciaCronos(escolha));
                    saida.flush();
                }

                saida.close();

                entrada.close();

            }

        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public String time(String[] msg) {
        String opcao = msg[1];
        String retorno = "nada feito";
        switch (opcao) {
            case "VISITANTE":
                String funcao = msg[2];
                switch (funcao) {
                    case "SOMA_PONTO":
                        if (msg[3].equals("UM")) {

                        } else if (msg[3].equals("DOIS")) {

                        } else {

                        }
                        retorno = "#SOMA$OK";
                        break;
                    case "SUB_PONTO":
                        System.out.println("RECEBEU SUCESSO 2");
                        retorno = "Adicionado 2 Ponto";
                        break;
                }
                break;

            case "LOCAL":

                break;
        }
        return retorno;
    }

    public String login(String[] msg) throws IOException {

        Main.loadScene("/view/FXMLBasquete.fxml");

        String retorno = "LOGADO";
        return retorno;
    }

    public String fechaConexao(String[] msg) {
        return "";
    }

    public String iniciaCronos(String[] msg) {
        System.out.println("Chego no corte");
        String[] msm = msg[1].split("\\:");
        if (msm.length > 1) {
            int minutos = Integer.parseInt(msm[0]);
            int segundos = Integer.parseInt(msm[1]);
            chamaCronos((Label) p.getScene().getRoot().lookup("#jLCronometroCentral"), minutos, segundos, 0);
            return "CRONOS_INICIADO";
        } else {
            int minutos = Integer.parseInt(msm[0]);
            int segundos = Integer.parseInt(msm[1]);
            chamaCronos((Label) p.getScene().getRoot().lookup("#jLCronometroCentral"), minutos, segundos, 0);
            return "CRONOS_INICIADO";
        }

    }

    public void chamaCronos(Label label, int min, int seg, int mili) {
        Thread th = new Thread(iniciaCronos(label, min, seg, mili));
        th.setDaemon(true);
        th.start();
        System.out.println("Thread Iniciada" + label.getText());
    }

    private Task iniciaCronos(Label l, int min, int seg, int mili) {

        Task task = new Task<Void>() {
            int m = min;
            int s = seg;
            int ms = mili;
            String minutos;
            String segundos;
            String milisegundos;

            @Override
            public Void call() throws Exception {
                while (fimCrono()) {

                    if (m > 9) {
                        minutos = "" + m;
                    } else {
                        minutos = "0" + m;
                    }
                    if (s > 9) {
                        segundos = "" + s;
                    } else {
                        segundos = "0" + s;
                    }
                    if (ms > 9) {
                        milisegundos = "" + ms;
                    } else {
                        milisegundos = "0" + ms;
                    }
                    Platform.runLater(() -> {
                        l.setText(minutos + ":" + segundos + ":" + milisegundos);
                    });

                    if (m == 0) {
                        if (s == 0) {
                            if (ms == 0) {
                                fimcrono = false;
                                URL url = getClass().getResource("/estilos/apito.wav");
                                AudioClip audio = Applet.newAudioClip(url);
                                audio.play();
                            }
                        }
                    }

                    ms--;

                    if (ms < 0) {
                        ms = 99;
                        s--;
                    }
                    if (s < 0) {
                        s = 59;
                        m--;
                    }
                    Thread.sleep(10);
                }
                return null;
            }
        };
        return task;
    }

    public boolean fimCrono() {
        return fimcrono;
    }

}
