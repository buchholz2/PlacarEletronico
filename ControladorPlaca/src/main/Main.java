package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage primaryStage;
    public static Scene sceneBasquete, scenePrincipal;
    public static Class thisClass;
    public static Socket cliente;
    public static ObjectInputStream entrada;
    public static ObjectOutputStream saida;

    /**
     * A classe principal da aplicação em JavaFX
     */
    public Main() throws IOException {
        thisClass = getClass();
        cliente = new Socket("localhost", 12345);
        entrada = new ObjectInputStream(cliente.getInputStream());
        saida = new ObjectOutputStream(cliente.getOutputStream());
    }

    /**
     * Inicia o layout da aplicação
     */
    @Override
    public void start(Stage primaryStage) {
        setStage(primaryStage);

        Font.loadFont(this.getClass().getResource("/estilos/fontes/digi.ttf").toExternalForm(), 23.8);
        Font.loadFont(this.getClass().getResource("/estilos/fontes/SoccerLeague.ttf").toExternalForm(), 23.8);

        loadScene("/view/FXMLLoginCadastro.fxml");
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void setStage(Stage s) {
        this.primaryStage = s;
    }
    
    public static Stage getStage(){
        return primaryStage;
    }

    public static void loadScene(String local) {

        try {
            Parent root = FXMLLoader.load(thisClass.getClass().getResource(local));
            scenePrincipal = new Scene(root);
            Platform.runLater(() -> {
                primaryStage.setScene(scenePrincipal);
                primaryStage.centerOnScreen();
                primaryStage.setTitle("Controlador");
                primaryStage.show();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
//        try {
//            Parent root = FXMLLoader.load(thisClass.getClass().getResource("/view/FXMLBasquete.fxml"));
//            sceneBasquete = new Scene(root);
//            Stage novaStage = (Stage) primaryStage.getScene().getWindow();
//
//            novaStage.setScene(sceneBasquete);
//            
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    public static String mandaMSG(String msg) throws IOException {
        saida.writeUTF(msg);
        saida.flush();

        String retorno = entrada.readUTF();
        System.out.println(retorno);
        return retorno;
    }
}
