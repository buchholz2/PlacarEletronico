package model;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Cristiano Künas
 * @author Daniel Buchholz
 * @author Douglas Hoffmann
 * @author Leandro Heck
 */

public class Propaganda {
    private final SimpleStringProperty url;

    /**
     * Construtor da classe
     * 
     * @param url 
     */
    public Propaganda(String url) {
        this.url = new SimpleStringProperty(url);
    }

    public SimpleStringProperty urlProperty() {
        return url;
    }
    
    /**
     * Ação de ler a URL
     * 
     * @return 
     */
    public String getUrl() {
        return url.get();
    }

    /**
     * Ação de gravar a URL
     * 
     * @param url 
     */
    public void setUrl(String url) {
        this.url.set(url);
    }   
        
}
