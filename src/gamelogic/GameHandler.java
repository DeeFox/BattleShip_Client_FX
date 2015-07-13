package gamelogic;

import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;

import model.Game;
import model.Game.GameState;
import model.Player;
import model.Point;
import model.Ship;
import model.Ship.Orientation;
import network.NetworkHandler;
import view.GamePanel;

public class GameHandler extends Observable {

    private Ship selectedShip;
    private Point mousePosition;

    private Point playerTarget;
    public boolean clicked = false;

    public enum FireState {
        NOHIT, HIT, FULLHIT
    }

    private GamePanel gamePanel;
    private Game game;

    private NetworkHandler net;

    public GameHandler(Game game) {
        super();
        this.game = game;
        this.game.getPlayer1Field().addEmptyShips();
        this.game.getPlayer2Field().addEmptyShips();
    }

    public void setNet(NetworkHandler net) {
        this.net = net;

    }

    public Game getGame() {
        return this.game;
    }

    public void setGamePanel(GamePanel gp) {
        this.gamePanel = gp;
    }

    public void clearSelection() {
        this.selectedShip.deselect();
        this.selectedShip = null;
        //gamePanel.clearSelection();
    }

    public void sendChatMessage(String message, Player source) {
        this.game.addChatMessage(source.getName(), message);
        this.net.sendMsg(message);
    }
    
    public void receiveChatMessage(String message, Player source) {
        this.game.addChatMessage(source.getName(), message);
    }

    public void triggerUIRedraw() {
        if (this.gamePanel != null) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    gamePanel.refreshUI();

                }
            });
        }

    }

    public void selectedShipChanged(Ship ship) {
        this.selectedShip = ship;
        ship.select();
        this.setChanged();
        this.notifyObservers();
    }

    public void mousePositionChanged(Point p) {
        this.mousePosition = p;
        if (this.selectedShip != null) {
            Point np = new Point(p.getX(), p.getY());
            this.selectedShip.setPosition(np);
        }
        
    }
    
    public void clickPH() {
        if(this.selectedShip != null) {
            this.net.place(selectedShip);
        }
    }

    public void rotateSelectedShip() {
        if (selectedShip != null) {
            Orientation o = selectedShip.getOrientation().equals(Orientation.HORIZONTAL) ? Orientation.VERTICAL : Orientation.HORIZONTAL;
            this.selectedShip.setOrientation(o);
        }
    }

    public void onMouseClick(int x, int y) {
        GameState state = this.game.getGameState();
         if (state.equals(GameState.YOUR_TURN)) {

                    net.fire(new Point(x, y));
                
            }
        
    }

    public void changeState(String state) {
        GameState newState;
        switch(state) {
        case "your_turn":
            newState = GameState.YOUR_TURN;
            break;
        case "opponent_turn":
            newState = GameState.WAITING_FOR_TURN;
            break;
        case "opponent_placing_ships":
            newState = GameState.WAITING_FOR_SHIP_PLACEMENT;
            break;
        case "winner":
            newState = GameState.YOU_WON;
            this.changeBackButton();
            break;
        case "loser":
            newState = GameState.YOU_LOST;
            this.changeBackButton();
            break;
        case "player_left":
            newState = GameState.PLAYER2_LEFT;
            this.changeBackButton();
            break;
        default:
            newState = GameState.PLACE_YOUR_SHIPS;
            break;
        }
        this.game.setGameState(newState);
    }

    public Ship getSelectedShip() {
        return selectedShip;
    }

    public Point getMousePosition() {
        return mousePosition;
    }

    public Point getPlayerTarget() {
        return playerTarget;
    }
    
    private void changeBackButton() {
        if (this.gamePanel != null) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    gamePanel.morphLeaveButton();
                    gamePanel.refreshUI();

                }
            });
        }
    }

    public void handleFireState(FireState fs, Point p, Player source, Player target) {
        String coord = p.toBSString();
        if (fs.equals(FireState.NOHIT)) {
            target.getField().markAsFired(p, false);
            receiveLogMessage(source.getName(), String.format("Angriff auf %s. Kein Treffer.", coord));
        } else {
            target.getField().markAsFired(p, true);
            if (fs.equals(FireState.FULLHIT)) {
                receiveLogMessage(source.getName(), String.format("Angriff auf %s. Treffer. Schiff versenkt.", coord));
            } else {
                receiveLogMessage(source.getName(), String.format("Angriff auf %s. Treffer.", coord));
            }
        }
    }

    public void receiveLogMessage(String source, String s) {
        this.game.addLogMessage(source, s);
    }   
}
