/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rede;

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
public class ComunicacaoSocket implements Runnable {

    private Main teste;
    
    public ComunicacaoSocket(Main main) {
        this.teste = main;
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

                } else if (escolha[0].equals("LOGIN")){
                    saida.writeUTF(login(escolha));
                    saida.flush();
                }
                
                saida.close();

                entrada.close();
                cliente.close();
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
        switch (tipo){
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
}
