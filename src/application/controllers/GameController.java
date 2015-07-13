package application.controllers;

import java.util.Observable;
import java.util.Observer;

import gamelogic.GameHandler;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Ship;

public class GameController implements Observer {
    
    @FXML
    private AnchorPane anchor;
    
    @FXML
    private VBox root;
    
    @FXML
    private BorderPane fieldContainer;
    
    @FXML
    private BorderPane chatContainer;
    
    @FXML
    private HBox leftContainer;
    
    @FXML 
    private HBox rightContainer;
    
    @FXML
    private AnchorPane leftListContainer;
    
    @FXML
    private AnchorPane leftFieldContainer;
    
    @FXML
    private AnchorPane rightListContainer;
    
    @FXML
    private AnchorPane rightFieldContainer;
    
    @FXML
    private BorderPane leftChatContainer;
    
    @FXML
    private BorderPane rightChatContainer;
    
    @FXML
    private TextArea leftTextArea;
    
    @FXML
    private TextArea rightTextArea;
    
    @FXML
    private GridPane leftInfoPanel;
    
    @FXML
    private GridPane rightInfoPanel;
    
    @FXML
    private GridPane leftButtonPanel;
    
    @FXML
    private GridPane rightButtonPanel;

    @FXML
    private ListController fleet1Controller;

    @FXML
    private ListController fleet2Controller;
    
    @FXML
    private FieldController field1Controller;
    
    @FXML
    private FieldController field2Controller;
    
    private GameHandler model;

    @FXML
    private void initialize() {
        
        fleet1Controller.setParent(this);
        field1Controller.setParent(this);
        field1Controller.setActive();
        field2Controller.setParent(this);
        this.bindSizes();
    }
    
    private void bindSizes() {
        Platform.runLater(new Runnable() {
            
            @Override
            public void run() {
                Scene scene = anchor.getScene();
                ReadOnlyDoubleProperty width = scene.widthProperty();
                ReadOnlyDoubleProperty height = scene.heightProperty();
                anchor.prefWidthProperty().bind(width);
                anchor.prefHeightProperty().bind(height);
                root.prefWidthProperty().bind(width);
                root.prefHeightProperty().bind(height);
                fieldContainer.prefWidthProperty().bind(width);
                fieldContainer.prefHeightProperty().bind(height.multiply(2.0 / 3.0));
                chatContainer.prefWidthProperty().bind(width);
                chatContainer.prefHeightProperty().bind(height.multiply(1.0 / 3.0));
                leftContainer.prefWidthProperty().bind(width.multiply(0.46875));
                leftContainer.prefHeightProperty().bind(height.multiply(2.0 / 3.0));
                rightContainer.prefWidthProperty().bind(width.multiply(0.46875));
                rightContainer.prefHeightProperty().bind(height.multiply(2.0 / 3.0));
                leftListContainer.prefWidthProperty().bind(width.multiply(0.09375));
                leftListContainer.prefHeightProperty().bind(height.multiply(2.0 / 3.0));
                rightListContainer.prefWidthProperty().bind(width.multiply(0.09375));
                rightListContainer.prefHeightProperty().bind(height.multiply(2.0 / 3.0));
                leftFieldContainer.prefWidthProperty().bind(width.multiply(0.375));
                leftFieldContainer.prefHeightProperty().bind(height.multiply(2.0 / 3.0));
                rightFieldContainer.prefWidthProperty().bind(width.multiply(0.375));
                rightFieldContainer.prefHeightProperty().bind(height.multiply(2.0 / 3.0));
                leftChatContainer.prefWidthProperty().bind(width.multiply(0.46875));
                leftChatContainer.prefHeightProperty().bind(height.multiply(2.5 / 9.0));
                rightChatContainer.prefWidthProperty().bind(width.multiply(0.46875));
                rightChatContainer.prefHeightProperty().bind(height.multiply(2.5 / 9.0));
                leftTextArea.prefWidthProperty().bind(width.multiply(0.46875));
                leftTextArea.prefHeightProperty().bind(height.multiply(0.2));
                rightTextArea.prefWidthProperty().bind(width.multiply(0.46875));
                rightTextArea.prefHeightProperty().bind(height.multiply(0.2));
                leftInfoPanel.prefWidthProperty().bind(width.multiply(0.46875));
                leftInfoPanel.prefHeightProperty().bind(height.multiply(0.5 / 9.0));
                leftButtonPanel.prefWidthProperty().bind(width.multiply(0.46875));
                leftButtonPanel.prefHeightProperty().bind(height.multiply(0.5 / 9.0));
                rightInfoPanel.prefWidthProperty().bind(width.multiply(0.46875));
                rightInfoPanel.prefHeightProperty().bind(height.multiply(0.5 / 9.0));
                rightInfoPanel.prefWidthProperty().bind(width.multiply(0.46875));
                rightInfoPanel.prefHeightProperty().bind(height.multiply(0.5 / 9.0));
                fleet1Controller.bindSizes(scene);
                fleet2Controller.bindSizes(scene);
                field1Controller.bindSizes(scene);
                field2Controller.bindSizes(scene);
            }
        });
    }
    
    public void setModel(GameHandler gh) {
        this.model = gh;
        gh.addObserver(this);
        fleet1Controller.setModel(model.getGame().getPlayer1().getField().getShips(), true);
        fleet2Controller.setModel(model.getGame().getPlayer2().getField().getShips(), false);
        for(Ship s : model.getGame().getPlayer1Field().getShips()) {
            s.addObserver(field1Controller);
        }
        for(Ship s : model.getGame().getPlayer2Field().getShips()) {
            s.addObserver(field2Controller);
        }
        this.getModel().getGame().getPlayer1Field().addObserver(field1Controller);
        this.getModel().getGame().getPlayer2Field().addObserver(field2Controller);
       
    }
    
    public GameHandler getModel() {
        return model;
    }

    @Override
    public void update(Observable o, Object arg) {
        

    }




}
