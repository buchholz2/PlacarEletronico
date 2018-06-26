package main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
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
import model.ListaUsuarios;
import model.Usuario;
import rede.ComunicacaoSocketServidor;

public class Main extends Application {

    public static boolean propaganda = false;
    public static Stage secundaryStage;
    public static Stage primaryStage;
    public static Scene sceneBasquete, scenePrincipal;
    public static Class thisClass;
    public static Class outraClass;
    private static final String PATH = (System.getProperty("user.home") + "\\Documents\\Placar\\");
    public static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    public static String nomeVisitante = "";
    public static String nomeLocal = "";
    public static int pLocal = 0;
    public static int pVisitante = 0;

    /**
     * A classe principal da aplicação em JavaFX
     */
    public Main() {
        thisClass = getClass();
        outraClass = getClass();
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
        iniciaDiretorioLog();
        criaDirXmlPrimeiraExecucao();
        criaDirMidia();
        setDeslogaAllFalse();
        launch(args);
    }

    public static void setDeslogaAllFalse() {
        ComunicacaoSocketServidor com = new ComunicacaoSocketServidor();
        ListaUsuarios users = com.leituraXML();
        Iterator<Usuario> iterator = users.getUsuarios().iterator();
        while (iterator.hasNext()) {
            Usuario user = iterator.next();
            user.setLogado(false);  
            com.gravarXML(users);
        }        
    }

    /**
     * Quando iniciar o sistema se não existir o diretorio do arquivo XML.
     * É criado o diretorio deste arquivo
     */
    public static void criaDirXmlPrimeiraExecucao() {
        File file = new File(PATH + "xml");
        if (!file.exists()) {
            Path p = Paths.get(PATH + "xml");
            try {
                Files.createDirectories(p);
                criaUsuariosXmlPrimeiraExecucao();
            } catch (IOException ex) {
                Main.LOGGER.config("Erro ao fazer a primeira criação do diretório do XML");
            }
        }
    }

    /**
     * Quando iniciar o sistema se não existir o diretorio de Midia.
     * É criado o diretorio deste arquivo
     */
    public static void criaDirMidia() {
        File file = new File(PATH + "Midia");
        if (!file.exists()) {
            Path p = Paths.get(PATH + "Midia");
            try {
                Files.createDirectories(p);
            } catch (IOException ex) {
                Main.LOGGER.config("Problema ao criar diretório de Midias!");   
            }
        }
    }

    /**
     * Na primeira execução do programa não existe nenhum dado dentro do XML.
     * Aqui é criado o XML com os usuários padrões.
     * ADM, PLACAR, PROPAGANDA
     */
    private static void criaUsuariosXmlPrimeiraExecucao() {
        ComunicacaoSocketServidor c = new ComunicacaoSocketServidor();
        ListaUsuarios lista = new ListaUsuarios();
        Usuario adm = new Usuario();
        adm.setUsuario("adm");
        adm.setSenha(c.encode("adm"));
        adm.setUserAdm(true);

        Usuario placar = new Usuario();
        placar.setUsuario("placar");
        placar.setSenha(c.encode("placar"));
        placar.setUserPlacar(true);

        Usuario prop = new Usuario();
        prop.setUsuario("propaganda");
        prop.setSenha(c.encode("propaganda"));
        prop.setUserPropaganda(true);

        lista.getUsuarios().add(adm);
        lista.getUsuarios().add(placar);
        lista.getUsuarios().add(prop);

        c.gravarXML(lista);
    }

    /**
     * Inicia o diretorio de logs
     */
    public static void iniciaDiretorioLog() {
        Date data = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dataFormatada = sdf.format(data);
        Path path = Paths.get(PATH + "\\Log\\" + dataFormatada);
        try {
            Files.createDirectories(path);
        } catch (IOException ex) {
           // Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            Main.LOGGER.config("Erro ao iniciar o diretorio de Log");
            
        }

        Handler fh = null;
        try {
            // Nome do arquivo, booleano (append)
            fh = new FileHandler(path + "\\log.txt", true);
        } catch (IOException ex) {
           // Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            Main.LOGGER.config("Erro ao criar arquivo de Log");
            
        } catch (SecurityException ex) {
             Main.LOGGER.warning("Erro de segurança ao criar arquivo de Log");
            
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

    /**
     * Ação de ler o Path
     * 
     * @return 
     */
    public static String getPath() {
        return PATH;
    }

    /**
     * Ação de gravar o Path
     * 
     * @param s 
     */
    public void setStage(Stage s) {
        this.primaryStage = s;
    }

    /** 
     * Ação de ler a cena
     * @param local 
     */
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
            Main.LOGGER.config("Erro ao carregar cena");
            System.out.println(e.toString());
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

    /**
     * 
     * 
     * 
     * @param pontosL
     * @param pontosV
     * @param nomeLocal
     * @param nomeVisitante
     * @throws IOException 
     */
    public static void propaganda(int pontosL, int pontosV, String nomeLocal, String nomeVisitante) throws IOException {
        Main.pLocal = pontosL;
        Main.pVisitante = pontosV;
        Main.nomeLocal = nomeLocal;
        Main.nomeVisitante = nomeVisitante;
        try {
            propaganda = false;
            FXMLLoader fxmlLoader = new FXMLLoader(outraClass.getClass().getResource("/view/FXMLPropaganda.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();

            Platform.runLater(() -> {
                secundaryStage = new Stage();
                secundaryStage.setScene(new Scene(root1));
                secundaryStage.show();
                secundaryStage.setFullScreen(true);
            });
        } catch (Exception e) {
            Main.LOGGER.config("Erro ao carregar cena de propaganda");
            System.out.println(e.toString());
        }

    }

    public void setStageSecundary(Stage s) {
        Main.secundaryStage = s;
    }

    /**
     *
     * @return
     */
    public static Stage getStageSecundary() {
        propaganda = true;
        return secundaryStage;
    }

    public static boolean fechaPropaganda() {
        return propaganda;
    }

    public static Stage alteraStageSecundary() {
        return secundaryStage;
    }

    public static String getNomeVisitante() {
        return nomeVisitante;
    }

    public static String getNomeLocal() {
        return nomeLocal;
    }

    public static int getpLocal() {
        return pLocal;
    }

    public static int getpVisitante() {
        return pVisitante;
    }

}
