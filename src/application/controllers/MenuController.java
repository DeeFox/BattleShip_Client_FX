package application.controllers;

import application.Main;
import application.interfaces.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;

public class MenuController implements Controller<Main> {
    
    @FXML
    private Button spbutton;
    
    @FXML
    private Button mpbutton;
    
    @FXML
    private Button prefbutton;
    
    private Main parent;
    
    @Override
    public void setParent(Main parent) {
        this.parent = parent;
    }
    
    @FXML
    protected void spButtonPressed() {
        System.out.println("singleplayer");
        if(parent != null) {
            parent.doStuff();
        }
    }
    
    @FXML
    protected void mpButtonPressed() {
        System.out.println("multiplayer");
        if(parent != null) {
            parent.doStuff2();
        }
    }
    
    @FXML
    protected void prefButtonPressed() {
        System.out.println("preferences");
        if(parent != null) {
            parent.doStuff3();
        }
    }
    
    @FXML
    protected void spButtonMouseEntered() {
        DropShadow effect = new DropShadow();
        spbutton.setEffect(effect);
    }
    
    @FXML
    protected void spButtonMouseExited() {
        spbutton.setEffect(null);
    }

}
