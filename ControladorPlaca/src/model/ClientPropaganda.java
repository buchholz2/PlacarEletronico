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

/**
 *
 * @author Cristiano Alex Künas
 */
public class ClientPropaganda {

    private Socket cliente;
  
    public void enviaArquivo(File file) throws IOException {
        cliente = new Socket("127.0.0.1", 50001);
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

    public Socket getCliente() {
        return cliente;
    }

    public void setCliente(Socket cliente) {
        this.cliente = cliente;
    }

}
