/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rede;

import com.sun.org.apache.bcel.internal.generic.AALOAD;
import control.FXMLBasqueteController;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.Main;
import scene.SceneBasquete;

/**
 *
 * @author danie
 */
public class ComunicacaoSocketServidor implements Runnable {

    private Main teste;
    SceneBasquete p;

    public ComunicacaoSocketServidor(Main main) {
        this.teste = main;
    }

    public ComunicacaoSocketServidor(SceneBasquete p) {
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
            System.out.println("" + p.getRoot().lookup("#jLCronometroCentral").getId());

            while (true) {
                // o método accept() bloqueia a execução até que
                // o servidor receba um pedido de conexão
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());

                ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
                ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());

                String msg = entrada.readUTF();
                String[] escolha = msg.split("\\$");

                if (escolha[0].equals("TIME_A")) {
                    saida.writeUTF(timeA(escolha));
                    saida.flush();
                } else if (escolha[0].equals("LOGIN")) {
                    saida.writeUTF(login(escolha));
                    saida.flush();
                } else if (escolha[0].equals("FECHAR")) {
                    saida.writeUTF(fechaConexao(escolha));
                    saida.flush();
                    cliente.close();
                } else if (escolha[0].equals("INICIA_CRONO")) {
                    System.out.println("Cgegou aq");
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

    public String timeA(String[] msg) {
        String tipo = msg[1];
        String retorno = "nada feito";
        switch (tipo) {
            case "PONTOS":
                String quantidade = msg[2];
                switch (quantidade) {
                    case "1":
                        System.out.println("RECEBEU SUCESSO 1");
                        retorno = "Adicionado 1 Ponto";
                        break;
                    case "2":
                        System.out.println("RECEBEU SUCESSO 2");
                        retorno = "Adicionado 2 Ponto";
                        break;
                    case "3":
                        System.out.println("RECEBEU SUCESSO 3");
                        retorno = "Adicionado 3 Ponto";
                        break;
                }
                break;
        }
        return retorno;
    }

    public String login(String[] msg) {
        String tipo = msg[1];
        String retorno = "nada feito";
        switch (tipo) {
            case "admin":
                System.out.println("ACESSO ADMIN!");
                retorno = "ACESSO_PERMITIDO";
                break;
            default:
                retorno = "NEGADO";
                break;
        }

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

//            FXMLBasqueteController bas = new FXMLBasqueteController();
//            bas.chamaCronos(minutos, segundos, 0);
            chamaCronos((Label) p.getRoot().lookup("#jLCronometroCentral"), minutos, segundos, 0);
            return "CRONOS_INICIADO";
        } else {
            int minutos = Integer.parseInt(msm[0]);
            int segundos = Integer.parseInt(msm[1]);

//            stage.getScene().getRoot().getParent().getChildrenUnmodifiable().
//            FXMLBasqueteController bas = new FXMLBasqueteController();
//            bas.chamaCronos(minutos, segundos, 0);
            chamaCronos((Label) p.getRoot().lookup("#jLCronometroCentral"), minutos, segundos, 0);
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
                while (true) {
                    System.out.println("Thread Iniciada While" + l.getText());

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
                        l.setText(minutos+":" + segundos +":"+ milisegundos);
                    });
                    
                    
                    ms--;
                    
                    if (ms < 0) {
                        ms = 99;
                        s--;
                    }
                    if (s < 0) {
                        s = 59;
                        m--;
                    }
                
                    Thread.sleep(2000);
                }

            }
        };

        return task;
    }

}
