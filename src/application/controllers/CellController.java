package application.controllers;

import application.interfaces.Controller;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CellController implements Controller<Cell> {    
    @FXML
    private GridPane cellContainer;
    
    @FXML
    private Text nameText;
    
    @FXML
    private HBox rectangleContainer;
    
    private Cell parent;
    
    private boolean leftAlignment;
    private int enableAmount;
    
    @FXML
    private void initialize() {
        ObservableList<Node> list = rectangleContainer.getChildren();
        for(Node n : list) {
            n.setVisible(false);
        }
    }
    
    public void setName(String name) {
        nameText.setText(name);
    }
    
    @FXML
    private void selectEntry(MouseEvent e) {
        parent.selectCell();
    }
    
    public void setAlignment(boolean left) {
        this.leftAlignment = left;
        if(!left) {
            GridPane.setHalignment(nameText, HPos.RIGHT);
            rectangleContainer.setAlignment(Pos.TOP_RIGHT);
            enable(enableAmount);
        } else {
            GridPane.setHalignment(nameText, HPos.LEFT);
            rectangleContainer.setAlignment(Pos.TOP_LEFT);
        }
    }
    
    public void mark(int i, boolean intact) {
        ObservableList<Node> list = rectangleContainer.getChildren();
        Rectangle rec = (Rectangle) list.get(i);
        if(intact) {
            rec.setFill(Color.GREEN);
        } else {
            rec.setFill(Color.RED);
        }
        
    }
    
    public void markAllDisabled() {
        ObservableList<Node> list = rectangleContainer.getChildren();
        for(Node n : list) {
            Rectangle rec = (Rectangle) n;
            rec.setFill(Color.GRAY);
        }
    }
    
    public void markAllIntact() {
        ObservableList<Node> list = rectangleContainer.getChildren();
        for(Node n : list) {
            Rectangle rec = (Rectangle) n;
            rec.setFill(Color.GREEN);
        }
    }
    
    public void markAllDestroyed() {
        ObservableList<Node> list = rectangleContainer.getChildren();
        for(Node n : list) {
            Rectangle rec = (Rectangle) n;
            rec.setFill(Color.RED);
        }
    }
    
    public void enable(int amount) {
        this.enableAmount = amount;
        ObservableList<Node> list = rectangleContainer.getChildren();
        if(leftAlignment) {
            for(int i = 0; i < amount; i++) {
                list.get(i).setVisible(true);
            } 
        } else {
            for(int i = 4; i >= 5 - amount; i--) {
                list.get(i).setVisible(true);
            }
        }
        
    }

    @Override
    public void setParent(Cell parent) {
        this.parent = parent;
        
    }
    
    public void bindSizes(Scene scene) {
        ReadOnlyDoubleProperty width = scene.widthProperty();
        ReadOnlyDoubleProperty height = scene.heightProperty();
        cellContainer.prefWidthProperty().bind(width.multiply(0.09375));
        cellContainer.prefHeightProperty().bind(height.multiply(0.6 / 9.0));
        rectangleContainer.prefWidthProperty().bind(width.multiply(0.09375));
        rectangleContainer.prefHeightProperty().bind(height.multiply(0.6 / 9.0));
        ObservableList<Node> list = rectangleContainer.getChildren();
        for(Node n : list) {
            Rectangle rec = (Rectangle) n;
            rec.widthProperty().bind(width.multiply(0.01625));
            rec.heightProperty().bind(height.multiply(0.15 / 9.0));
        }
        height.addListener(new ChangeListener<Number>() {
            

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double value = (double) newValue;
                nameText.setFont(Font.font(value * (0.14 / 9.0)));
            }
        });
    }
    

}
