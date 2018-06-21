package main;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import rede.ComunicacaoSocketServidor;

public class Main extends Application {

    public static Stage primaryStage;
    public static Scene sceneBasquete, scenePrincipal;
    public static Class thisClass;
    public static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    /**
     * A classe principal da aplicação em JavaFX
     */
    public Main() {
        thisClass = getClass();
    }
//
//    public static void printLog(String msg) {
//        LOGGER.info(msg);
//    }

    /**
     * Inicia o layout da aplicação
     */
    @Override
    public void start(Stage primaryStage) {
        setStage(primaryStage);

        new Thread(new ComunicacaoSocketServidor(primaryStage)).start();
        Font.loadFont(this.getClass().getResource("/estilos/fontes/digi.ttf").toExternalForm(), 23.8);

        loadScene("/view/FXMLPrincipal.fxml");
    }

    public static void main(String[] args) {
        Handler fh = null;
        try {
            // Nome do arquivo, booleano (append)
            fh = new FileHandler("C:\\Placar\\Log\\log.txt", true);
        } catch (IOException | SecurityException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Padrão é XML, para log no formato texto deve setar.
        fh.setFormatter(new SimpleFormatter());

        Logger.getLogger("").addHandler(fh);
        // Remoção das mensagens no console
        Logger l = Logger.getLogger("");
        Handler[] handlers = l.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            l.removeHandler(handlers[0]);
        }

        launch(args);
    }

    public void setStage(Stage s) {
        this.primaryStage = s;
    }

    public static void loadScene(String local) {

        try {
            Parent root = FXMLLoader.load(thisClass.getClass().getResource(local));
            scenePrincipal = new Scene(root);
            Platform.runLater(() -> {
                primaryStage.setScene(scenePrincipal);
                primaryStage.setTitle("Placar");
                primaryStage.show();
                primaryStage.setFullScreen(true);
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
}
