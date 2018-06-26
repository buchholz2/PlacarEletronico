/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;

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

     /**
     * Evento do botão alterar nomes. Verifica se camposestão vazios. Envia
     * comando altera_nome para o servidor Aguarda retorno para atualizar placar
     * no preview do controlador
     *
     * @param event
     */
    @FXML
    void alteraNomes(MouseEvent event) {

    }

    /**
     * Ação iniciar cronômetro. Envia comanto para servidor, Aguarda retorno
     * para atualizar preview
     *
     * @param event
     */
    @FXML
    void iniciaCrono(MouseEvent event) {

    }

     /**
     * Ação iniciar propaganda. Envia comanto para servidor, Aguarda retorno
     * para atualizar preview
     *
     * @param event
     */
    @FXML
    void iniciaPropaganda(MouseEvent event) {

    }

    /**
     * Ação soma falta time local. Envia comanto para servidor, Aguarda retorno
     * para atualizar preview
     * @param event
     */
    @FXML
    void maisFaltaL(MouseEvent event) {

    }

    /**
     * Ação soma falta time visitante. Envia comanto para servidor, Aguarda retorno
     * para atualizar preview
     * @param event
     */
    @FXML
    void maisFaltaV(MouseEvent event) {

    }

    /**
     * Ação soma mais um ponto time local. Envia comanto para servidor,
     * Aguarda retorno para atualizar preview
     * @param event
     */
    @FXML
    void maisUmL(MouseEvent event) {

    }

    /**
     * Ação soma mais um ponto time visitante. Envia comanto para servidor,
     * Aguarda retorno para atualizar preview
     * @param event
     */
    @FXML
    void maisUmV(MouseEvent event) {

    }

    /**
     * Ação diminui falta time local. Envia comanto para servidor, Aguarda retorno
     * para atualizar preview
     * @param event
     */
    @FXML
    void menosFaltaL(MouseEvent event) {

    }

    /**
     * Ação diminui falta time visitante. Envia comanto para servidor, Aguarda retorno
     * para atualizar preview
     * @param event
     */
    @FXML
    void menosFaltaV(MouseEvent event) {

    }

    /**
     * Ação subtrai menos um ponto time local. Envia comanto para servidor,
     * Aguarda retorno para atualizar preview
     * @param event
     */
    @FXML
    void menosUmL(MouseEvent event) {

    }

    /**
     * Ação subtrai menos um ponto time visitante. Envia comanto para servidor,
     * Aguarda retorno para atualizar preview
     * @param event
     */
    @FXML
    void menosUmV(MouseEvent event) {

    }

    /**
     * Ação para iniciar um novo periodo na partida. Envia comanto para servidor,
     * Aguarda retorno para atualizar preview
     * @param event 
     */
    @FXML
    void novoPeriodo(MouseEvent event) {

    }

    /**
     * Ação de pausar o cronometro. Envia comanto para servidor, Aguarda retorno
     * para atualizar preview
     * @param event
     */
    @FXML
    void pausaCrono(MouseEvent event) {

    }

    /**
     * Ação de reiniciar o cronometro que esta pausado. Envia comanto para servidor, Aguarda retorno
     * para atualizar preview
     * @param event
     * @throws InterruptedException
     */
    @FXML
    void reiniciaCrono(MouseEvent event) {

    }

    /**
     * Ação de restaurar todos os elementos do painel contradolor basquete. 
     * Envia comanto para servidor, Aguarda retorno
     * para atualizar preview
     * @param event
     */
    @FXML
    void restauraTudo(MouseEvent event) {

    }
    
    /**
     * Ação para somar acrescimo ao tempo. 
     * Envia comanto para servidor, Aguarda retorno
     * para atualizar preview
     * @param event 
     */
    @FXML
    void somaArescimo(MouseEvent event) {

    }
    
    /**
     * Ação para voltar a tela de escolha de modalidade. 
     * Envia comanto para servidor, Aguarda retorno
     * para atualizar preview
     * @param event 
     */
    @FXML
    void voltarEscolhaModalidade(MouseEvent event) {

    }

    /**
     * Inicializar
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
