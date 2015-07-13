package application.controllers;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import model.Ship;

public class Cell implements Observer {

    private CellController controller;
    private GridPane content;

    // TODO: make generic
    private Ship ship;
    private ListController parent;
    private boolean left;

    public Cell(Ship ship, boolean left, ListController parent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/CellView.fxml"));
            this.content = loader.load();
            this.controller = loader.getController();
            this.ship = ship;
            this.parent = parent;
            this.left = left;
            initialize();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void initialize() {
        controller.setAlignment(left);
        controller.enable(ship.getSize());
        controller.markAllDisabled();
        controller.setName(ship.toString());
        controller.setParent(this);
    }

    public GridPane getContent() {
        return content;
    }
    
    public Ship getModel() {
        return ship;
    }

    public void selectCell() {
        if(!ship.isPlaced()) {
            content.setStyle("-fx-background-color: darkgray");
            ship.addObserver(this);
            parent.selectionDetected(this);
        }
        
    }

    public void deselectCell() {
        content.setStyle("-fx-background-color: transparent");
    }
    
    public void bindSizes(Scene scene) {
        controller.bindSizes(scene);
        
    }

    @Override
    public void update(Observable o, Object arg) {
        Ship ship = (Ship) arg;
        if(ship.isPlaced()) {
            controller.markAllIntact();
            this.deselectCell();
        }
        
    }
}