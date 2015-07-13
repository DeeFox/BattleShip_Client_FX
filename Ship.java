package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Ship {
    
    private StringProperty name;
    private BooleanProperty[] hits;
    private int length = 5;
    
    public Ship(String name) {
        this.name = new SimpleStringProperty(name);
        hits = new BooleanProperty[5];
        for(int i = 0; i < hits.length; i++) {
            hits[i] = new SimpleBooleanProperty(false);
        }
    }
    
    public Ship(String name, int l) {
        this.name = new SimpleStringProperty(name);
        hits = new BooleanProperty[5];
        for(int i = 0; i < hits.length; i++) {
            hits[i] = new SimpleBooleanProperty(false);
        }
        this.length = l;
    }
    
    public String getName() {
        return name.getValue();
    }
    
    public boolean[] getHits() {
        boolean[] res = new boolean[hits.length];
        for(int i = 0; i < hits.length; i++) {
            res[i] = hits[i].get();
        }
        return res;
    }
    
    
    public StringProperty nameProperty() {
        return name;
    }
    
    public BooleanProperty[] hitsProperty() {
        return hits;
    }
    
    public void hit() {
        this.name = new SimpleStringProperty("abc");
    }
    
    public int getLength() {
        return length;
    }
    
    public void setLength(int length) {
        this.length = length;
    }
    

}
