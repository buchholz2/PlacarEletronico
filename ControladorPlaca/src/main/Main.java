package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author Cristiano Künas
 * @author Daniel Buchholz
 * @author Douglas Hoffmann
 * @author Leandro Heck
 */
public class Main extends Application {

    public static Stage primaryStage;
    public static Scene sceneBasquete, scenePrincipal;
    public static Class thisClass;
    public static Socket cliente;
    public static ObjectInputStream entrada;
    public static ObjectOutputStream saida;
    private static final String PATH = (System.getProperty("user.home")+"\\Documents\\Placar\\");
    public static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    /**
     * A classe principal da aplicação em JavaFX
     */
    public Main() throws IOException {
        thisClass = getClass();
    }

    /**
     * Inicia o layout da aplicação
     *
     * @throws java.io.IOException
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        setStage(primaryStage);

        Font.loadFont(this.getClass().getResource("/estilos/fontes/digi.ttf").toExternalForm(), 23.8);
        Font.loadFont(this.getClass().getResource("/estilos/fontes/SoccerLeague.ttf").toExternalForm(), 23.8);

        loadScene("/view/FXMLLogin.fxml");
        primaryStage.initStyle(StageStyle.TRANSPARENT);

    }

    /**
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
//        setSocket(new Socket("localhost", 12345));
//        entrada = new ObjectInputStream(getSocket().getInputStream());
//        saida = new ObjectOutputStream(getSocket().getOutputStream());
        launch(args);
    }

    /**
     * Ação conectar ao servidor, passando o ip do servidor e a porta a qual será 
     * feita a conexão 
     */
    public static void conectar() {

        try {
            setSocket(new Socket("localhost", 12345));
            entrada = new ObjectInputStream(getSocket().getInputStream());
            saida = new ObjectOutputStream(getSocket().getOutputStream());
        } catch (ConnectException ex) {
            
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Açao de gravar a Stage
     * 
     * @param s
     */
    public void setStage(Stage s) {
        Main.primaryStage = s;
    }

    /**
     * Ação de ler a Stage 
     * 
     * @return
     */
    public static Stage getStage() {
        return primaryStage;
    }

    /**
     * Carrega views
     *
     * @param local
     */
    public static void loadScene(String local) {

        try {
            Parent root = FXMLLoader.load(thisClass.getClass().getResource(local));
            scenePrincipal = new Scene(root);
            Platform.runLater(() -> {

                primaryStage.setScene(scenePrincipal);
                primaryStage.centerOnScreen();
                primaryStage.setResizable(false);
                primaryStage.setTitle("Controlador");
                primaryStage.show();
            });
        } catch (IOException e) {
            Main.LOGGER.severe("Erro ao carregar cena !");
            System.out.println(e.toString());
            
        }

    }

    /**
     * Ação de mandar mensagens ao servidor
     * @param msg
     * @return
     * @throws IOException
     */
    public static String mandaMSG(String msg) throws IOException {

        saida.writeUTF(msg);
        saida.flush();

        String retorno = entrada.readUTF();
        System.out.println(retorno);

//        entrada.close();
//        saida.close();
        return retorno;
    }

    /**
     * Ação de gravar o socket
     * 
     * @param sok
     */
    private static void setSocket(Socket sok) {
        Main.cliente = sok;
    }

    /**
     * Ação de ler o socket
     * 
     * @return 
     */
    public static Socket getSocket() {
        return Main.cliente;
    }
    
    public static void iniciaDiretorioLog() {
        Date data = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dataFormatada = sdf.format(data);
        Path path = Paths.get(PATH+"\\Log\\" + dataFormatada);
        try {
            Files.createDirectories(path);
        } catch (IOException ex) {
           // Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            Main.LOGGER.severe("Erro ao criar diretorio do Log");
            System.out.println(ex.toString());
        }

        Handler fh = null;
        try {
            // Nome do arquivo, booleano (append)
            fh = new FileHandler(path + "\\log.txt", true);
        } catch (IOException ex) {
           // Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            Main.LOGGER.severe("Erro ao criar arquivo de Log");
            System.out.println(ex.toString());
        } catch (SecurityException ex) {
             Main.LOGGER.severe("Erro de segurança ao criar arquivo de Log");
             System.out.println(ex.toString());
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

    }


}
