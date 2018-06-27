/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import main.Main;

/**
 *@author Cristiano Künas
 * @author Daniel Buchholz
 * @author Douglas Hoffmann
 * @author Leandro Heck
 */
public class ClientPropaganda {

    private Socket cliente;
  
    /**
     * Ação de enviar os arquivos para o diretorio, onde
     * será salvo as mídias de propaganda
     * 
     * @param file
     * @throws IOException 
     */
    public void enviaArquivo(File file) throws IOException {
        cliente = new Socket(Main.getIPServidor(), 50001);
        DataOutputStream out = new DataOutputStream(cliente.getOutputStream());

        FileInputStream fl = new FileInputStream(file.getPath());

        byte[] buf = new byte[4096];

        while (true) {
            int len = fl.read(buf);
            if (len == -1) {
                break;
            }
            out.write(buf, 0, len);
        }
        fl.close();
        out.close();
        cliente.close();

    }

    /**
     * Ação de ler que é o cliente
     * 
     * @return 
     */
    public Socket getCliente() {
        return cliente;
    }

    /**
     * Ação de gravar o cliente
     * 
     * @param cliente 
     */
    public void setCliente(Socket cliente) {
        this.cliente = cliente;
    }

}
