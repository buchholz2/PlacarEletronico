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
public class ControlPadraoController implements Initializable {

    @FXML
    private Label jLCronometro;

    @FXML
    private Label jLPontosLocal;

    @FXML
    private Label jLPontosVisitante;

    @FXML
    private Label jLFaltasVisitante;

    @FXML
    private Label jLFaltasLocal;

    @FXML
    private Label jLNomeLocal;

    @FXML
    private Label jLNomeVisitante;

    @FXML
    private Label jLPeriodo;

    @FXML
    private Label jLAcrescimo;

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
    private Button jBMaisFaltaL;

    @FXML
    private Button jBMenosFaltaL;

    @FXML
    private Button jBMaisFaltaV;

    @FXML
    private Button jBMenosFaltaV;

    @FXML
    private Button jBNovoQuarto;

    @FXML
    private Label jLLogger;

    @FXML
    private Button jBIniciaCrono;

    @FXML
    private Button jBReiniciaCrono;

    @FXML
    private Button jBDefineCrono;

    @FXML
    private ToggleButton jTBPausaCrono;

    @FXML
    private TextField jTFValorAcrescimo;

    @FXML
    private Button jBRestauraTudo;

    @FXML
    private ToggleButton jTBIniciaProp;

    @FXML
    private Button jBVoltar;

    @FXML
    private TextField jTFValorLimiteCrono;

    @FXML
    private Button jBLimitaTempo;

    private boolean cronosPausado = false;
    private int pontosV = 0;
    private int pontosL = 0;
    private boolean fimCrono = false;
    private int faltasV = 0;
    private int faltasL = 0;

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
                Main.mandaMSG("#PROPAGANDA_INICIA_PADRAO");
            } else {
                Main.mandaMSG("#PROPAGANDA_FECHA");
            }
        } catch (IOException ex) {
            Logger.getLogger(ControlBasqueteController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Ação soma falta time local. Envia comanto para servidor, Aguarda retorno
     * para atualizar preview
     *
     * @param event
     */
    @FXML
    void maisFaltaL(MouseEvent event) {
        try {
            if (Main.mandaMSG("#MUDA_FALTA$LOCAL$SOMA").equals("MUDADO")) {
                mudaFalta("LOCAL", "SOMA");
            }
        } catch (IOException ex) {
            //IMPLEMENTAR LOG
        }
    }

    /**
     * Ação soma falta time visitante. Envia comanto para servidor, Aguarda
     * retorno para atualizar preview
     *
     * @param event
     */
    @FXML
    void maisFaltaV(MouseEvent event) {
        try {
            if (Main.mandaMSG("#MUDA_FALTA$VISITANTE$SOMA").equals("MUDADO")) {
                mudaFalta("VISITANTE", "SOMA");
            }
        } catch (IOException ex) {
            //IMPLEMENTAR LOG
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
            //IMPLEMENTAR LOG
        }
    }

    /**
     * Ação diminui falta time local. Envia comanto para servidor, Aguarda
     * retorno para atualizar preview
     *
     * @param event
     */
    @FXML
    void menosFaltaL(MouseEvent event) {
        try {
            if (Main.mandaMSG("#MUDA_FALTA$LOCAL$SUB").equals("MUDADO")) {
                mudaFalta("LOCAL", "SUB");
            }
        } catch (IOException ex) {
            //IMPLEMENTAR LOG
        }
    }

    /**
     * Ação diminui falta time visitante. Envia comanto para servidor, Aguarda
     * retorno para atualizar preview
     *
     * @param event
     */
    @FXML
    void menosFaltaV(MouseEvent event) {
        try {
            if (Main.mandaMSG("#MUDA_FALTA$VISITANTE$SUB").equals("MUDADO")) {
                mudaFalta("VISITANTE", "SUB");
            }
        } catch (IOException ex) {
            //IMPLEMENTAR LOG
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
     * Ação para iniciar um novo periodo na partida. Envia comanto para
     * servidor, Aguarda retorno para atualizar preview
     *
     * @param event
     */
    @FXML
    void novoPeriodo(MouseEvent event) {

    }

    /**
     * Ação de pausar o cronometro. Envia comanto para servidor, Aguarda retorno
     * para atualizar preview
     *
     * @param event
     */
    @FXML
    void pausaCrono(MouseEvent event) {
        try {
            if (jTBPausaCrono.isSelected()) {
                if (Main.mandaMSG("#PAUSA_CRONOS").equals("PAUSADO")) {
                    cronosPausado = true;
                }
            } else {
                if (Main.mandaMSG("#CONTINUA_CRONOS").equals("CONTINUA")) {
                    cronosPausado = false;
                }
            }
        } catch (IOException ex) {
            //IMPLEMENTAR LOG
        }
    }

    /**
     * Ação de reiniciar o cronometro que esta pausado. Envia comanto para
     * servidor, Aguarda retorno para atualizar preview
     *
     * @param event
     * @throws InterruptedException
     */
    @FXML
    void reiniciaCrono(MouseEvent event) {
        try {
            Main.mandaMSG("#REINICIA_CRONO_PADRAO");
            cronosPausado = false;
            fimCrono = false;
            Platform.runLater(() -> {
                jLCronometro.setText("00:00:00");
            });
        } catch (IOException ex) {
            Logger.getLogger(ControlPadraoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Ação de restaurar todos os elementos do painel contradolor padrão.
     * Envia comanto para servidor, Aguarda retorno para atualizar preview
     *
     * @param event
     */
    @FXML
    void restauraTudo(MouseEvent event) {
        cronosPausado = false;
        pontosV = 0;
        pontosL = 0;
        fimCrono = false;
        faltasV = 0;
        faltasL = 0;
        Platform.runLater(() -> {
            jLAcrescimo.setOpacity(0);
            jLCronometro.setText("00:00:00");
            jLFaltasLocal.setText("00");
            jLFaltasVisitante.setText("00");
            jLNomeLocal.setText("LOCAL");
            jLNomeVisitante.setText("VISITANTE");
            jLPeriodo.setText("0");
            jLPontosLocal.setText("00");
            jLPontosVisitante.setText("00");
        });
    }

    /**
     * Ação para somar acrescimo ao tempo. Envia comanto para servidor, Aguarda
     * retorno para atualizar preview
     *
     * @param event
     */
    @FXML
    void somaArescimo(MouseEvent event) {

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
            Logger.getLogger(ControlBasqueteController.class
                    .getName()).log(Level.SEVERE, null, ex);
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
        jLAcrescimo.setOpacity(0);
    }
    
    /**
     * Ação de mudar faltas dos times. 
     * @param time
     * @param fun 
     */
    private void mudaFalta(String time, String fun) {
        if (time.equals("LOCAL")) {
            if (fun.equals("SOMA")) {
                faltasL++;
            } else {
                faltasL--;
            }
            if (faltasL < 0) {
                faltasL = 0;
            }

            if (faltasL > 9) {
                Platform.runLater(() -> {
                    jLFaltasLocal.setText("" + faltasL);
                });
            } else {
                Platform.runLater(() -> {
                    jLFaltasLocal.setText("0" + faltasL);
                });
            }

        } else {
            if (fun.equals("SOMA")) {
                faltasV++;
            } else {
                faltasV--;
            }
            if (faltasV < 0) {
                faltasV = 0;
            }

            if (faltasV > 9) {
                Platform.runLater(() -> {
                    jLFaltasVisitante.setText("" + faltasV);
                });
            } else {
                Platform.runLater(() -> {
                    jLFaltasVisitante.setText("0" + faltasV);
                });
            }
        }
    }

    /**
     * 
     * Inicialiaza o cronometro na preview placar padrão
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

                    while (cronosPausado) {

                        Thread.sleep(100);
                    }

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

    /**
     * Retorna se o cronometro está encerrado
     * @return 
     */
    public boolean fimCrono() {
        return fimCrono;
    }

    @FXML
    void limitarTempo(MouseEvent event) {

    }
}
