/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rede;

import model.Usuario;
import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import main.Main;
import model.ListaUsuarios;
import model.ServerPropaganda;
import org.apache.commons.codec.binary.Base64;



/**
 * @author Cristiano Künas
 * @author Daniel Buchholz
 * @author Douglas Hoffmann
 * @author Leandro Heck
 */
public class ComunicacaoSocketServidor implements Runnable {

    private boolean fimCrono = false;
    private Stage p;
    public int pontosL = 0;
    public int pontosV = 0;
    public boolean cronosPausado = false;
    private int tempoLan = 24;
    private int muda = 60;
    private int faltasL = 0;
    private int faltasV = 0;
    private int rodada = 1;
    private int somaRodadaV = 0;
    private int somaRodadaL = 0;
    String resultadoFinal = "";
    private int count = 0;
    private int i = 0;
    private String nomeVisitante = "Visitante";
    private String nomeLocal = "Local";
    private int valorSetLocal = 0;
    private int valorSetVisitante = 0;
    private int setAutal = 0;

    /**
     * Construtor
     *
     * @param p
     */
    public ComunicacaoSocketServidor(Stage p) {
        this.p = p;
    }

    /**
     * Contrutor default
     */
    public ComunicacaoSocketServidor() {

    }

    /**
     *
     */
    @Override
    public void run() {
        try {
            ServerSocket servidor = new ServerSocket(12345);
            Main.LOGGER.info("Servidor ouvindo na porta 12345");
            //System.out.println("" + p.getRoot().lookup("#jLCronometroCentral").getId());
            while (true) {

                Socket cliente = servidor.accept();

                if (count < 1) {
                    ProgressIndicator pro = (ProgressIndicator) p.getScene().getRoot().lookup("#progressIndicator");
                    pro.setOpacity(0);
                }

                if (count < 3) {
                    System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress() + " n:" + count);
                    count++;
                    chamaConversa(cliente);
                } else {
                    ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
                    ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
                    String msg = entrada.readUTF();
                    saida.writeUTF("#MAXIMO_USER");
                    saida.flush();
                    entrada.close();
                    saida.close();
                    cliente.close();
                }
            }
        } catch (IOException e) {
          //  System.out.println("Erro: " + e.getMessage());
            Main.LOGGER.severe("Erro na conexão do cliente com o servidor - Método run do Servidor ");
            System.out.println(e.toString());
        }
    }

    /**
     * Seleciona time
     *
     * @param msg
     * @return
     */
    public String time(String[] msg) {
        String opcao = msg[1];
        String retorno = "nada feito";
        switch (opcao) {
            case "VISITANTE":
                mudaPontosV((Label) p.getScene().getRoot().lookup("#jLTimeDireitoPontos"), msg[3], msg[2]);
                retorno = "#OK";
                break;
            case "LOCAL":
                mudaPontosL((Label) p.getScene().getRoot().lookup("#jLTimeEsquerdoPontos"), msg[3], msg[2]);
                retorno = "#OK";
                break;
        }
        return retorno;
    }

    /**
     * Login
     *
     * @param msg
     * @return
     * @throws IOException
     */
    public String login(String[] msg) throws IOException {
        String login = msg[1];
        String senha = msg[2];
        Usuario usuario = new Usuario();
        if (validaLogin(login, senha, usuario)) {
            System.out.println("verificou usuario e senha");
            if (usuario.isLogado()) {
                return ("#JA_LOGOU");
            } else if (usuario.isUserAdm()) {
                return ("#LOGADO$ADM");
            } else if (usuario.isUserPlacar()) {
                ListaUsuarios l = leituraXML();
                Iterator<Usuario> ite = l.getUsuarios().iterator();
                while (ite.hasNext()) {
                    Usuario u = ite.next();
                    if (u.isLogado()) {
                        return ("#JA_LOGOU_P");
                    }
                }
                return ("#LOGADO$PLACAR");
            } else if (usuario.isUserPropaganda()) {
                return ("#LOGADO$PROPAGANDA");
            } else {
                return ("#NOT$LOGADO");
            }
        } else {
            return ("#NOT$LOGADO");
        }
    }

    /**
     * Validação de usuário no XML
     *
     * @param login
     * @param senha
     * @param usuario
     * @return
     */
    public boolean validaLogin(String login, String senha, Usuario usuario) {
        if (login == null || senha == null) {
            return false;
        } else {
            ListaUsuarios users = leituraXML();
            if (users != null) {
                for (Usuario u : users.getUsuarios()) {
                    String pass = decode(u.getSenha());
                    if (login.equals(u.getUsuario()) && senha.equals(pass)) {
                        usuario.setUsuario(u.getUsuario());
                        usuario.setSenha(pass);
                        usuario.setUserAdm(u.isUserAdm());
                        usuario.setUserPlacar(u.isUserPlacar());
                        usuario.setUserPropaganda(u.isUserPropaganda());
                        usuario.setLogado((u.isLogado()));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Escolhe modalidade
     *
     * @param msg
     * @return
     */
    public String escolhaModalidade(String[] msg) {
        String opcao = msg[1];
        String retorno = "nada feito";
        switch (opcao) {
            case "BASQUETE":
                Main.loadScene("/view/FXMLBasquete.fxml");
                retorno = "ESCOLHIDA";
                break;

            case "VOLEI":
                Main.loadScene("/view/FXMLVolei.fxml");
                retorno = "ESCOLHIDA";
                break;

            case "PADRAO":
                Main.loadScene("/view/FXMLPadrao.fxml");
                retorno = "ESCOLHIDA";
                break;
        }
        return retorno;
    }

    /**
     * Adiciona usuário ao XML
     *
     * @param msg
     * @return
     */
    public String adicionaUsuario(String[] msg) {
        String opcao = msg[1];
        ListaUsuarios lista = leituraXML();
        Iterator<Usuario> iterator = lista.getUsuarios().iterator();
        while (iterator.hasNext()) {
            Usuario user = iterator.next();
            if (user.getUsuario().equals(msg[2])) {
                return ("#USUARIO_EXISTENTE");
            }
        }
        if (opcao.equals("ADMINISTRADOR")) {
            String senha = encode(msg[3]);
            Usuario adm = new Usuario();
            adm.setUserAdm(true);
            adm.setUsuario(msg[2]);
            adm.setSenha(senha);
            lista.getUsuarios().add(adm);
            gravarXML(lista);
            return ("#OK");
        } else if (opcao.equals("PLACAR")) {
            String senha = encode(msg[3]);
            Usuario placar = new Usuario();
            placar.setUserPlacar(true);
            placar.setUsuario(msg[2]);
            placar.setSenha(senha);
            lista.getUsuarios().add(placar);
            gravarXML(lista);
            return ("#OK");
        } else if (opcao.equals("PROPAGANDA")) {
            String senha = encode(msg[3]);
            Usuario propaganda = new Usuario();
            propaganda.setUserPropaganda(true);
            propaganda.setUsuario(msg[2]);
            propaganda.setSenha(senha);
            lista.getUsuarios().add(propaganda);
            gravarXML(lista);
            return ("#OK");
        } else {
            return ("#NOT$OK");
        }
    }

    /**
     * Criptografa senha
     *
     * @param encriptar
     * @return
     */
    public String encode(String encriptar) {
        String criptografado = Base64.encodeBase64String(encriptar.getBytes());
        return criptografado;
    }

    /**
     *
     * @param encriptado
     * @return
     */
    public String decode(String encriptado) {
        byte[] decoded = Base64.decodeBase64(encriptado);
        String deco = new String(decoded);
        return deco;
    }

    /**
     * Exclui usuário do XML
     *
     * @param msg
     * @return
     */
    public String excluirUsuario(String[] msg) {
        String opcao = msg[1];
        ListaUsuarios lista = leituraXML();
        Iterator<Usuario> iterator = lista.getUsuarios().iterator();
        while (iterator.hasNext()) {
            Usuario user = iterator.next();
            if (user.getUsuario().equals(opcao)) {
                if (!user.isLogado()) {
                    iterator.remove();
                    gravarXML(lista);
                    return ("#OK");
                }
                return ("#USUARIO_ATIVO");
            }
        }
        return ("#NOT$OK");
    }

    /**
     * Lista usuários cadastrados
     *
     * @param msg
     * @return
     */
    public String listaUsuario(String[] msg) {
        String opcao = msg[0];
        String funcao;
        String retorno = "";
        if (opcao.equals("#LISTAR_USUARIOS")) {
            ListaUsuarios lista = leituraXML();
            for (Usuario u : lista.getUsuarios()) {
                if (u.isUserAdm()) {
                    funcao = "ADMINISTRADOR?";
                    retorno = retorno.concat(u.getUsuario() + "$" + funcao);
                } else if (u.isUserPlacar()) {
                    funcao = "PLACAR?";
                    retorno = retorno.concat(u.getUsuario() + "$" + funcao);
                } else if (u.isUserPropaganda()) {
                    funcao = "PROPAGANDA?";
                    retorno = retorno.concat(u.getUsuario() + "$" + funcao);
                }
            }
            return retorno;
        }
        return ("#NOT$OK");
    }

    /**
     * Lista propagandas contidas no diretório servidor
     *
     * @param msg
     * @return
     */
    public String listaPropagandas(String[] msg) {
        String retorno = "";
        ArrayList<String> lista = new ArrayList<>();
        File file = new File(Main.getPath() + "Midia");
        File afile[] = file.listFiles();
        int k = 0;
        if (afile.length != 0) {
            for (int j = afile.length; k < j; k++) {
                File arquivos = afile[k];
                String u = arquivos.getName();
                lista.add(u);
            }
        }
        if (!lista.isEmpty()) {
            for (String arquivo : lista) {
                if (!arquivo.isEmpty()) {
                    retorno = retorno.concat(arquivo + "$");
                }
            }
            return retorno;
        }
        return ("#NOT$OK");
    }

    /**
     * Exclui propaganda do diretorio servidor
     *
     * @param msg
     * @return
     */
    public String excluirPropaganda(String[] msg) {
        String url = msg[1];
        File fl = new File(Main.getPath() + "Midia\\" + url);
        if (fl.exists()) {
            fl.delete();
            return ("#OK");
        }
        return ("#NOT$OK");
    }

    /**
     * Fecha conexão
     *
     * @param msg
     * @return
     */
    public String fechaConexao(String[] msg) {
        return "";
    }

    /**
     * Inicia cronometro
     *
     * @param msg
     * @return
     */
    public String iniciaCronos(String[] msg) {
        if (fimCrono != true) {
            fimCrono = true;
            System.out.println("Chego no corte");
            String[] msm = msg[1].split("\\:");
            if (msm.length > 1) {
                tempoLan = 24;
                int minutos = Integer.parseInt(msm[0]);
                int segundos = Integer.parseInt(msm[1]);
                chamaCronos((Label) p.getScene().getRoot().lookup("#jLCronometroCentral"), minutos, segundos, 0);
                return "CRONOS_INICIADO";
            } else {
                tempoLan = 24;
                int minutos = Integer.parseInt(msm[0]);
                int segundos = Integer.parseInt(msm[1]);
                chamaCronos((Label) p.getScene().getRoot().lookup("#jLCronometroCentral"), minutos, segundos, 0);
                return "CRONOS_INICIADO";
            }
        } else {
            return "CRONOS_EXECUTANDO";
        }

    }

    public void chamaCronos(Label label, int min, int seg, int mili) {
        Thread th = new Thread(iniciaCronos(label, min, seg, mili));
        th.setDaemon(true);
        th.start();
    }

    private Task iniciaCronos(Label l, int min, int seg, int mili) {

        Task task = new Task<Void>() {
            int m = min;
            int s = seg;
            int ms = mili;
            String minutos;
            String segundos;
            String milisegundos;

            @Override
            public Void call() throws Exception {

                while (fimCrono()) {

                    if (m > 9) {
                        minutos = "" + m;
                    } else {
                        minutos = "0" + m;
                    }
                    if (s > 9) {
                        segundos = "" + s;
                    } else {
                        segundos = "0" + s;
                    }
                    if (ms > 9) {
                        milisegundos = "" + ms;
                    } else {
                        milisegundos = "0" + ms;
                    }
                    Platform.runLater(() -> {
                        if (muda != s) {
                            muda = s;
                            iniciaTempoLPreview((Label) p.getScene().getRoot().lookup("#jLSeguraBola"));
                        }
                        l.setText(minutos + ":" + segundos + ":" + milisegundos);
                    });

                    while (cronosPausado) {
                        Thread.sleep(100);
                    }

                    if ((m == 0) && (s == 0) && (ms == 0)) {
                        fimCrono = true;
                        URL url = getClass().getResource("/estilos/apito.wav");
                        AudioClip audio = Applet.newAudioClip(url);
                        audio.play();
                    }

                    --ms;

                    if (ms < 0) {
                        ms = 99;
                        s--;
                    }
                    if (s < 0) {
                        s = 59;
                        m--;
                    }
                    Thread.sleep(10);
                }
                fimCrono = false;
                return null;
            }
        };
        return task;
    }

    public boolean fimCrono() {
        return fimCrono;
    }

    /**
     * Altera pontos time visitante
     *
     * @param l
     * @param pontos
     * @param funcao
     */
    private void mudaPontosV(Label l, String pontos, String funcao) {

        if (funcao.equals("SOMA_PONTO")) {
            if (pontos.equals("UM")) {
                pontosV++;
            } else if (pontos.equals("DOIS")) {
                pontosV = pontosV + 2;
            } else {
                pontosV = pontosV + 3;
            }
        } else {
            if (pontos.equals("UM")) {
                pontosV--;
            } else if (pontos.equals("DOIS")) {
                pontosV = pontosV - 2;
            } else {
                pontosV = pontosV - 3;
            }
        }

        if (pontosV < 0) {
            pontosV = 0;
        }

        if (pontosV > 9) {
            Platform.runLater(() -> {
                l.setText("" + pontosV);
            });
        } else {
            Platform.runLater(() -> {
                l.setText("0" + pontosV);
            });
        }
    }

    /**
     * Altera pontos time local
     *
     * @param l
     * @param pontos
     * @param funcao
     */
    private void mudaPontosL(Label l, String pontos, String funcao) {

        if (funcao.equals("SOMA_PONTO")) {
            if (pontos.equals("UM")) {
                pontosL++;
            } else if (pontos.equals("DOIS")) {
                pontosL = pontosL + 2;
            } else {
                pontosL = pontosL + 3;
            }
        } else {
            if (pontos.equals("UM")) {
                pontosL--;
            } else if (pontos.equals("DOIS")) {
                pontosL = pontosL - 2;
            } else {
                pontosL = pontosL - 3;
            }
        }

        if (pontosL < 0) {
            pontosL = 0;
        }

        if (pontosL > 9) {
            Platform.runLater(() -> {
                l.setText("" + pontosL);
            });
        } else {
            Platform.runLater(() -> {
                l.setText("0" + pontosL);
            });
        }
    }

    /**
     * Inicia tempo na preview
     *
     * @param l
     */
    private void iniciaTempoLPreview(Label l) {
        String muda;

        if (tempoLan > 9) {
            muda = ("" + tempoLan);
        } else {
            muda = ("0" + tempoLan);
        }

        Platform.runLater(() -> {
            l.setText(muda);
        });

        if (--tempoLan < 0) {
            cronosPausado = true;
            URL url = getClass().getResource("/estilos/grito.wav");
            AudioClip audio = Applet.newAudioClip(url);
            audio.play();
        }

    }

    private String mudaFalta(String[] msg) {
        if (msg[1].equals("LOCAL")) {
            Label l = (Label) p.getScene().getRoot().lookup("#jLTimeEsquerdoFaltas");
            if (msg[2].equals("SOMA")) {
                faltasL++;
            } else {
                faltasL--;
            }
            if (faltasL < 0) {
                faltasL = 0;
            }

            if (faltasL > 9) {
                Platform.runLater(() -> {
                    l.setText("" + faltasL);
                });
            } else {
                Platform.runLater(() -> {
                    l.setText("0" + faltasL);
                });
            }
            return "MUDADO";

        } else {
            Label l = (Label) p.getScene().getRoot().lookup("#jLTimeDireitoFaltas");
            if (msg[2].equals("SOMA")) {
                faltasV++;
            } else {
                faltasV--;
            }
            if (faltasV < 0) {
                faltasV = 0;
            }

            if (faltasV > 9) {
                Platform.runLater(() -> {
                    l.setText("" + faltasV);
                });
            } else {
                Platform.runLater(() -> {
                    l.setText("0" + faltasV);
                });
            }
            return "MUDADO";
        }
    }

    /**
     * Recupera usuarios do XML para manipulação
     *
     * @return
     */
    public ListaUsuarios leituraXML() {
        ListaUsuarios lista = null;
        try {
            File file = new File(Main.getPath() + "xml\\usuarios.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(ListaUsuarios.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            lista = (ListaUsuarios) jaxbUnmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            Main.LOGGER.severe("Falha ao ler o arquivo XML!");
            System.out.println(e.toString());
        }
        return lista;
    }

    /**
     * Grava XML atualizado
     *
     * @param l
     */
    public void gravarXML(ListaUsuarios l) {
        try {
            File file = new File(Main.getPath() + "xml\\usuarios.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(ListaUsuarios.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(l, file);
        } catch (JAXBException e) {
            Main.LOGGER.warning("Falha ao gravar o arquivo XML!");
            System.out.println(e.toString());
        }

    }

    /**
     * Restaura tudo
     *
     * @return
     */
    private String restauraTudo() {
        cronosPausado = false;
        fimCrono = false;
        pontosV = 0;
        pontosL = 0;
        tempoLan = 24;
        muda = 60;
        faltasV = 0;
        faltasL = 0;
        rodada = 1;
        somaRodadaL = 0;
        somaRodadaV = 0;
        Platform.runLater(() -> {
            Label l = (Label) p.getScene().getRoot().lookup("#jLCronometroCentral");
            l.setText("00:00:00");
            l = (Label) p.getScene().getRoot().lookup("#jLRodada1");
            l.setText("00 X 00");
            l = (Label) p.getScene().getRoot().lookup("#jLRodada2");
            l.setText("00 X 00");
            l = (Label) p.getScene().getRoot().lookup("#jLRodada3");
            l.setText("00 X 00");
            l = (Label) p.getScene().getRoot().lookup("#jLRodada4");
            l.setText("00 X 00");
            l = (Label) p.getScene().getRoot().lookup("#jLRodada5");
            l.setText("00 X 00");
            l = (Label) p.getScene().getRoot().lookup("#jLCronometroCentral");
            l.setText("00");
            l = (Label) p.getScene().getRoot().lookup("#jLCronometroCentral");
            l.setText("00");
            l = (Label) p.getScene().getRoot().lookup("#jLCronometroCentral");
            l.setText("00");
            l = (Label) p.getScene().getRoot().lookup("#jLCronometroCentral");
            l.setText("00");
            l = (Label) p.getScene().getRoot().lookup("#jLCronometroCentral");
            l.setText("00");
            l = (Label) p.getScene().getRoot().lookup("#jLCronometroCentral");
            l.setText("LOCAL");
            l = (Label) p.getScene().getRoot().lookup("#jLCronometroCentral");
            l.setText("VISITANTE");
        });
        return "RESTAURADO";
    }

    /**
     * Chama conversa controlador-placar
     *
     * @param cli
     */
    public void chamaConversa(Socket cli) {
        Thread th = new Thread(iniciaConversa(cli));
        th.setDaemon(true);
        th.start();
    }

    public void setLogar(String u) {
        ListaUsuarios lista = leituraXML();
        Iterator<Usuario> iterator = lista.getUsuarios().iterator();
        while (iterator.hasNext()) {
            Usuario user = iterator.next();
            if (user.getUsuario().equals(u)) {
                user.setLogado(true);
                gravarXML(lista);
            }
        }
    }

    public void setDeslogar(String u) {
        ListaUsuarios lista = leituraXML();
        Iterator<Usuario> iterator = lista.getUsuarios().iterator();
        while (iterator.hasNext()) {
            Usuario user = iterator.next();
            if (user.getUsuario().equals(u)) {
                user.setLogado(false);
                gravarXML(lista);
            }
        }
    }

    private Task iniciaConversa(Socket cliente) {

        Task task = new Task<Void>() {

            @Override
            public Void call() throws Exception {
                String user = "";
                ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
                ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());

                while (true) {
                    String msg = entrada.readUTF();

                    String[] escolha = msg.split("\\$");

                    if (escolha[0].equals("#TIME")) {
                        saida.writeUTF(time(escolha));
                        saida.flush();
                    } else if (escolha[0].equals("#LOGIN")) {
                        String m = login(escolha);
                        System.out.println(m);
                        if (!m.contains("NOT")) {
                            user = escolha[1];
                            if (!m.equals("#JA_LOGOU_P")) {
                                setLogar(user);
                            }
                        }
                        saida.writeUTF(m);
                        saida.flush();
                    } else if (escolha[0].equals("#INICIA_CRONO")) {
                        saida.writeUTF(iniciaCronos(escolha));
                        saida.flush();
                    } else if (escolha[0].equals("#PAUSA_CRONOS")) {
                        cronosPausado = true;
                        saida.writeUTF("PAUSADO");
                        saida.flush();
                    } else if (escolha[0].equals("#CONTINUA_CRONOS")) {
                        cronosPausado = false;
                        saida.writeUTF("CONTINUA");
                        saida.flush();
                    } else if (escolha[0].equals("#REINICIA_CRONO")) {
                        System.out.println("Reinicia");
                        fimCrono = false;
                        cronosPausado = false;
                        tempoLan = 24;
                        Label t = (Label) p.getScene().getRoot().lookup("#jLSeguraBola");
                        Label l = (Label) p.getScene().getRoot().lookup("#jLCronometroCentral");
                        String[] tempo = escolha[1].split("\\:");
                        String m;
                        String s;
                        int min = Integer.parseInt(tempo[0]);
                        int seg = Integer.parseInt(tempo[1]);

                        if (min > 9) {
                            m = "" + min;
                        } else {
                            m = "0" + min;
                        }

                        if (seg > 9) {
                            s = "" + seg;
                        } else {
                            s = "0" + seg;
                        }

                        Platform.runLater(() -> {
                            t.setText("24");
                            l.setText(m + ":" + s + ":" + "00");
                        });
                        saida.writeUTF("REINICIADO");
                        saida.flush();
                    } else if (escolha[0].equals("#REINICIA_TEMPO")) {
                        tempoLan = 24;
                        cronosPausado = false;
                        Platform.runLater(() -> {
                            Label l = (Label) p.getScene().getRoot().lookup("#jLSeguraBola");
                            l.setText("24");
                        });
                        saida.writeUTF("REINICIADO");
                        saida.flush();
                    } else if (escolha[0].equals("#MUDA_FALTA")) {
                        saida.writeUTF(mudaFalta(escolha));
                        saida.flush();
                    } else if (escolha[0].equals("#ALTERA_NOME")) {
                        Label local = (Label) p.getScene().getRoot().lookup("#jLTimeEsquerdo");
                        Label visitante = (Label) p.getScene().getRoot().lookup("#jLTimeDireito");
                        if (escolha.length > 0) {
                            if (escolha[1].equals("") != true) {
                                Platform.runLater(() -> {
                                    local.setText(escolha[1]);
                                    nomeLocal = escolha[1];
                                });
                            }
                            if (escolha[2].equals("") != true) {
                                Platform.runLater(() -> {
                                    visitante.setText(escolha[2]);
                                    nomeVisitante = escolha[2];
                                });
                            }
                        }
                        saida.writeUTF("ALTERADO");
                        saida.flush();
                    } else if (escolha[0].equals("#NOVA_RODADA")) {
                        Label local = (Label) p.getScene().getRoot().lookup("#jLRodada" + rodada);
                        if (rodada == 1) {

                            rodada++;

                            cronosPausado = false;
                            fimCrono = false;

                            somaRodadaL = pontosL;
                            somaRodadaV = pontosV;

                            if (somaRodadaL > 9 & somaRodadaV > 9) {

                                resultadoFinal = (somaRodadaL + " x " + somaRodadaV);

                            } else if (somaRodadaL < 9 & somaRodadaV < 9) {

                                resultadoFinal = ("0" + somaRodadaL + " x 0" + somaRodadaV);

                            } else if (somaRodadaL > 9 & somaRodadaV < 9) {

                                resultadoFinal = (somaRodadaL + " x 0" + somaRodadaV);

                            } else if (somaRodadaL < 9 & somaRodadaV > 9) {

                                resultadoFinal = ("0" + somaRodadaL + " x " + somaRodadaV);

                            }
                            Platform.runLater(() -> {
                                local.setText(resultadoFinal);
                            });

                        } else {

                            rodada++;

                            cronosPausado = false;
                            fimCrono = false;

                            somaRodadaL = pontosL - somaRodadaL;
                            somaRodadaV = pontosV - somaRodadaV;

                            if (somaRodadaL > 9 & somaRodadaV > 9) {

                                resultadoFinal = (somaRodadaL + " x " + somaRodadaV);

                            } else if (somaRodadaL < 9 & somaRodadaV < 9) {

                                resultadoFinal = ("0" + somaRodadaL + " x 0" + somaRodadaV);

                            } else if (somaRodadaL > 9 & somaRodadaV < 9) {

                                resultadoFinal = (somaRodadaL + " x 0" + somaRodadaV);

                            } else if (somaRodadaL < 9 & somaRodadaV > 9) {

                                resultadoFinal = ("0" + somaRodadaL + " x " + somaRodadaV);

                            }
                            Platform.runLater(() -> {
                                local.setText(resultadoFinal);
                            });
                            somaRodadaL = pontosL;
                            somaRodadaV = pontosV;
                        }
                        saida.writeUTF("INICIADA_RODADA");
                        saida.flush();

                    } else if (escolha[0].equals("#ESCOLHE_MODALIDADE")) {
                        saida.writeUTF(escolhaModalidade(escolha));
                        saida.flush();
                    } else if (escolha[0].equals("#ADICIONAR_USUARIO")) {
                        saida.writeUTF(adicionaUsuario(escolha));
                        saida.flush();
                    } else if (escolha[0].equals("#EXCLUIR_USUARIO")) {
                        saida.writeUTF(excluirUsuario(escolha));
                        saida.flush();
                    } else if (escolha[0].equals("#RESTAURA_TUDO")) {
                        saida.writeUTF(restauraTudo());
                        saida.flush();
                    } else if (escolha[0].equals("#LISTAR_USUARIOS")) {
                        saida.writeUTF(listaUsuario(escolha));
                        saida.flush();
                    } else if (escolha[0].equals("#DESCONECTAR")) {
                        count--;
                        saida.writeUTF("OK");
                        saida.flush();
                        entrada.close();
                        saida.close();
                        setDeslogar(user);
                        cliente.close();
                        System.out.println("Cliente se desconectou!");
                    } else if (escolha[0].equals("#QUAL_USER")) {
                        saida.writeUTF(user);
                        saida.flush();
                    } else if (escolha[0].equals("#PROPAGANDA_INICIA")) {
                        Main.propaganda(pontosL, pontosV, nomeLocal, nomeVisitante);
                        saida.writeUTF("INICIADA");
                        saida.flush();
                        // alteraDados();
                    } else if (escolha[0].equals("#PROPAGANDA_FECHA")) {
                        System.out.println("Fechando Propaganda");
                        saida.writeUTF("FECHADA");
                        saida.flush();
                        Platform.runLater(() -> {
                            StackPane pane = new StackPane();
                            Main.getStageSecundary().setScene(new Scene(pane, 10, 10));
                            Main.getStageSecundary().close();
                        });
                    } else if (escolha[0].equals("#ENVIAR_PROPAGANDA")) {
                        chamaTransferencia(escolha);
                        saida.writeUTF("ESPERANDO");
                        saida.flush();
                    } else if (escolha[0].equals("#LISTAR_PROPAGANDA")) {
                        saida.writeUTF(listaPropagandas(escolha));
                        saida.flush();
                    } else if (escolha[0].equals("#EXCLUIR_PROPAGANDA")) {
                        saida.writeUTF(excluirPropaganda(escolha));
                        saida.flush();
                    } else if (escolha[0].equals("#TROCA_TELA")) {
                        if (escolha[1].equals("PRINCIPAL")) {
                            Main.loadScene("/view/FXMLPrincipal.fxml");
                            saida.writeUTF("TROCADO");
                            saida.flush();
                        }
                    } else if (escolha[0].equals("#INICIA_CRONO_VOLEI")) {
                        saida.writeUTF(iniciaCronosPadrao());
                        saida.flush();
                    } else if (escolha[0].equals("#PROXIMO_SET")) {
                        saida.writeUTF(proximoSet());
                        saida.flush();
                    }
                }
            }
        };
        return task;
    }

    public void chamaTransferencia(String[] msg) throws IOException {
        Thread th = new Thread(armazenaPropaganda(msg[1]));
        th.setDaemon(true);
        th.start();
    }

    /**
     * Inicia tranferência de arquivos controlador-servidor
     *
     * @param msg
     * @return
     */
    private Task armazenaPropaganda(String msg) {

        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                ServerPropaganda sp = new ServerPropaganda();
                sp.receberArquivo(msg);
                return null;
            }
        };
        return task;
    }

    public String iniciaCronosPadrao() {
        if (fimCrono != true) {
            fimCrono = true;
            chamaCronosPadrao((Label) p.getScene().getRoot().lookup("#jLCronometroCentral"));
            return "CRONOS_INICIADO";

        } else {
            return "CRONOS_EXECUTANDO";
        }

    }

    public void chamaCronosPadrao(Label label) {
        Thread th = new Thread(iniciaCronosPadrao(label));
        th.setDaemon(true);
        th.start();
    }

    private Task iniciaCronosPadrao(Label l) {

        Task task = new Task<Void>() {
            int m = 0;
            int s = 0;
            int h = 0;
            String minutos;
            String segundos;
            String horas;

            @Override
            public Void call() throws Exception {
                while (fimCrono()) {

                    if (m > 9) {
                        minutos = "" + m;
                    } else {
                        minutos = "0" + m;
                    }
                    if (s > 9) {
                        segundos = "" + s;
                    } else {
                        segundos = "0" + s;
                    }
                    if (h > 9) {
                        horas = "" + h;
                    } else {
                        horas = "0" + h;
                    }

//                    while (cronosPausado) {
//                        Thread.sleep(100);
//                    }
//                    if (cronosPausado) {
//                        fimCrono = true;
//                        URL url = getClass().getResource("/estilos/apito.wav");
//                        AudioClip audio = Applet.newAudioClip(url);
//                        audio.play();
//                    }
                    Platform.runLater(() -> {
                        l.setText(horas + ":" + minutos + ":" + segundos);
                    });

                    Thread.sleep(1000);

                    s++;
                    if (s > 59) {
                        m++;
                        s = 0;
                    }
                    if (m > 59) {
                        h++;
                        m = 0;
                    }

                }
                return null;
            }
        };
        return task;
    }

    public String proximoSet() {
        int verificador = 0;
        int pL = pontosL;
        int pV = pontosV;
        verificador = pL - pV;
        if (verificador < 0) {
            verificador = verificador * (-1);
        }
        if (pL >= 25 || pV >= 25) {
            if (verificador >= 2) {
                if (pL > pV) {
                    setAutal++;
                    valorSetLocal++;
                    Label l = (Label) p.getScene().getRoot().lookup("#jLSetsLocal");
                    Label setLocal = (Label) p.getScene().getRoot().lookup("#jLSetLocal" + setAutal);
                    Label setVisitante = (Label) p.getScene().getRoot().lookup("#jLSetVisitante" + setAutal);
                    Platform.runLater(() -> {
                        l.setText("" + valorSetLocal);
                        if (pL < 9) {
                            setLocal.setText("0" + pL);
                        } else {
                            setLocal.setText("" + pL);
                        }
                        if (pV < 9) {
                            setVisitante.setText("0" + pV);
                        } else {
                            setVisitante.setText("" + pV);
                        }
                    });
                    zeraPlacar();
                    System.out.println(pL + " : " + pV);
                } else {
                    setAutal++;
                    valorSetVisitante++;
                    Label l = (Label) p.getScene().getRoot().lookup("#jLSetsLocal");
                    Label setLocal = (Label) p.getScene().getRoot().lookup("#jLSetLocal" + setAutal);
                    Label setVisitante = (Label) p.getScene().getRoot().lookup("#jLSetVisitante" + setAutal);
                    Platform.runLater(() -> {
                        l.setText("" + valorSetLocal);
                        if (pL < 9) {
                            setLocal.setText("0" + pL);
                        } else {
                            setLocal.setText("" + pL);
                        }
                        if (pV < 9) {
                            setVisitante.setText("0" + pV);
                        } else {
                            setVisitante.setText("" + pV);
                        }
                    });
                    zeraPlacar();
                    System.out.println(pL + " : " + pV);
                }
                if (setAutal == 3) {
                    int verificadorGanho = valorSetLocal - valorSetVisitante;
                    if (verificadorGanho < 0) {
                        verificadorGanho = verificadorGanho * (-1);
                    }
                    if (verificadorGanho == 3) {
                        if (valorSetLocal > valorSetVisitante) {
                            fimCrono = false;
                        } else {
                            fimCrono = false;
                        }
                        return "#ALGUEM_GANHO";
                    }

                }
                if (setAutal == 4) {
                    int verificadorGanho = valorSetLocal - valorSetVisitante;
                    if (verificadorGanho < 0) {
                        verificadorGanho = verificadorGanho * (-1);
                    }
                    if (verificadorGanho == 2) {
                        if (valorSetLocal > valorSetVisitante) {
                            fimCrono = false;
                        } else {
                            fimCrono = false;
                        }
                        return "#ALGUEM_GANHO";
                    }
                }
            }
        }
        if (setAutal == 5) {
            if (valorSetLocal > valorSetVisitante) {
                fimCrono = false;
            } else {
                fimCrono = false;
            }
            return "#ALGUEM_GANHO";
        }
        return "#NINGUEM_GANHOU";
    }

    public void zeraPlacar() {
        pontosL = 0;
        pontosV = 0;
        Label local = (Label) p.getScene().getRoot().lookup("#jLTimeDireitoPontos");
        Label visitante = (Label) p.getScene().getRoot().lookup("#jLTimeEsquerdoPontos");
        Platform.runLater(() -> {
            if (pontosL < 9) {
                local.setText("0" + pontosL);
            } else {
                local.setText("" + pontosL);
            }
            if (pontosV < 9) {
                visitante.setText("0" + pontosV);
            } else {
                visitante.setText("" + pontosV);
            }
        });
    }
}
