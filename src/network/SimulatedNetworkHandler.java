package network;

import gamelogic.AIPlayer;
import gamelogic.GameHandler;
import gamelogic.GameHandler.FireState;

import java.util.concurrent.TimeUnit;

import model.Field;
import model.Game.GameState;
import model.Player;
import model.Point;
import model.Ship;

public class SimulatedNetworkHandler implements Runnable, NetworkHandler {

    private GameHandler handler;

    private boolean placed;
    private Ship placeTarget;

    private boolean fired;
    private Point fireTarget;

    private boolean isRunning;

    public SimulatedNetworkHandler(GameHandler h) {
        this.handler = h;
        this.isRunning = true;
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }

    @Override
    public synchronized void place(Ship ship) {
        this.placed = true;
        this.placeTarget = ship;
    }

    @Override
    public synchronized void fire(Point tar) {
        this.fired = true;

        this.fireTarget = tar;
        System.out.println(tar.toBSString());
    }

    private void simulateShipPlacement(Ship ship) {
        if (handler.getSelectedShip() == null) {
            return;
        }
        if (handler.getGame().getPlayer1Field().placeShip(handler.getSelectedShip())) {
            handler.clearSelection();
        }

        if (handler.getGame().getPlayer1Field().allShipsPlaced()) {
            System.out.println(handler.getGame().getPlayer1Field().getShips().size());
            handler.getGame().setGameState(GameState.WAITING_FOR_SHIP_PLACEMENT);
            simulateOpponentPlacement();
        }

    }

    private void simulateOpponentPlacement() {
        AIPlayer p2 = (AIPlayer) handler.getGame().getPlayer2();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        p2.placeShip(handler);
    }

    private void simulatePlayerFire(Point target) {
        Player p1 = handler.getGame().getPlayer1();
        Player p2 = handler.getGame().getPlayer2();
        if (Field.isValidPoint(target) && !p2.getField().alreadyFiredHere(target)) {
            FireState fs = p2.getField().evalTurn(target);
            System.out.println("Player fires at " + target.toBSString() + " -> " + fs.toString());
            handler.handleFireState(fs, target, p1, p2);
            if (p2.getField().allShipsDestroyed()) {
                handler.getGame().setGameState(GameState.YOU_WON);
            } else {
                handler.getGame().setGameState(GameState.WAITING_FOR_TURN);
                handler.triggerUIRedraw();
                simulateOpponentFire();
            }

        }

    }

    private void simulateOpponentFire() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AIPlayer p2 = (AIPlayer) handler.getGame().getPlayer2();
        p2.makeTurn(handler);
    }

    @Override
    public void run() {
        while (isRunning) {
            synchronized (this) {
                if (placed) {
                    simulateShipPlacement(placeTarget);
                    synchronized (this) {
                        placed = false;
                        placeTarget = null;
                    }
                }
                if (fired) {
                    simulatePlayerFire(fireTarget);
                    fired = false;
                    fireTarget = null;

                }

            }

        }
        try {
            Thread.sleep(16);
        } catch (InterruptedException e) {
            this.isRunning = false;
        }
    }

    @Override
    public void sendMsg(String msg) {
        // TODO Auto-generated method stub
        
    }

}
