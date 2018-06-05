package main;


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

    /**
     * A classe principal da aplicação em JavaFX
     */
    public Main() {
        thisClass = getClass();
    }

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
