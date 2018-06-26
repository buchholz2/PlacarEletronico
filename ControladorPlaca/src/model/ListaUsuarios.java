package model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Cristiano Künas
 * @author Daniel Buchholz
 * @author Douglas Hoffmann
 * @author Leandro Heck
 */

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
