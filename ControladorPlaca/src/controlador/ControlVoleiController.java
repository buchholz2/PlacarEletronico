/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import main.Main;

/**
 * FXML Controller class
 *
 * @author Cristiano Künas
 * @author Daniel Buchholz
 * @author Douglas Hoffmann
 * @author Leandro Heck
 */
public class ControlVoleiController implements Initializable {

    @FXML
    private Label jLCronometro;

    @FXML
    private Label jLPontosLocal;

    @FXML
    private Label jLPontosVisitante;

    @FXML
    private Label jLNomeLocal;

    @FXML
    private Label jLNomeVisitante;

    @FXML
    private Label jLNomeLocalSets;

    @FXML
    private Label jLNomeVisitanteSets;

    @FXML
    private Label jLSetsLocal;

    @FXML
    private Label jLSetsVisitante;

    @FXML
    private Label jLLocalSet1;

    @FXML
    private Label jLVisitanteSet1;

    @FXML
    private Label jLLocalSet2;

    @FXML
    private Label jLVisitanteSet2;

    @FXML
    private Label jLLocalSet3;

    @FXML
    private Label jLVisitanteSet3;

    @FXML
    private Label jLLocalSet4;

    @FXML
    private Label jLVisitanteSet4;

    @FXML
    private Label jLLocalSet5;

    @FXML
    private Label jLVisitanteSet5;

    @FXML
    private Button jBAlteraNome;

    @FXML
    private Button jBMaisUmPontosL;

    @FXML
    private Button jBMenosUmPontosL;

    @FXML
    private TextField jTFAlteraNomeLocal;

    @FXML
    private TextField jTFAlteraNomeVisitante;

    @FXML
    private Button jBMaisUmPontosV;

    @FXML
    private Button jBMenosUmPontosV;

    @FXML
    private Button jBProximoSet;

    @FXML
    private Label jLLogger;

    @FXML
    private Button jBIniciaCrono;

    @FXML
    private Button jBRestauraTudo;

    @FXML
    private ToggleButton jTBIniciaProp;

    @FXML
    private Button jBVoltar;

    @FXML
    private Button jBTrocaPosse;

    @FXML
    private Label jLPosseLocal;

    @FXML
    private Label jLPosseVisitante;

    private int pontosV = 0;
    private int pontosL = 0;
    private boolean fimCrono = false;
    private int setAtual = 1;
    private int valorSetLocal;
    private int valorSetVisitante;
    private boolean control = true;

    /**
     * Evento do botão alterar nomes. Verifica se camposestão vazios. Envia
     * comando altera_nome para o servidor Aguarda retorno para atualizar placar
     * no preview do controlador
     *
     * @param event
     */
    @FXML
    void alteraNomes(MouseEvent event) {
        try {
            if (jTFAlteraNomeLocal.getText().equals("") != true && jTFAlteraNomeVisitante.getText().equals("") != true) {
                if (Main.mandaMSG("#ALTERA_NOME_VOLEI$" + jTFAlteraNomeLocal.getText() + "$" + jTFAlteraNomeVisitante.getText()).equals("#ALTERADO")) {
                    Platform.runLater(() -> {
                        jLNomeLocal.setText(jTFAlteraNomeLocal.getText());
                        jLNomeVisitante.setText(jTFAlteraNomeVisitante.getText());
                        jLNomeLocalSets.setText(jTFAlteraNomeLocal.getText());
                        jLNomeVisitanteSets.setText(jTFAlteraNomeVisitante.getText());
                    });
                }
            } else {
                if (Main.mandaMSG("#ALTERA_NOME$" + "LOCAL" + "$" + "VISITANTE").equals("#ALTERADO")) {
                    Platform.runLater(() -> {
                        jLNomeVisitante.setText("VISITANTE");
                        jLNomeLocal.setText("LOCAL");
                        jLNomeVisitanteSets.setText("VISITANTE");
                        jLNomeLocalSets.setText("LOCAL");
                    });
                }
            }
        } catch (IOException ex) {
            //IMPLEMENTAR LOG
        }
    }

    /**
     * Ação iniciar cronômetro. Envia comanto para servidor, Aguarda retorno
     * para atualizar preview
     *
     * @param event
     */
    @FXML
    void iniciaCrono(MouseEvent event) {
        try {
            String retorno = Main.mandaMSG("#INICIA_CRONO_PADRAO");
            if (retorno.equals("CRONOS_INICIADO")) {
                fimCrono = true;
                Thread th = new Thread(iniciaCronosPreview(jLCronometro));
                th.setDaemon(true);
                th.start();
            }
        } catch (IOException ex) {
            //IMPLEMENTAR LOG
        }
    }

    /**
     * Ação iniciar propaganda. Envia comanto para servidor, Aguarda retorno
     * para atualizar preview
     *
     * @param event
     */
    @FXML
    void iniciaPropaganda(MouseEvent event) {
        try {
            if (jTBIniciaProp.isSelected()) {
                Main.mandaMSG("#PROPAGANDA_INICIA_VOLEI");
            } else {
                Main.mandaMSG("#PROPAGANDA_FECHA");
            }
        } catch (IOException ex) {

        }
    }

    /**
     * Ação soma mais um ponto time local. Envia comanto para servidor, Aguarda
     * retorno para atualizar preview
     *
     * @param event
     */
    @FXML
    void maisUmL(MouseEvent event) {
        try {
            String retorno = Main.mandaMSG("#TIME$LOCAL$SOMA_PONTO$UM$");
            if (retorno.equals("#OK")) {
                pontosL = pontosL + 1;
                if (pontosL > 9) {
                    jLPontosLocal.setText("" + pontosL);
                } else {
                    jLPontosLocal.setText("0" + pontosL);
                }

            }
            if (pontosL >= 25) {
                Main.mandaMSG("#PROXIMO_SET");
                proximoSet();
            }

            if (setAtual == 5 && pontosL >= 15) {
                Main.mandaMSG("#PROXIMO_SET");
                proximoSet();
            }
        } catch (IOException ex) {
            //IMPLEMENTAR LOG
        }
    }

    /**
     * Ação soma mais um ponto time visitante. Envia comanto para servidor,
     * Aguarda retorno para atualizar preview
     *
     * @param event
     */
    @FXML
    void maisUmV(MouseEvent event) {
        try {
            String retorno = Main.mandaMSG("#TIME$VISITANTE$SOMA_PONTO$UM$");
            if (retorno.equals("#OK")) {
                pontosV = pontosV + 1;
                if (pontosV > 9) {
                    jLPontosVisitante.setText("" + pontosV);
                } else {
                    jLPontosVisitante.setText("0" + pontosV);
                }

            }
            if (pontosV >= 25) {
                Main.mandaMSG("#PROXIMO_SET");
                proximoSet();
            }
            if (setAtual == 5 && pontosV >= 15) {
                Main.mandaMSG("#PROXIMO_SET");
                proximoSet();
            }
        } catch (IOException ex) {
            Logger.getLogger(ControlVoleiController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Ação subtrai menos um ponto time local. Envia comanto para servidor,
     * Aguarda retorno para atualizar preview
     *
     * @param event
     */
    @FXML
    void menosUmL(MouseEvent event) {
        try {
            String retorno = Main.mandaMSG("#TIME$LOCAL$SUB_PONTO$UM$");
            if (retorno.equals("#OK")) {
                pontosL = pontosL - 1;
                if (pontosL < 0) {
                    pontosL = 0;
                }
                if (pontosL > 9) {
                    jLPontosLocal.setText("" + pontosL);
                } else {
                    jLPontosLocal.setText("0" + pontosL);
                }
            }
        } catch (IOException ex) {
            //IMPLEMENTAR LOG
        }
    }

    /**
     * Ação subtrai menos um ponto time visitante. Envia comanto para servidor,
     * Aguarda retorno para atualizar preview
     *
     * @param event
     */
    @FXML
    void menosUmV(MouseEvent event) {
        try {
            String retorno = Main.mandaMSG("#TIME$VISITANTE$SUB_PONTO$UM$");
            if (retorno.equals("#OK")) {
                pontosV = pontosV - 1;
                if (pontosV < 0) {
                    pontosV = 0;
                }
                if (pontosV > 9) {
                    jLPontosVisitante.setText("" + pontosV);
                } else {
                    jLPontosVisitante.setText("0" + pontosV);
                }
            }
        } catch (IOException ex) {
            //IMPLEMENTAR LOG
        }
    }

    /**
     * Ação para iniciar um novo set na partida. Envia comanto para servidor,
     * Aguarda retorno para atualizar preview
     *
     * @param event
     */
    @FXML
    void proximoSet(MouseEvent event) {
        try {
            Main.mandaMSG("#PROXIMO_SET");
            proximoSet();
        } catch (IOException ex) {
            Logger.getLogger(ControlVoleiController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Ação de restaurar todos os elementos do painel contradolor basquete.
     * Envia comanto para servidor, Aguarda retorno para atualizar preview
     *
     * @param event
     */
    @FXML
    void restauraTudo(MouseEvent event) {
        try {
            if (Main.mandaMSG("#RESTAURA_TUDO_VOLEI").equals("RESTAURADO")) {
                Platform.runLater(() -> {
                    jLNomeLocal.setText("LOCAL");
                    jLNomeVisitante.setText("VISITANTE");
                    jLNomeLocalSets.setText("LOCAL");
                    jLNomeVisitanteSets.setText("VISITANTE");
                    jLPontosLocal.setText("00");
                    jLPontosVisitante.setText("00");
                    jLLocalSet1.setText("-");
                    jLLocalSet2.setText("-");
                    jLLocalSet3.setText("-");
                    jLLocalSet4.setText("-");
                    jLLocalSet5.setText("-");
                    jLVisitanteSet1.setText("-");
                    jLVisitanteSet2.setText("-");
                    jLVisitanteSet3.setText("-");
                    jLVisitanteSet4.setText("-");
                    jLVisitanteSet5.setText("-");
                    jLSetsLocal.setText("0");
                    jLSetsVisitante.setText("0");
                    jLCronometro.setText("00:00:00");
                    jLPosseLocal.setOpacity(1);
                    jLPosseVisitante.setOpacity(0);
                });
                jBTrocaPosse.setDisable(false);
                jBAlteraNome.setDisable(false);
                jBIniciaCrono.setDisable(false);
                jBMaisUmPontosL.setDisable(false);
                jBMaisUmPontosV.setDisable(false);
                jBMenosUmPontosL.setDisable(false);
                jBMenosUmPontosV.setDisable(false);
                jBProximoSet.setDisable(false);
                control = true;
                fimCrono = false;
                setAtual = 1;
                valorSetLocal = 0;
                valorSetVisitante = 0;
                pontosL = 0;
                pontosV = 0;
            }
        } catch (IOException ex) {
            Logger.getLogger(ControlVoleiController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Ação para voltar a tela de escolha de modalidade. Envia comanto para
     * servidor, Aguarda retorno para atualizar preview
     *
     * @param event
     */
    @FXML
    void voltarEscolhaModalidade(MouseEvent event) {
        try {
            Main.mandaMSG("#TROCA_TELA$PRINCIPAL");
            if (jTBIniciaProp.isSelected()) {

            } else {
                Main.mandaMSG("#PROPAGANDA_FECHA");
            }
        } catch (IOException ex) {
        }
        Main.loadScene("/view/FXMLEscolheModalidade.fxml");
    }

    /**
     * Inicializar
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Date date = new Date();
            jLLogger.setText(Main.mandaMSG("#QUAL_USER") + " : " + date.toGMTString());
        } catch (IOException ex) {

        }
        jLPosseVisitante.setOpacity(0);
    }

    /**
     *
     * Inicia O cronometro na preview volei
     *
     * @param l
     * @return
     */
    private Task iniciaCronosPreview(Label l) {

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

    public boolean fimCrono() {
        return fimCrono;
    }

    /**
     * Verifica que é o vencedor do SET atual
     *
     * @return
     */
    public String chamaGanho() {
        System.out.println("QUEM CHAMOU GANHO" + setAtual);
        int verificador = 0;
        verificador = pontosL - pontosV;
        if (verificador < 0) {
            verificador = verificador * (-1);
        }

        if (pontosL >= 15 || pontosV >= 15) {
            if (setAtual == 5) {
                if (verificador >= 2) {
                    System.out.println("2 chamaGanh");
                    if (pontosL > pontosV) {
                        fimCrono = false;
                    } else {
                        fimCrono = false;
                    }
                    URL url = getClass().getResource("/estilos/apito.wav");
                    AudioClip audio = Applet.newAudioClip(url);
                    audio.play();
                    encerraPartida();
                    return "#ALGUEM_GANHOU";
                }
            }
        }

        if (pontosL >= 25 || pontosV >= 25) {
            System.out.println("Pontos 25 " + setAtual);
            System.out.println("PONTOS: " + pontosL + " = " + pontosV);
            if (setAtual == 3) {
                System.out.println("3 chama ganho");
                int verificadorGanho = valorSetLocal - valorSetVisitante;
                if (verificadorGanho < 0) {
                    verificadorGanho = verificadorGanho * (-1);
                }
                System.out.println("Verificador Ganho 3" + verificadorGanho);
                if (verificadorGanho == 3) {
                    if (valorSetLocal > valorSetVisitante) {
                        fimCrono = false;
                    } else {
                        fimCrono = false;
                    }
                    System.out.println("entrou ganho 3");
                    URL url = getClass().getResource("/estilos/apito.wav");
                    AudioClip audio = Applet.newAudioClip(url);
                    audio.play();
                    encerraPartida();
                    return "#ALGUEM_GANHO";
                }
            }
            if (setAtual == 4) {
                System.out.println("4 chama ganho");
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
                    URL url = getClass().getResource("/estilos/apito.wav");
                    AudioClip audio = Applet.newAudioClip(url);
                    audio.play();
                    encerraPartida();
                    return "#ALGUEM_GANHO";
                }
            }
        }
        zeraPlacar();
        return "#NINGUEM_GANHO";
    }

    /**
     * Método para realizar o inicio de um novo SET. Seta a pontuação do set
     * Atual nos labels inferiores
     */
    private void proximoSet() {

        int verificador = 0;
        verificador = pontosL - pontosV;
        if (verificador < 0) {
            verificador = verificador * (-1);
        }
        if (setAtual == 5 && (pontosL >= 15 || pontosV >= 15)) {
            if (verificador >= 2) {
                if (pontosL > pontosV) {
                    valorSetLocal++;
                    jLSetsLocal.setText("" + valorSetLocal);
                    if (pontosL < 9) {
                        jLLocalSet5.setText("0" + pontosL);
                    } else {
                        jLLocalSet5.setText("" + pontosL);
                    }
                    if (pontosV < 9) {
                        jLVisitanteSet5.setText("0" + pontosV);
                    } else {
                        jLVisitanteSet5.setText("" + pontosV);
                    }
                    chamaGanho();
                    setAtual++;
                    System.out.println(pontosL + " : " + pontosV);
                } else {
                    jLSetsVisitante.setText("" + valorSetVisitante);
                    if (pontosL < 9) {
                        jLLocalSet5.setText("0" + pontosL);
                    } else {
                        jLLocalSet5.setText("" + pontosL);
                    }
                    if (pontosV < 9) {
                        jLVisitanteSet5.setText("0" + pontosV);
                    } else {
                        jLVisitanteSet5.setText("" + pontosV);
                    }

                    System.out.println(pontosL + " : " + pontosV);
                    chamaGanho();
                    setAtual++;
                }
            }
        }

        if (pontosL >= 25 || pontosV >= 25) {
            if (verificador >= 2) {
                if (pontosL > pontosV) {
                    valorSetLocal++;
                    Label setLocal;
                    Label setVisitante;
                    switch (setAtual) {
                        case 1:
                            setLocal = jLLocalSet1;
                            setVisitante = jLVisitanteSet1;
                            break;
                        case 2:
                            setLocal = jLLocalSet2;
                            setVisitante = jLVisitanteSet2;
                            break;
                        case 3:
                            setLocal = jLLocalSet3;
                            setVisitante = jLVisitanteSet3;
                            break;
                        case 4:
                            setLocal = jLLocalSet4;
                            setVisitante = jLVisitanteSet4;
                            break;
                        default:
                            setLocal = jLLocalSet5;
                            setVisitante = jLVisitanteSet5;
                            break;
                    }
                    System.out.println(setAtual);

                    jLSetsLocal.setText("" + valorSetLocal);
                    if (pontosL < 9) {
                        setLocal.setText("0" + pontosL);
                    } else {
                        setLocal.setText("" + pontosL);
                    }
                    if (pontosV < 9) {
                        setVisitante.setText("0" + pontosV);
                    } else {
                        setVisitante.setText("" + pontosV);
                    }
                    chamaGanho();
                    setAtual++;
                    System.out.println(pontosL + " : " + pontosV);
                } else {
                    valorSetVisitante++;
                    Label setLocal;
                    Label setVisitante;

                    switch (setAtual) {
                        case 1:
                            setLocal = jLLocalSet1;
                            setVisitante = jLVisitanteSet1;
                            break;
                        case 2:
                            setLocal = jLLocalSet2;
                            setVisitante = jLVisitanteSet2;
                            break;
                        case 3:
                            setLocal = jLLocalSet3;
                            setVisitante = jLVisitanteSet3;
                            break;
                        case 4:
                            setLocal = jLLocalSet4;
                            setVisitante = jLVisitanteSet4;
                            break;
                        default:
                            setLocal = jLLocalSet5;
                            setVisitante = jLVisitanteSet5;
                            break;
                    }
                    System.out.println(setAtual);
                    jLSetsVisitante.setText("" + valorSetVisitante);
                    if (pontosL < 9) {
                        setLocal.setText("0" + pontosL);
                    } else {
                        setLocal.setText("" + pontosL);
                    }
                    if (pontosV < 9) {
                        setVisitante.setText("0" + pontosV);
                    } else {
                        setVisitante.setText("" + pontosV);
                    }
                    chamaGanho();
                    setAtual++;
                    System.out.println(pontosL + " : " + pontosV);

                }
            }
            System.out.println("Somou o setAtual" + setAtual);
        }
    }

    /**
     * Zera os pontos dos times a cada novo SET. Envia comanto para servidor,
     * Aguarda retorno para atualizar preview
     */
    public void zeraPlacar() {
        pontosL = 0;
        pontosV = 0;
        Platform.runLater(() -> {
            if (pontosL < 9) {
                jLPontosLocal.setText("0" + pontosL);
            } else {
                jLPontosLocal.setText("" + pontosL);
            }
            if (pontosV < 9) {
                jLPontosVisitante.setText("0" + pontosV);
            } else {
                jLPontosVisitante.setText("" + pontosV);
            }
        });
    }

    /**
     * Ação de trocr a posse da bola. Envia comanto para servidor, Aguarda
     * retorno para atualizar preview
     *
     * @param event
     */
    @FXML
    void trocaPosse(MouseEvent event) {
        try {
            if (Main.mandaMSG("#TROCA_POSSE").equals("#TROCADO")) {
                if (control) {
                    jLPosseVisitante.setOpacity(1);
                    jLPosseLocal.setOpacity(0);
                    control = false;
                } else {
                    jLPosseVisitante.setOpacity(0);
                    jLPosseLocal.setOpacity(1);
                    control = true;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ControlVoleiController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método chamado quando um time vence. Desativa os botões
     */
    private void encerraPartida() {
        jBTrocaPosse.setDisable(true);
        jBAlteraNome.setDisable(true);
        jBIniciaCrono.setDisable(true);
        jBMaisUmPontosL.setDisable(true);
        jBMaisUmPontosV.setDisable(true);
        jBMenosUmPontosL.setDisable(true);
        jBMenosUmPontosV.setDisable(true);
        jBProximoSet.setDisable(true);
        zeraPlacar();
    }
}
