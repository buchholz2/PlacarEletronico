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
import java.util.List;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import main.Main;
import model.ListaUsuarios;

/**
 *
 * @author danie
 */
public class ComunicacaoSocketServidor implements Runnable {

    private boolean fimCrono = false;
    Stage p;
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

    public ComunicacaoSocketServidor(Stage p) {
        this.p = p;
    }

    public ComunicacaoSocketServidor() {

    }

    @Override
    public void run() {
        try {
            // Instancia o ServerSocket ouvindo a porta 12345
            ServerSocket servidor = new ServerSocket(12345);
            System.out.println("Servidor ouvindo a porta 12345");
            //System.out.println("" + p.getRoot().lookup("#jLCronometroCentral").getId());
            // o método accept() bloqueia a execução até que
            // o servidor receba um pedido de conexão
            Socket cliente = servidor.accept();

            ProgressIndicator pro = (ProgressIndicator) p.getScene().getRoot().lookup("#progressIndicator");
            pro.setOpacity(0);

            System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());

            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());

            while (true) {

                String msg = entrada.readUTF();

                String[] escolha = msg.split("\\$");

                if (escolha[0].equals("#TIME")) {
                    saida.writeUTF(time(escolha));
                    saida.flush();
                } else if (escolha[0].equals("#LOGIN")) {
                    saida.writeUTF(login(escolha));
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
                            });
                        }
                        if (escolha[2].equals("") != true) {
                            Platform.runLater(() -> {
                                visitante.setText(escolha[2]);
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
                }
            }

        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

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

    public String login(String[] msg) throws IOException {
        String login = msg[1];
        String senha = msg[2];
        Usuario usuario = new Usuario();
        if (validaLogin(login, senha, usuario)) {
            System.out.println("verificou usuario e senha");
            if (usuario.isUserAdm()) {
                return ("#LOGADO$ADM");
            } else if (usuario.isUserPlacar()) {
                System.out.println("é usuario placar");
                System.out.println("logado - aguarda escolha modalidade");
                return ("#LOGADO$PLACAR");
            } else if (usuario.isUserPropaganda()) {
                return ("#LOGADO$PROPAGANDA");
            } else {
                return ("#NOT$LOGADO");
            }
        }
        return ("#NOT$LOGADO");

    }

    private boolean validaLogin(String login, String senha, Usuario usuario) {
        if (login == null || senha == null) {
            return false;
        } else {
            //  ComunicacaoSocketServidor c = new ComunicacaoSocketServidor();
            ListaUsuarios users = leituraXML();

            for (Usuario u : users.getUsuarios()) {
                if (login.equals(u.getUsuario()) && senha.equals(u.getSenha())) {
                    usuario.setUsuario(u.getUsuario());
                    usuario.setSenha(u.getSenha());
                    usuario.setUserAdm(u.isUserAdm());
                    usuario.setUserPlacar(u.isUserPlacar());
                    usuario.setUserPropaganda(u.isUserPropaganda());

                    return true;
                }
            }
        }

        return false;
    }

    private String escolhaModalidade(String[] msg) {
        String opcao = msg[1];
        String retorno = "nada feito";
        switch (opcao) {
            case "BASQUETE":
                Main.loadScene("/view/FXMLBasquete.fxml");
                retorno = "ESCOLHIDA";
                break;

            case "VOLEI":
                Main.loadScene("/view/FXMLBasquete.fxml");
                retorno = "ESCOLHIDA";
                break;

            case "FUTSAL":
                Main.loadScene("/view/FXMLBasquete.fxml");
                retorno = "ESCOLHIDA";
                break;
        }

        return retorno;
    }

    private String adicionaUsuario(String[] msg) {
        String opcao = msg[1];
        List<Usuario> l = new ArrayList<>();
        ListaUsuarios lista = leituraXML();

        if (opcao.equals("ADMINISTRADOR")) {
            lista.getUsuarios().forEach((u) -> {
                l.add(u);
            });
            Usuario adm = new Usuario();
            adm.setUserAdm(true);
            adm.setUserPlacar(false);
            adm.setUserPropaganda(false);
            adm.setUsuario(msg[2]);
            adm.setSenha(msg[3]);
            l.add(adm);
            lista.setUsuarios(l);
            gravarXML(lista);
            return ("#OK");
        } else if (opcao.equals("PLACAR")) {
            lista.getUsuarios().forEach((u) -> {
                l.add(u);
            });
            Usuario placar = new Usuario();
            placar.setUserAdm(false);
            placar.setUserPlacar(true);
            placar.setUserPropaganda(false);
            placar.setUsuario(msg[2]);
            placar.setSenha(msg[3]);
            l.add(placar);
            lista.setUsuarios(l);
            gravarXML(lista);
            return ("#OK");
        } else if (opcao.equals("PROPAGANDA")) {
            lista.getUsuarios().forEach((u) -> {
                l.add(u);
            });
            Usuario propaganda = new Usuario();
            propaganda.setUserAdm(false);
            propaganda.setUserPlacar(false);
            propaganda.setUserPropaganda(true);
            propaganda.setUsuario(msg[2]);
            propaganda.setSenha(msg[3]);
            l.add(propaganda);
            lista.setUsuarios(l);
            gravarXML(lista);
            return ("#OK");
        } else {
            return ("#NOT$OK");
        }
    }

    private String excluirUsuario(String[] msg) {

        return ("#NOT$OK");
    }

    public String fechaConexao(String[] msg) {
        return "";
    }

    public String iniciaCronos(String[] msg) {
        if (fimCrono != true) {
            fimCrono = true;
            System.out.println("Chego no corte");
            String[] msm = msg[1].split("\\:");
            if (msm.length > 1) {
                int minutos = Integer.parseInt(msm[0]);
                int segundos = Integer.parseInt(msm[1]);
                chamaCronos((Label) p.getScene().getRoot().lookup("#jLCronometroCentral"), minutos, segundos, 0);
                return "CRONOS_INICIADO";
            } else {
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

    public ListaUsuarios leituraXML() {
        ListaUsuarios lista = null;
        try {
            File file = new File("src/xml/usuarios.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(ListaUsuarios.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            lista = (ListaUsuarios) jaxbUnmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void gravarXML(ListaUsuarios l) {
        try {

            File file = new File("src/xml/usuarios.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(ListaUsuarios.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(l, file);
            // Se desejar mostrar no console o xml gerado
//            jaxbMarshaller.marshal(usuarios, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

}
