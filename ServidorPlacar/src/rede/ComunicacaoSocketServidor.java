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
import main.Main;

/**
 *
 * @author danie
 */
public class ComunicacaoSocketServidor implements Runnable {

    private Main teste;

    public ComunicacaoSocketServidor(Main main) {
        this.teste = main;
    }

    public ComunicacaoSocketServidor() {

    }

    @Override
    public void run() {
        try {
            // Instancia o ServerSocket ouvindo a porta 12345
            ServerSocket servidor = new ServerSocket(12345);
            System.out.println("Servidor ouvindo a porta 12345");

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

    public static String timeA(String[] msg) {
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

    public static String login(String[] msg) {
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

    public static String fechaConexao(String[] msg) {
        return "";
    }

    public static String iniciaCronos(String[] msg) {
        System.out.println("Chego no corte");
        String[] msm = msg[1].split("\\:");
        if (msm.length > 1) {
            int minutos = Integer.parseInt(msm[0]);
            int segundos = Integer.parseInt(msm[1]);
            
            FXMLBasqueteController bas = new FXMLBasqueteController();
            bas.chamaCronos(minutos, segundos, 0);
            return "CRONOS_INICIADO";
        } else {
            int minutos = Integer.parseInt(msm[0]);
            int segundos = Integer.parseInt(msm[1]);
            FXMLBasqueteController bas = new FXMLBasqueteController();
            bas.chamaCronos(minutos, segundos, 0);
            return "CRONOS_INICIADO";
        }

    }
}
