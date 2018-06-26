/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Cristiano Alex Künas
 */
public class ServerPropaganda {

    private int i = 0;
    private Socket sv;
    private ServerSocket servidor;

    public void receberArquivo(String nomeArquivo) throws IOException {
        servidor = new ServerSocket(50001);
        sv = servidor.accept();

        DataInputStream out = new DataInputStream(sv.getInputStream());
        FileOutputStream file = new FileOutputStream(main.Main.getPath()+"\\Midia\\"+nomeArquivo);
        byte[] buf = new byte[4096];
        while (true) {
            int len = out.read(buf);
            if (len == -1) {
                break;
            }
            file.write(buf, 0, len);
        }
        file.close();
        out.close();
        sv.close();
        servidor.close();

    }

    public Socket getSv() {
        return sv;
    }

    public void setSv(Socket sv) {
        this.sv = sv;
    }

    public ServerSocket getServidor() {
        return servidor;
    }

    public void setServidor(ServerSocket servidor) {
        this.servidor = servidor;
    }
    
    

}
