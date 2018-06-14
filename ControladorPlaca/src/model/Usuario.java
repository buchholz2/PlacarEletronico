package model;

import javafx.beans.property.SimpleStringProperty;

public class Usuario {

    private final SimpleStringProperty usuario;
    private final SimpleStringProperty funcao;

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

    public String getUsuario() {
        return usuario.get();
    }

    public void setUsuario(String usuario) {
        this.usuario.set(usuario);
    }

    public String getFuncao() {
        return funcao.get();
    }

    public void setFuncao(String funcao) {
        this.funcao.set(funcao);
    }

}
