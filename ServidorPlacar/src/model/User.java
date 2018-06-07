package model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "usuario")
@XmlAccessorType(XmlAccessType.FIELD)

public class User {

    private String user;
    private String password;
    private boolean userAdm;
    private boolean userPlacar;
    private boolean userPropaganda;

    @XmlElement
    public String getUser() {
        return user;
    }

    @XmlElement
    public void setUser(String user) {
        this.user = user;
    }

    @XmlElement
    public String getPassword() {
        return password;
    }

    @XmlElement
    public void setPassword(String password) {
        this.password = password;
    }

    @XmlElement
    public boolean isUserAdm() {
        return userAdm;
    }

    @XmlElement
    public void setUserAdm(boolean userAdm) {
        this.userAdm = userAdm;
    }

    @XmlElement
    public boolean isUserPlacar() {
        return userPlacar;
    }

    @XmlElement
    public void setUserPlacar(boolean userPlacar) {
        this.userPlacar = userPlacar;
    }

    @XmlElement
    public boolean isUserPropaganda() {
        return userPropaganda;
    }

    @XmlElement
    public void setUserPropaganda(boolean userPropaganda) {
        this.userPropaganda = userPropaganda;
    }

}
