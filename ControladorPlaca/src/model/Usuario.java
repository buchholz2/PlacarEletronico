package model;

import javafx.beans.property.SimpleStringProperty;

public class Usuario {

    private final SimpleStringProperty usuario;
    private final SimpleStringProperty funcao;

    /**
     * Constrututor da classe
     * 
     * @param usuario
     * @param funcao 
     */
    public Usuario(String usuario, String funcao) {
        this.usuario = new SimpleStringProperty(usuario);
        this.funcao = new SimpleStringProperty(funcao);
    }

    public SimpleStringProperty usuarioProperty() {
        return usuario;
    }

    public SimpleStringProperty funcaoProperty() {
        return funcao;
    }

    /**
     * Ação de ler o usuário
     * 
     * @return 
     */
    public String getUsuario() {
        return usuario.get();
    }

    /**
     * Ação de gravar o usuário
     * 
     * @param usuario 
     */
    public void setUsuario(String usuario) {
        this.usuario.set(usuario);
    }

    /**
     * Ação de ler a função do usuário
     * @return 
     */
    public String getFuncao() {
        return funcao.get();
    }

    /**
     * Ação de gravar a função do usuário
     * @param funcao 
     */
    public void setFuncao(String funcao) {
        this.funcao.set(funcao);
    }

}
