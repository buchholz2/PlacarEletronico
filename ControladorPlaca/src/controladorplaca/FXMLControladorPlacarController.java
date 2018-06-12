package controladorplaca;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import main.Main;

/**
 * @author Cristiano Künas
 * @author Daniel Buchholz
 * @author Douglas Hoffmann
 * @author Leandro Heck
 */
public class FXMLControladorPlacarController implements Initializable {

    private boolean fimCrono = true;
    private int pontosV = 0;
    private int pontosL = 0;
    private boolean cronosPausado = false;
    private int tempoLan = 24;
    private int muda = 60;
    private int faltasV = 0;
    private int faltasL = 0;
    private int rodada = 1;
    private int somaRodadaL = 0;
    private int somaRodadaV = 0;

    @FXML
    private AnchorPane aPJanelaP;

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
    private Label jLRodada1;

    @FXML
    private Label jLRodada2;

    @FXML
    private Label jLRodada3;

    @FXML
    private Label jLRodada4;

    @FXML
    private Label jLRodada5;

    @FXML
    private Label jLTempoLancamento;

    @FXML
    private Button jBAlteraNome;

    @FXML
    private Button jBMaisUmPontosL;

    @FXML
    private Button jBMenosUmPontosL;

    @FXML
    private Button jBMaisDoisPontosL;

    @FXML
    private Button jBMenosDoisPontosL;

    @FXML
    private Button jBMaisTresPontosL;

    @FXML
    private Button jBMenosTresPontosL;

    @FXML
    private TextField jTFAlteraNomeLocal;

    @FXML
    private Button jBMaisUmPontosV;

    @FXML
    private Button jBMenosUmPontosV;

    @FXML
    private Button jBMaisDoisPontosV;

    @FXML
    private Button jBMenosDoisPontosV;

    @FXML
    private Button jBMaisTresPontosV;

    @FXML
    private Button jBMenosTresPontosV;

    @FXML
    private TextField jTFAlteraNomeVisitante;

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
    private Button jBIniciaCrono;

    @FXML
    private Button jBReiniciaLancamento;

    @FXML
    private Button jBReiniciaCrono;

    @FXML
    private Button jBDefineCrono;

    @FXML
    private ToggleButton jTBPausaCrono;

    @FXML
    private TextField jTFDefineCrono;

    @FXML
    private Button jBRestauraTudo;

    @FXML
    private Label jLNomeLocal;

    @FXML
    private Label jLNomeVisitante;

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
     * Evento do botão definir
     * define valor para iniciar cronomentro
     * @param event 
     */
    @FXML
    void defineValorCrono(MouseEvent event) {
        System.out.println("Reinicia");
        fimCrono = false;
        jTBPausaCrono.setSelected(false);
        cronosPausado = false;
        try {
            if (jTFDefineCrono.getText().equals("")) {
                if (Main.mandaMSG("#REINICIA_CRONO$" + "10:00:00").equals("REINICIADO")) {
                    fimCrono = false;
                    jTBPausaCrono.setSelected(false);
                    cronosPausado = false;
                    tempoLan = 24;
                    Platform.runLater(() -> {
                        jLTempoLancamento.setText("24");
                        jLCronometro.setText("10:00:00");
                    });
                }
            } else {
                if (Main.mandaMSG("#REINICIA_CRONO$" + jTFDefineCrono.getText()).equals("REINICIADO")) {
                    System.out.println("AQUI");
                    String[] tempo = jTFDefineCrono.getText().split("\\:");
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
                    fimCrono = false;
                    jTBPausaCrono.setSelected(false);
                    cronosPausado = false;
                    tempoLan = 24;

                    Platform.runLater(() -> {
                        jLTempoLancamento.setText("24");
                        jLCronometro.setText(m + ":" + s + ":" + "00");

                    });

                }

            }
        } catch (IOException ex) {
           //IMPLEMENTAR LOG
        }
    }

    /**
     * Ação iniciar cronômetro.
     * Envia comanto para servidor, 
     * Aguarda retorno para atualizar preview
     * @param event 
     */
    @FXML
    void iniciaCrono(MouseEvent event){
        try {
            String retorno = Main.mandaMSG("#INICIA_CRONO$" + jTFDefineCrono.getText());
            if (retorno.equals("CRONOS_INICIADO")) {
                fimCrono = true;
                String corte[] = jTFDefineCrono.getText().split("\\:");
                int min = Integer.parseInt(corte[0]);
                int seg = Integer.parseInt(corte[1]);
                //int mili = Integer.parseInt(corte[3]);
                System.out.println("Chegou Incia Cronos Preview");
                Thread th = new Thread(iniciaCronosPreview(jLCronometro, min, seg, 00));
                th.setDaemon(true);
                th.start();
            }
        } catch (IOException ex) {
            //IMPLEMENTAR LOG
        }
    }

    /**
     * Ação soma mais dois pontos time local.
     * Envia comanto para servidor, 
     * Aguarda retorno para atualizar preview
     * @param event 
     */
    @FXML
    void maisDoisL(MouseEvent event){
        try {
            String retorno = Main.mandaMSG("#TIME$LOCAL$SOMA_PONTO$DOIS$");
            if (retorno.equals("#OK")) {
                pontosL = pontosL + 2;
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
     * Ação soma mais dois pontos time visitante.
     * Envia comanto para servidor, 
     * Aguarda retorno para atualizar preview
     * @param event 
     */
    @FXML
    void maisDoisV(MouseEvent event){
        try {
            String retorno = Main.mandaMSG("#TIME$VISITANTE$SOMA_PONTO$DOIS$");
            if (retorno.equals("#OK")) {
                pontosV = pontosV + 2;
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
     * Ação soma falta time local.
     * Envia comanto para servidor, 
     * Aguarda retorno para atualizar preview
     * @param event 
     */
    @FXML
    void maisFaltaL(MouseEvent event){
        try {
            if (Main.mandaMSG("#MUDA_FALTA$LOCAL$SOMA").equals("MUDADO")) {
                mudaFalta("LOCAL", "SOMA");
            }
        } catch (IOException ex) {
           //IMPLEMENTAR LOG
        }
    }

    /**
     * Ação soma falta time visitante.
     * Envia comanto para servidor, 
     * Aguarda retorno para atualizar preview
     * @param event 
     */
    @FXML
    void maisFaltaV(MouseEvent event){
        try {
            if (Main.mandaMSG("#MUDA_FALTA$VISITANTE$SOMA").equals("MUDADO")) {
                mudaFalta("VISITANTE", "SOMA");
            }
        } catch (IOException ex) {
            //IMPLEMENTAR LOG
        }
    }

    /**
     * Ação soma mais três pontos time local.
     * Envia comanto para servidor, 
     * Aguarda retorno para atualizar preview
     * @param event 
     */
    @FXML
    void maisTresL(MouseEvent event){
        try {
            String retorno = Main.mandaMSG("#TIME$LOCAL$SOMA_PONTO$TRES$");
            if (retorno.equals("#OK")) {
                pontosL = pontosL + 3;
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
     * Ação soma mais três pontos time visitante.
     * Envia comanto para servidor, 
     * Aguarda retorno para atualizar preview
     * @param event 
     */
    @FXML
    void maisTresV(MouseEvent event){
        try {
            String retorno = Main.mandaMSG("#TIME$VISITANTE$SOMA_PONTO$TRES$");
            if (retorno.equals("#OK")) {
                pontosV = pontosV + 3;
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
     * Ação soma mais um ponto time local.
     * Envia comanto para servidor, 
     * Aguarda retorno para atualizar preview
     * @param event 
     */
    @FXML
    void maisUmL(MouseEvent event){
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
     * Ação soma mais um ponto time visitante.
     * Envia comanto para servidor, 
     * Aguarda retorno para atualizar preview
     * @param event 
     */
    @FXML
    void maisUmV(MouseEvent event){
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
     * 
     * @param event 
     */
    @FXML
    void menosDoisL(MouseEvent event){
        try {
            String retorno = Main.mandaMSG("#TIME$LOCAL$SUB_PONTO$DOIS$");
            if (retorno.equals("#OK")) {
                pontosL = pontosL - 2;
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
     * 
     * @param event 
     */
    @FXML
    void menosDoisV(MouseEvent event){
        try {
            String retorno = Main.mandaMSG("#TIME$VISITANTE$SUB_PONTO$DOIS$");
            if (retorno.equals("#OK")) {
                pontosV = pontosV - 2;
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
     * 
     * @param event 
     */
    @FXML
    void menosFaltaL(MouseEvent event){
        try {
            if (Main.mandaMSG("#MUDA_FALTA$LOCAL$SUB").equals("MUDADO")) {
                mudaFalta("LOCAL", "SUB");
            }
        } catch (IOException ex) {
            //IMPLEMENTAR LOG
        }
    }

    /**
     * 
     * @param event 
     */
    @FXML
    void menosFaltaV(MouseEvent event){
        try {
            if (Main.mandaMSG("#MUDA_FALTA$VISITANTE$SUB").equals("MUDADO")) {
                mudaFalta("VISITANTE", "SUB");
            }
        } catch (IOException ex) {
            //IMPLEMENTAR LOG
        }
    }

    /**
     * 
     * @param event 
     */
    @FXML
    void menosTresL(MouseEvent event){
        try {
            String retorno = Main.mandaMSG("#TIME$LOCAL$SUB_PONTO$TRES$");
            if (retorno.equals("#OK")) {
                pontosL = pontosL - 3;
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
     * 
     * @param event 
     */
    @FXML
    void menosTresV(MouseEvent event){
        try {
            String retorno = Main.mandaMSG("#TIME$VISITANTE$SUB_PONTO$TRES$");
            if (retorno.equals("#OK")) {
                pontosV = pontosV - 3;
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
     * 
     * @param event 
     */
    @FXML
    void menosUmL(MouseEvent event){
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
     * 
     * @param event 
     */
    @FXML
    void menosUmV(MouseEvent event){
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
     * 
     * @param event 
     */
    @FXML
    void novoQuarto(MouseEvent event){
        try {
            if (Main.mandaMSG("#NOVA_RODADA").equals("INICIADA_RODADA")) {
                if (rodada == 1) {
                    
                    rodada++;
                    
                    cronosPausado = false;
                    fimCrono = false;
                    
                    somaRodadaL = pontosL;
                    somaRodadaV = pontosV;
                    
                    if (somaRodadaL > 9 & somaRodadaV > 9) {
                        Platform.runLater(() -> {
                            jLRodada1.setText(somaRodadaL + " x " + somaRodadaV);
                        });
                    } else if (somaRodadaL < 9 & somaRodadaV < 9) {
                        Platform.runLater(() -> {
                            jLRodada1.setText("0" + somaRodadaL + " x 0" + somaRodadaV);
                        });
                    } else if (somaRodadaL > 9 & somaRodadaV < 9) {
                        Platform.runLater(() -> {
                            jLRodada1.setText(somaRodadaL + " x 0" + somaRodadaV);
                        });
                    } else if (somaRodadaL < 9 & somaRodadaV > 9) {
                        Platform.runLater(() -> {
                            jLRodada1.setText("0" + somaRodadaL + " x " + somaRodadaV);
                        });
                    }
                } else if (rodada > 1 && rodada < 5) {
                    Label rod;
                    switch (rodada) {
                        case 2:
                            rod = jLRodada2;
                            break;
                        case 3:
                            rod = jLRodada3;
                            break;
                        default:
                            rod = jLRodada4;
                            break;
                    }
                    rodada++;
                    
                    cronosPausado = false;
                    fimCrono = false;
                    
                    somaRodadaL = pontosL - somaRodadaL;
                    somaRodadaV = pontosV - somaRodadaV;
                    
                    System.out.println(somaRodadaL + " = " + somaRodadaV);
                    
                    if (somaRodadaL > 9 & somaRodadaV > 9) {
                        //  Platform.runLater(() -> {
                        rod.setText(somaRodadaL + " x " + somaRodadaV);
                        //});
                    } else if (somaRodadaL < 9 & somaRodadaV < 9) {
                        //Platform.runLater(() -> {
                        rod.setText("0" + somaRodadaL + " x 0" + somaRodadaV);
                        //});
                    } else if (somaRodadaL > 9 & somaRodadaV < 9) {
                        //Platform.runLater(() -> {
                        rod.setText(somaRodadaL + " x 0" + somaRodadaV);
                        //});
                    } else if (somaRodadaL < 9 & somaRodadaV > 9) {
                        //Platform.runLater(() -> {
                        rod.setText("0" + somaRodadaL + " x " + somaRodadaV);
                        //});
                    }
                    
                    somaRodadaL = pontosL;
                    somaRodadaV = pontosV;
                } else {
                    
                    cronosPausado = false;
                    fimCrono = false;
                    
                    somaRodadaL = pontosL - somaRodadaL;
                    somaRodadaV = pontosV - somaRodadaV;
                    
                    if (somaRodadaL > 9 & somaRodadaV > 9) {
                        //Platform.runLater(() -> {
                        jLRodada5.setText(somaRodadaL + " x " + somaRodadaV);
                        //});
                    } else if (somaRodadaL < 9 & somaRodadaV < 9) {
                        // Platform.runLater(() -> {
                        jLRodada5.setText("0" + somaRodadaL + " x 0" + somaRodadaV);
                        //});
                    } else if (somaRodadaL > 9 & somaRodadaV < 9) {
                        //Platform.runLater(() -> {
                        jLRodada5.setText(somaRodadaL + " x 0" + somaRodadaV);
                        //});
                    } else if (somaRodadaL < 9 & somaRodadaV > 9) {
                        //Platform.runLater(() -> {
                        jLRodada5.setText("0" + somaRodadaL + " x " + somaRodadaV);
                        // });
                        
                    }
                    somaRodadaL = pontosL;
                    somaRodadaV = pontosV;
                    jBNovoQuarto.setDisable(true);
                }
            }
        } catch (IOException ex) {
            //IMPLEMENTAR LOG
        }
    }

    /**
     * 
     * @param event 
     */
    @FXML
    void pausaCrono(MouseEvent event){
        try{
        if (jTBPausaCrono.isSelected()) {
            if (Main.mandaMSG("#PAUSA_CRONOS").equals("PAUSADO")) {
                cronosPausado = true;
            }
        } else {
            if (Main.mandaMSG("#CONTINUA_CRONOS").equals("CONTINUA")) {
                cronosPausado = false;
            }
        } 
        }catch(IOException ex){
            //IMPLEMENTAR LOG
        }
    }

    /**
     * 
     * @param event
     * @throws InterruptedException 
     */
    @FXML
    void reiniciaCrono(MouseEvent event) throws InterruptedException {
        System.out.println("Reinicia");
        fimCrono = false;
        jTBPausaCrono.setSelected(false);
        cronosPausado = false;
        try{
        if (jTFDefineCrono.getText().equals("")) {
            if (Main.mandaMSG("#REINICIA_CRONO$" + "10:00:00").equals("REINICIADO")) {
                fimCrono = false;
                jTBPausaCrono.setSelected(false);
                cronosPausado = false;
                tempoLan = 24;
                Platform.runLater(() -> {
                    jLTempoLancamento.setText("24");
                    jLCronometro.setText("10:00:00");
                });
            }
        } else {
            if (Main.mandaMSG("#REINICIA_CRONO$" + jTFDefineCrono.getText()).equals("REINICIADO")) {
                System.out.println("AQUI");
                String[] tempo = jTFDefineCrono.getText().split("\\:");
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
                fimCrono = false;
                jTBPausaCrono.setSelected(false);
                cronosPausado = false;
                tempoLan = 24;

                Platform.runLater(() -> {
                    jLTempoLancamento.setText("24");
                    jLCronometro.setText(m + ":" + s + ":" + "00");

                });

            }

        }
        }catch(IOException ex){
            //IMPLEMENTAR LOG
        }
    }

    /**
     * 
     * @param event 
     */
    @FXML
    void reiniciaLancamento(MouseEvent event){
        try {
            if (Main.mandaMSG("#REINICIA_TEMPO").equals("REINICIADO")) {
                tempoLan = 24;
                cronosPausado = false;
                jLTempoLancamento.setText("24");
            }
        } catch (IOException ex) {
            //IMPLEMENTAR LOG
        }
    }

    /**
     * 
     * @param event 
     */
    @FXML
    void restauraTudo(MouseEvent event) throws IOException {
        if (Main.mandaMSG("#RESTAURA_TUDO").equals("RESTAURADO")) {
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
            jBNovoQuarto.setDisable(true);
            Platform.runLater(() -> {
                jLCronometro.setText("00:00:00");
                jLRodada1.setText("00 X 00");
                jLRodada2.setText("00 X 00");
                jLRodada3.setText("00 X 00");
                jLRodada4.setText("00 X 00");
                jLRodada5.setText("00 X 00");
                jLTempoLancamento.setText("00");
                jLPontosVisitante.setText("00");
                jLPontosLocal.setText("00");
                jLFaltasLocal.setText("00");
                jLFaltasVisitante.setText("00");
                jLNomeLocal.setText("LOCAL");
                jLNomeVisitante.setText("VISITANTE");
            });
        }
    }

    /**
     * 
     * @param l
     * @param min
     * @param seg
     * @param mili
     * @return 
     */
    private Task iniciaCronosPreview(Label l, int min, int seg, int mili) {

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
                            iniciaTempoLPreview(jLTempoLancamento);
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

                    ms--;

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
                return null;
            }
        };
        return task;
    }

    /**
     * 
     * @return 
     */
    public boolean fimCrono() {
        return fimCrono;
    }

    /**
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

    /**
     * 
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
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
