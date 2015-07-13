package application;

import java.io.IOException;

import application.controllers.GameController;
import application.controllers.ListController;
import application.interfaces.Controller;
import gamelogic.AIPlayer;
import gamelogic.GameHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Game;
import model.Player;
import network.SimulatedNetworkHandler;

public class Main extends Application {

    private Scene scene;

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/application/views/MenuView.fxml"));
            GridPane root = (GridPane) fxmlloader.load();
            Controller<Main> con = fxmlloader.getController();
            con.setParent(this);

            scene = new Scene(root, 1600, 900);
            //scene.getStylesheets().add(getClass().getResource("/application/styles/application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("BattleShipFX");
            primaryStage.setOnCloseRequest(e -> Platform.exit());
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doStuff() {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/application/views/GameView.fxml"));
            Pane root = (Pane) fxmlloader.load();
            GameController controller = fxmlloader.getController();
            Player p1 = new Player("Franz");
            Player p2 = new AIPlayer();
            Game g = new Game(p1, p2);
            GameHandler gh = new GameHandler(g);
            gh.setNet(new SimulatedNetworkHandler(gh));
            controller.setModel(gh);
            scene.setRoot(root);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    
    public void doStuff2() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/FieldView.fxml"));
            GridPane root = (GridPane) loader.load();
            scene.setRoot(root);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    
    public void doStuff3() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/TestView.fxml"));
            BorderPane root = loader.load();
            ListController controller = loader.getController();
            scene.setRoot(root);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        

    }

    public static void main(String[] args) {
        launch(args);
    }
}
