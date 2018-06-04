package scene;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneG extends Application {

    public static Stage primaryStage;
    public static Class thisClass;

    /**
     * A classe principal da aplicação em JavaFX
     */
    public SceneG() {
        thisClass = getClass();
    }

    /**
     * Inicia o layout da aplicação
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/FXMLPrincipal.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setTitle("Principal");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Inicia outra página
     *
     * @param nameFile
     * @param titlePage
     */
    public static void loadScene(String nameFile, String titlePage) {

        Parent root;
        try {
            root = FXMLLoader.load(thisClass.getClass().getResource(nameFile));
            Scene scene = new Scene(root, 400, 400);
            primaryStage.setTitle(titlePage);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
