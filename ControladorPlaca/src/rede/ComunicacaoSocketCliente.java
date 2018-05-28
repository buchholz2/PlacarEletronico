/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rede;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import main.Main;

/**
 *
 * @author danie
 */
public class ComunicacaoSocketCliente implements Runnable {

    private String mensagem = "";
    private Main teste;

    public ComunicacaoSocketCliente(Main main) {
        this.teste = main;
    }

    public ComunicacaoSocketCliente() {

    }

    @Override
    public void run() {
        System.out.println("Chegou");
        try {
            Socket cliente = new Socket("localhost", 12345);

            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            while (true) {
                if (mensagem != "") {
                    saida.writeUTF(mensagem);
                    saida.flush();

                    String msg = entrada.readUTF();
                    System.out.println(msg);
                    mensagem = "";
                }
                Thread.sleep(10);
            }

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }

    }

    public void esperaMSG(String msg) {
        mensagem = msg;
    }
}
