package model;

public class User {

    private String user;
    private String password;
    private boolean userAdm;
    private boolean userPlacar;
    private boolean userPropaganda;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

}
