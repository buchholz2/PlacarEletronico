package controladorplaca;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
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

    @FXML
    void alteraNomes(MouseEvent event) throws IOException {
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

    }

    @FXML
    void defineValorCrono(MouseEvent event) throws IOException {
        System.out.println("Reinicia");
        fimCrono = false;
        jTBPausaCrono.setSelected(false);
        cronosPausado = false;
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
    }

    @FXML
    void iniciaCrono(MouseEvent event) throws IOException {
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
    }

    @FXML
    void maisDoisL(MouseEvent event) throws IOException {
        String retorno = Main.mandaMSG("#TIME$LOCAL$SOMA_PONTO$DOIS$");
        if (retorno.equals("#OK")) {
            pontosL = pontosL + 2;
            if (pontosL > 9) {
                jLPontosLocal.setText("" + pontosL);
            } else {
                jLPontosLocal.setText("0" + pontosL);
            }

        }
    }

    @FXML
    void maisDoisV(MouseEvent event) throws IOException {
        String retorno = Main.mandaMSG("#TIME$VISITANTE$SOMA_PONTO$DOIS$");
        if (retorno.equals("#OK")) {
            pontosV = pontosV + 2;
            if (pontosV > 9) {
                jLPontosVisitante.setText("" + pontosV);
            } else {
                jLPontosVisitante.setText("0" + pontosV);
            }

        }
    }

    @FXML
    void maisFaltaL(MouseEvent event) throws IOException {
        if (Main.mandaMSG("#MUDA_FALTA$LOCAL$SOMA").equals("MUDADO")) {
            mudaFalta("LOCAL", "SOMA");
        }
    }

    @FXML
    void maisFaltaV(MouseEvent event) throws IOException {
        if (Main.mandaMSG("#MUDA_FALTA$VISITANTE$SOMA").equals("MUDADO")) {
            mudaFalta("VISITANTE", "SOMA");
        }
    }

    @FXML
    void maisTresL(MouseEvent event) throws IOException {
        String retorno = Main.mandaMSG("#TIME$LOCAL$SOMA_PONTO$TRES$");
        if (retorno.equals("#OK")) {
            pontosL = pontosL + 3;
            if (pontosL > 9) {
                jLPontosLocal.setText("" + pontosL);
            } else {
                jLPontosLocal.setText("0" + pontosL);
            }

        }
    }

    @FXML
    void maisTresV(MouseEvent event) throws IOException {
        String retorno = Main.mandaMSG("#TIME$VISITANTE$SOMA_PONTO$TRES$");
        if (retorno.equals("#OK")) {
            pontosV = pontosV + 3;
            if (pontosV > 9) {
                jLPontosVisitante.setText("" + pontosV);
            } else {
                jLPontosVisitante.setText("0" + pontosV);
            }

        }
    }

    @FXML
    void maisUmL(MouseEvent event) throws IOException {
        String retorno = Main.mandaMSG("#TIME$LOCAL$SOMA_PONTO$UM$");
        if (retorno.equals("#OK")) {
            pontosL = pontosL + 1;
            if (pontosL > 9) {
                jLPontosLocal.setText("" + pontosL);
            } else {
                jLPontosLocal.setText("0" + pontosL);
            }

        }
    }

    @FXML
    void maisUmV(MouseEvent event) throws IOException {
        String retorno = Main.mandaMSG("#TIME$VISITANTE$SOMA_PONTO$UM$");
        if (retorno.equals("#OK")) {
            pontosV = pontosV + 1;
            if (pontosV > 9) {
                jLPontosVisitante.setText("" + pontosV);
            } else {
                jLPontosVisitante.setText("0" + pontosV);
            }

        }
    }

    @FXML
    void menosDoisL(MouseEvent event) throws IOException {
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
    }

    @FXML
    void menosDoisV(MouseEvent event) throws IOException {
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
    }

    @FXML
    void menosFaltaL(MouseEvent event) throws IOException {
        if (Main.mandaMSG("#MUDA_FALTA$LOCAL$SUB").equals("MUDADO")) {
            mudaFalta("LOCAL", "SUB");
        }
    }

    @FXML
    void menosFaltaV(MouseEvent event) throws IOException {
        if (Main.mandaMSG("#MUDA_FALTA$VISITANTE$SUB").equals("MUDADO")) {
            mudaFalta("VISITANTE", "SUB");
        }
    }

    @FXML
    void menosTresL(MouseEvent event) throws IOException {
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
    }

    @FXML
    void menosTresV(MouseEvent event) throws IOException {
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
    }

    @FXML
    void menosUmL(MouseEvent event) throws IOException {
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
    }

    @FXML
    void menosUmV(MouseEvent event) throws IOException {
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
    }

    @FXML
    void novoQuarto(MouseEvent event) throws IOException {
        if (Main.mandaMSG("#NOVA_RODADA").equals("INICIADA_RODADA")) {
            if (rodada == 1) {

                rodada++;

                cronosPausado = false;
                fimCrono = false;

                somaRodadaL = pontosL;
                somaRodadaV = pontosV;

                if (somaRodadaL > 9 && somaRodadaV > 9) {
                    Platform.runLater(() -> {
                        jLRodada1.setText(somaRodadaL + " : " + somaRodadaV);
                    });
                } else if (somaRodadaL < 9 && somaRodadaV < 9) {
                    Platform.runLater(() -> {
                        jLRodada1.setText("0" + somaRodadaL + " : 0" + somaRodadaV);
                    });
                } else if (somaRodadaL > 9 && somaRodadaV < 9) {
                    Platform.runLater(() -> {
                        jLRodada1.setText(somaRodadaL + " : 0" + somaRodadaV);
                    });
                } else {
                    Platform.runLater(() -> {
                        jLRodada1.setText("0" + somaRodadaL + " : " + somaRodadaV);
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

                if (somaRodadaL > 9 && somaRodadaV > 9) {
                    Platform.runLater(() -> {
                        rod.setText(somaRodadaL + " : " + somaRodadaV);
                    });
                } else if (somaRodadaL < 9 && somaRodadaV < 9) {
                    Platform.runLater(() -> {
                        rod.setText("0" + somaRodadaL + " : 0" + somaRodadaV);
                    });
                } else if (somaRodadaL > 9 && somaRodadaV < 9) {
                    Platform.runLater(() -> {
                        rod.setText(somaRodadaL + " : 0" + somaRodadaV);
                    });
                } else {
                    Platform.runLater(() -> {
                        rod.setText("0" + somaRodadaL + " : " + somaRodadaV);
                    });
                }

                somaRodadaL = pontosL + somaRodadaL;
                somaRodadaV = pontosV + somaRodadaV;
            } else {

                cronosPausado = false;
                fimCrono = false;

                somaRodadaL = pontosL - somaRodadaL;
                somaRodadaV = pontosV - somaRodadaV;

                if (somaRodadaL > 9 && somaRodadaV > 9) {
                    Platform.runLater(() -> {
                        jLRodada5.setText(somaRodadaL + " : " + somaRodadaV);
                    });
                } else if (somaRodadaL < 9 && somaRodadaV < 9) {
                    Platform.runLater(() -> {
                        jLRodada5.setText("0" + somaRodadaL + " : 0" + somaRodadaV);
                    });
                } else if (somaRodadaL > 9 && somaRodadaV < 9) {
                    Platform.runLater(() -> {
                        jLRodada5.setText(somaRodadaL + " : 0" + somaRodadaV);
                    });
                } else {
                    Platform.runLater(() -> {
                        jLRodada5.setText("0" + somaRodadaL + " : " + somaRodadaV);
                    });
                }
                somaRodadaL = pontosL + somaRodadaL;
                somaRodadaV = pontosV + somaRodadaV;
                jBNovoQuarto.setDisable(true);
            }
        }
    }

    @FXML
    void pausaCrono(MouseEvent event) throws IOException {
        if (jTBPausaCrono.isSelected()) {
            if (Main.mandaMSG("#PAUSA_CRONOS").equals("PAUSADO")) {
                cronosPausado = true;
            }
        } else {
            if (Main.mandaMSG("#CONTINUA_CRONOS").equals("CONTINUA")) {
                cronosPausado = false;
            }
        }
        //991417179   
    }

    @FXML
    void reiniciaCrono(MouseEvent event) throws IOException, InterruptedException {
        System.out.println("Reinicia");
        fimCrono = false;
        jTBPausaCrono.setSelected(false);
        cronosPausado = false;
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
    }

    @FXML
    void reiniciaLancamento(MouseEvent event) throws IOException {
        if (Main.mandaMSG("#REINICIA_TEMPO").equals("REINICIADO")) {
            tempoLan = 24;
            jLTempoLancamento.setText("24");
        }
    }

    @FXML
    void restauraTudo(MouseEvent event
    ) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb
    ) {
        // TODO
    }

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

    public boolean fimCrono() {
        return fimCrono;
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
}
