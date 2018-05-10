/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.xml.bind.JAXBException;

/**
 *
 * @author Leandro
 */
public class ServerPlacarSocket extends Thread{
    private final Socket socket;

    public ServerPlacarSocket(Socket socket) {
        this.socket = socket;
    }
    
    // Método para iniciar o servidor
    public static void iniciar() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try (ServerSocket server = new ServerSocket(9898)) {
                    while (true) {
                        new ServerPlacarSocket(server.accept()).start();
                    }
                } catch (IOException ex) {
                    // IMPLEMENTAR LOG
                }
            }
        });
        thread.start();
    }
    
    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String login = in.readLine();
            String senha = in.readLine();
            User user = new User();

//            if (validaLogin(login, senha, u)) {
//                if (u.isAdmin()) {
//                    out.println("#conexao;ok;usuario-administrador");
//                } else if (u.isPlacar()) {
//                    out.println("#conexao;ok;usuario-placar");
//                } else if (u.isPropaganda()) {
//                    out.println("#conexao;ok;usuario-propaganda");
//                } else {
//                    out.println("#conexao;not-ok");
//                }
//
//                while (true) {
//                    String input = in.readLine();
//                    if (input == null || input.equals(".")) {
//                        break;
//                    } else {
//                        out.println(processarComando(input));
//                    }
//                }
//            } else {
//                out.println("#conexao;not-ok");
//            }
        } catch (IOException ex) {
            // IMPLEMENTAR LOG
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                // IMPLEMENTAR LOG
            }
        }
    }
    
    
    
    
    private static boolean validaLogin(String login, String senha, User usuario) {
        try {
            if (login == null || senha == null) {
                return false;
            } else {
                //ListUser users = (ListUser) DadosXML.select("ListaUsuarios");
                ListUser users = new ListUser();

                for (User user : users.getUsers()) {
                    if (login.equals(user.getUser()) && senha.equals(user.getPassword())) {
                        usuario.setUser(user.getUser());
                        usuario.setPassword(user.getPassword());
                        usuario.setUserAdm(user.isUserAdm());
                        usuario.setUserPlacar(user.isUserPlacar());
                        usuario.setUserPropaganda(user.isUserPropaganda());

                        return true;
                    }
                }
            }
        } catch (Exception e){//(JAXBException ex) {
            // IMPLEMENTAR LOGIN
            return false;
        }

        return false;
    }
}