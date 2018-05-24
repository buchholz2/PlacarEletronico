/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rede;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import scene.SceneEscolhaModalidade;
import scene.ScenePrincipal;

/**
 *
 * @author danie
 */
public class ComunicacaoSocketCliente implements Runnable {

    @Override
    public void run() {
        System.out.println("Chegou");
        try {
            Socket cliente = new Socket("10.140.1.224", 12345);

            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());

            saida.writeUTF("LOGIN$" + jTFUsuario.getText() + "$" + jTFSenha.getText());
            saida.flush();

            String msg = entrada.readUTF();
            System.out.println(msg);

            if (msg.equals("ACESSO_PERMITIDO")) {
                ScenePrincipal scene = new ScenePrincipal();
                scene.getStage().close();
                new SceneEscolhaModalidade().start(new Stage());

            } else if (msg.equals("NEGADO")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("USUARIO INVALIDO");
                alert.setHeaderText("ERRO AO ACESSAR O SISTEMA");
                alert.setContentText("OTARIO DEU CERTO");
                alert.showAndWait();
            }

            saida.close();
            entrada.close();
            System.out.println("Conexão encerrada");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
//       String usuario = jTFUsuario.getText();
//       String senha = jTFSenha.getText();
//       
//       if ((usuario))

    }
}
