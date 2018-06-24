package model;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Cristiano Alex Künas
 */
public class Propaganda {
    private final SimpleStringProperty url;

    public Propaganda(String url) {
        this.url = new SimpleStringProperty(url);
    }

    public SimpleStringProperty urlProperty() {
        return url;
    }
    public String getUrl() {
        return url.get();
    }

    public void setUrl(String url) {
        this.url.set(url);
    }   
        
}
