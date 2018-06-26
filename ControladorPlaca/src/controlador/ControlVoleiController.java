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

    private int pontosV = 0;
    private int pontosL = 0;
    private boolean fimCrono = false;
    private boolean cronosPausado = false;
    private int setAutal;
    private int valorSetLocal;
    private int valorSetVisitante;

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
                if (Main.mandaMSG("#ALTERA_NOME$" + jTFAlteraNomeLocal.getText() + "$" + jTFAlteraNomeVisitante.getText()).equals("ALTERADO")) {
                    Platform.runLater(() -> {
                        jLNomeLocal.setText(jTFAlteraNomeLocal.getText());
                        jLNomeVisitante.setText(jTFAlteraNomeVisitante.getText());
                    });
                }
            } else {
                if (Main.mandaMSG("#ALTERA_NOME$" + "LOCAL" + "$" + "VISITANTE").equals("ALTERADO")) {
                    Platform.runLater(() -> {
                        jLNomeVisitante.setText("VISITANTE");
                        jLNomeLocal.setText("LOCAL");
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
            String retorno = Main.mandaMSG("#INICIA_CRONO_VOLEI");
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
                Main.mandaMSG("#PROPAGANDA_INICIA");
            } else {
                Main.mandaMSG("#PROPAGANDA_FECHA");
            }
        } catch (IOException ex) {
            Logger.getLogger(ControlBasqueteController.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (IOException ex) {
            Logger.getLogger(ControlBasqueteController.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ControlBasqueteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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

    public String proximoSet() {
        int verificador = 0;
        verificador = pontosL - pontosV;
        if (verificador < 0) {
            verificador = verificador * (-1);
        }
        if (pontosL >= 25 || pontosV >= 25) {

            if (verificador >= 2) {
                if (pontosL > pontosV) {
                    setAutal++;
                    valorSetLocal++;
                    Label setLocal;
                    Label setVisitante;

                    switch (setAutal) {
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
                    zeraPlacar();
                    System.out.println(pontosL + " : " + pontosV);
                } else {
                    setAutal++;
                    valorSetVisitante++;
                    Label setLocal;
                    Label setVisitante;

                    switch (setAutal) {
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
                    zeraPlacar();
                    System.out.println(pontosL + " : " + pontosV);
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

            if (setAutal == 5) {
                if (valorSetLocal > valorSetVisitante) {
                    fimCrono = false;
                } else {
                    fimCrono = false;
                }
                return "#ALGUEM_GANHO";
            }
        }
        return "#NINGUEM_GANHOU";
    }

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

    @FXML
    void trocaPosse(MouseEvent event) {

    }
}
