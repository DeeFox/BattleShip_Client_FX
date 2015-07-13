package application.controllers;

import java.util.Observable;
import java.util.Observer;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.Point;
import model.Ship;
import util.Pair;

public class FieldCellController implements Observer {

    private int x, y;

    private int shipIndex;

    private boolean placed;
    private Ship ship;

    @FXML
    private ImageView shipImage;
    
    @FXML
    private Circle hitMarker;

    @FXML
    private void initialize() {
        shipImage.setOpacity(0.0);
        hitMarker.setOpacity(0.0);
    }

    public void bindSizes(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height) {

        shipImage.fitWidthProperty().bind(width.multiply(0.10));
        shipImage.fitHeightProperty().bind(height.multiply(0.10));
        hitMarker.radiusProperty().bind(Bindings.min(height, width).multiply(0.025));
    }

    public void place(Ship ship, int indOfShip) {
        this.ship = ship;
        this.shipIndex = indOfShip;
        this.ship.addObserver(this);
        this.placed = true;
        Ship.Orientation o = ship.getOrientation();
        Image i = ship.getImage(indOfShip);
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                shipImage.setOpacity(1.0);
                shipImage.setImage(i);
                switch (o) {
                case HORIZONTAL:
                    shipImage.setRotate(0.0);
                    break;
                case VERTICAL:
                    shipImage.setRotate(90.0);
                    break;
                default:
                    break;

                }

            }
        });
    }

    public void enableGhost(Ship ship, int indOfShip) {
        if(!placed) {
            Ship.Orientation o = ship.getOrientation();
            Image i = ship.getImage(indOfShip);
            this.ship = ship;
            this.shipIndex = indOfShip;
            ship.addObserver(this);
            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    shipImage.setOpacity(0.5);
                    shipImage.setImage(i);
                    switch (o) {
                    case HORIZONTAL:
                        shipImage.setRotate(0.0);
                        break;
                    case VERTICAL:
                        shipImage.setRotate(90.0);
                        break;
                    default:
                        break;

                    }
                }
            }); 
        }
        
    }

    public void outOfBounds(Ship ship, int indOfShip) {
        if(!placed) {
            Ship.Orientation o = ship.getOrientation();
            Image i = ship.getImage(indOfShip);
            this.ship = ship;
            this.shipIndex = indOfShip;
            ship.addObserver(this);
            Platform.runLater(new Runnable() {

                @Override
                public void run() {

                    shipImage.setOpacity(0.1);
                    shipImage.setImage(i);
                    switch (o) {
                    case HORIZONTAL:
                        shipImage.setRotate(0.0);
                        break;
                    case VERTICAL:
                        shipImage.setRotate(90.0);
                        break;
                    default:
                        break;

                    }

                }
            });  
        }
        
    }

    public void destroy() {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {

                shipImage.setOpacity(1.0);
                // ph.setFill(Color.RED);

            }
        });
    }

    public void disable() {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                shipImage.setOpacity(0.0);

            }
        });
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void mark(Color color) {
        this.hitMarker.setOpacity(1.0);
        this.hitMarker.setFill(color);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(!placed) {
            switch(this.ship.getOrientation()) {
            case HORIZONTAL:
                if(ship.getPosX() > this.x || ship.getPosX() + ship.getSize() - 1 < this.x || ship.getPosY() != this.y) {
                    this.ship.deleteObserver(this);
                    this.ship = null;
                    this.shipIndex = 0;
                    this.disable();
                }
                break;
            case VERTICAL:
                if(ship.getPosY() > this.y || ship.getPosY() + ship.getSize() - 1 < this.y || ship.getPosX() != this.x) {
                    this.ship.deleteObserver(this);
                    this.ship = null;
                    this.shipIndex = 0;
                    this.disable();
                }
                break;
            default:
                break;
            
            }
        }

    }

}
