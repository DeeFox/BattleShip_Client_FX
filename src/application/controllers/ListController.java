package application.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import gamelogic.GameHandler;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import model.Ship;

public class ListController {
    
    @FXML
    private VBox list;
    
    private GameController parent;
    
    private List<Cell> entries;
    
    @FXML
    private void initialize() {
        entries = new ArrayList<Cell>();
        list.getStyleClass().add("list-view-left");
    }
    
    public void setParent(GameController parent) {
        this.parent = parent;
    }
    
    public void setModel(List<Ship> ships, boolean left) {
        for(Ship s : ships) {
            Cell cell = new Cell(s, left, this);
            entries.add(cell);
            list.getChildren().add(cell.getContent());
        }
        if(!left) {
            list.getStyleClass().add("list-view-right");
        }
    }
    
    public void selectionDetected(Cell source) {
        List<Cell> cells = entries.stream().filter(x -> !x.equals(source)).collect(Collectors.toList());
        for(Cell c : cells) {
            c.deselectCell();
        }
        System.out.println(parent);
        GameHandler gh = parent.getModel();
        gh.selectedShipChanged(source.getModel());
    }
    

    public void bindSizes(Scene scene) {
        ReadOnlyDoubleProperty width = scene.widthProperty();
        ReadOnlyDoubleProperty height = scene.heightProperty();
        list.prefWidthProperty().bind(width.multiply(0.09375));
        list.prefHeightProperty().bind(height.multiply(2.0 / 3.0));
        for(Cell c : entries) {
            c.bindSizes(scene);
        }
    }

}
