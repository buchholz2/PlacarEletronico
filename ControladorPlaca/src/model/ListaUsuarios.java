package model;

import java.util.ArrayList;
import java.util.List;


public class ListaUsuarios {

    
    private List<Usuario> usuarios = new ArrayList<>();

    /**
     * Ação de ler a lista de usuarios
     * 
     * @return 
     */
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    /**
     * Ação de gravar usários na lista
     * 
     * @param usuarios 
     */
    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
