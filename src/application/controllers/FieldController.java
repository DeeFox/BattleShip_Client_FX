package application.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import application.interfaces.Controller;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.Field;
import model.Point;
import model.Ship;
import util.Pair;

public class FieldController implements Controller<GameController>, Observer {

    @FXML
    private AnchorPane anchor;

    @FXML
    private GridPane grid;

    private GameController parent;

    private List<Rectangle> rectangles;

    private List<FieldCellController> cells;
    private FieldCellController[][] cellss;
    
    private boolean isActive;

    @FXML
    private void initialize() {
        rectangles = new ArrayList<>();
        cells = new ArrayList<>();
        cellss = new FieldCellController[grid.getColumnConstraints().size()][grid.getRowConstraints().size()];
        for (int i = 0; i < grid.getColumnConstraints().size(); i++) {
            for (int j = 0; j < grid.getRowConstraints().size(); j++) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/FieldCellView.fxml"));
                try {
                    StackPane pane = loader.load();
                    FieldCellController cell = loader.getController();
                    cell.setPos(i, j);
                    cell.bindSizes(anchor.widthProperty(), anchor.heightProperty());
                    pane.prefWidthProperty().bind(anchor.widthProperty().multiply(0.10));
                    pane.prefHeightProperty().bind(anchor.heightProperty().multiply(0.10));
                    cellss[j][i] = cell;
                    cells.add(cell);
                    grid.add(pane, i, j);
                    GridPane.setHalignment(pane, HPos.CENTER);
                    GridPane.setValignment(pane, VPos.CENTER);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (Rectangle rect : rectangles) {
                    rect.xProperty().bind(anchor.widthProperty().divide(10.0));
                }
            }
        });
    }

    @FXML
    protected void onMouseClicked(MouseEvent event) {

        

        if(this.isActive) {
            if (event.getButton() == MouseButton.PRIMARY) {
                parent.getModel().clickPH();
            }
            if (event.getButton() == MouseButton.SECONDARY) {
                parent.getModel().rotateSelectedShip();
            } 
        } else {
            int x = (int) (event.getX() / (anchor.getWidth() / 10));
            int y = (int) (event.getY() / (anchor.getHeight() / 10));
            parent.getModel().onMouseClick(x, y);
        }
        

    }

    @FXML
    protected void onMouseMoved(MouseEvent event) {
        if(this.isActive) {
            int x = (int) (event.getX() / (anchor.getWidth() / 10));
            int y = (int) (event.getY() / (anchor.getHeight() / 10));
            parent.getModel().mousePositionChanged(new Point(x, y));
        }
        
    }

    public List<FieldCellController> getCells() {
        return cells;
    }

    @Override
    public void setParent(GameController parent) {
        this.parent = parent;

    }
    
    public void setActive() {
        this.isActive = true;
    }

    public void bindSizes(Scene scene) {
        ReadOnlyDoubleProperty width = scene.widthProperty();
        ReadOnlyDoubleProperty height = scene.heightProperty();
        anchor.prefWidthProperty().bind(width.multiply(0.375));
        anchor.prefHeightProperty().bind(height.multiply(2.0 / 3.0));
        grid.prefWidthProperty().bind(width.multiply(0.375));
        grid.prefHeightProperty().bind(height.multiply(2.0 / 3.0));
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof Ship) {
            Ship ship = (Ship) arg;
            if (!ship.isPlaced()) {
                switch (ship.getOrientation()) {
                case HORIZONTAL:
                    for (int i = 0; i < ship.getSize(); i++) {
                        if (ship.getPosX() + ship.getSize() > 10) {
                            if (ship.getPosX() + i >= 10) {
                                return;
                            }
                            cellss[ship.getPosY()][ship.getPosX() + i].outOfBounds(ship, i);
                        } else {
                            if (parent.getModel().getGame().getPlayer1Field().isSpaceOccupied(ship)) {
                                cellss[ship.getPosY()][ship.getPosX() + i].outOfBounds(ship, i);
                            } else {
                                cellss[ship.getPosY()][ship.getPosX() + i].enableGhost(ship, i);
                            }

                        }

                    }
                    break;
                case VERTICAL:
                    for (int i = 0; i < ship.getSize(); i++) {
                        if (ship.getPosY() + ship.getSize() > 10) {
                            if (ship.getPosY() + i >= 10) {
                                return;
                            }
                            cellss[ship.getPosY() + i][ship.getPosX()].outOfBounds(ship, i);
                        } else {
                            if (parent.getModel().getGame().getPlayer1Field().isSpaceOccupied(ship)) {
                                cellss[ship.getPosY() + i][ship.getPosX()].outOfBounds(ship, i);
                            } else {
                                cellss[ship.getPosY() + i][ship.getPosX()].enableGhost(ship, i);
                            }

                        }

                    }
                    break;
                default:
                    break;

                }
            } else {
                switch (ship.getOrientation()) {
                case HORIZONTAL:
                    for (int i = 0; i < ship.getSize(); i++) {
                        cellss[ship.getPosY()][ship.getPosX() + i].place(ship, i);
                    }
                    break;
                case VERTICAL:
                    for (int i = 0; i < ship.getSize(); i++) {
                        cellss[ship.getPosY() + i][ship.getPosX()].place(ship, i);
                    }
                    break;
                default:
                    break;

                }
            }
        } else if(o instanceof Field) {
            Pair<Point, Boolean> p = (Pair<Point, Boolean>) arg;
            Point point = p.getVar1();
            boolean hit = p.getVar2();
            if(hit) {
                cellss[point.getY()][point.getX()].mark(Color.RED);
            } else {
                cellss[point.getY()][point.getX()].mark(Color.YELLOW);
            }
        }
        

    }

}
