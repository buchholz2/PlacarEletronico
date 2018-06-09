package model;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "usuario")
@XmlAccessorType(XmlAccessType.FIELD)
public class Usuario {

    private String usuario;
    private String senha;
    private boolean userAdm;
    private boolean userPlacar;
    private boolean userPropaganda;

    public boolean isUserAdm() {
        return userAdm;
    }

    public void setUserAdm(boolean userAdm) {
        this.userAdm = userAdm;
    }

    public boolean isUserPlacar() {
        return userPlacar;
    }

    public void setUserPlacar(boolean userPlacar) {
        this.userPlacar = userPlacar;
    }

    public boolean isUserPropaganda() {
        return userPropaganda;
    }

    public void setUserPropaganda(boolean userPropaganda) {
        this.userPropaganda = userPropaganda;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }
    
    public void setSenha(String senha) {
        this.senha = senha;
    }

}
