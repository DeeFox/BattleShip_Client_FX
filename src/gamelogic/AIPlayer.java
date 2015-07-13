package gamelogic;

import gamelogic.GameHandler.FireState;

import java.util.ArrayList;
import java.util.Random;

import model.Field;
import model.Game.GameState;
import model.Player;
import model.Point;
import model.Ship;
import model.Ship.Orientation;

public class AIPlayer extends Player {

    private Random rnd = new Random();

    // AI vars
    private String mode = "search";
    private int destroyDirX;
    private int destroyDirY;
    private Point lastHitCoord;
    private Point lastCoord;
    private Point firstHitCoord;
    private boolean wasLastHit = false;
    private boolean[][] tries;
    private boolean[][] hits;
    private boolean[][] fires;
    private boolean hardMode = true;

    public AIPlayer() {
        super("Computer");

        this.tries = new boolean[10][10];
        this.hits = new boolean[10][10];
        this.fires = new boolean[10][10];
    }

    private boolean alreadyTriedOnAdjascent(Point p) {
        Point[] pts = new Point[4];
        pts[0] = new Point(p.getX(), p.getY() - 1);
        pts[1] = new Point(p.getX() - 1, p.getY());
        pts[2] = new Point(p.getX() + 1, p.getY());
        pts[3] = new Point(p.getX(), p.getY() + 1);

        for (Point pt : pts) {
            if (Field.isValidPoint(pt)) {
                if (this.tries[pt.getX()][pt.getY()] || this.hits[pt.getX()][pt.getY()])
                    return true;
            }
        }
        return false;
    }

    private boolean alreadyFiredHere(Point p) {
        return this.fires[p.getX()][p.getY()];
    }

    private Point calcBetterSearchCoord() {
        boolean validCoord = false;
        Point tmp = new Point(-1, -1);
        while (!validCoord) {
            int ty = rnd.nextInt(10);
            int tx = ty % 2;
            tx += (rnd.nextInt(5) * 2);
            tmp = new Point(ty, tx);
            if (!alreadyFiredHere(tmp) && !alreadyTriedOnAdjascent(tmp) && Field.isValidPoint(tmp))
                validCoord = true;
        }
        this.tries[tmp.getX()][tmp.getY()] = true;
        return tmp;
    }

    private Point calcTurnCoord() {
        if (mode.equals("search")) {
            if (this.hardMode)
                return calcBetterSearchCoord();

            // Random Coord
            boolean validCoord = false;
            Point tmp = new Point(-1, -1);
            while (!validCoord) {
                tmp = new Point(rnd.nextInt(10), rnd.nextInt(10));
                if (!alreadyFiredHere(tmp))
                    validCoord = true;
            }
            this.tries[tmp.getX()][tmp.getY()] = true;
            return tmp;
        } else if (mode.equals("search2nd")) {
            ArrayList<Point> tries = new ArrayList<Point>();
            Point lh = this.lastHitCoord;
            tries.add(new Point(lh.getX(), lh.getY() - 1));
            tries.add(new Point(lh.getX() - 1, lh.getY()));
            tries.add(new Point(lh.getX(), lh.getY() + 1));
            tries.add(new Point(lh.getX() + 1, lh.getY()));

            ArrayList<Point> newTries = new ArrayList<Point>(tries);
            for (Point tr : tries) {
                if (!Field.isValidPoint(tr) || alreadyFiredHere(tr)) {
                    newTries.remove(tr);
                }
            }
            tries = newTries;

            int size = tries.size();
            int pos = rnd.nextInt(size);
            return tries.get(pos);
        } else if (mode.equals("destroy")) {
            Point target = new Point(this.lastCoord.getX() + this.destroyDirX, this.lastCoord.getY() + this.destroyDirY);
            if (!Field.isValidPoint(target) || !this.wasLastHit || alreadyFiredHere(target)) {
                this.destroyDirX = this.destroyDirX * -1;
                this.destroyDirY = this.destroyDirY * -1;
                this.lastCoord = this.firstHitCoord;

                target = new Point(this.lastCoord.getX() + this.destroyDirX, this.lastCoord.getY() + this.destroyDirY);
            }
            return target;
        }
        return null;
    }


    public void placeShip(GameHandler handler) {
        Field aiField = this.field;

        // Try to place all ships randomly
        for (Ship s : aiField.getShips()) {
            boolean placed = false;
            while (!placed) {
                Orientation o = (rnd.nextBoolean()) ? Orientation.HORIZONTAL : Orientation.VERTICAL;
                Point pos = new Point(rnd.nextInt(10), rnd.nextInt(10));
                s.setPosition(pos);
                s.setOrientation(o);
                boolean occupied = aiField.isSpaceOccupied(s);
                if (!occupied) {
                    aiField.placeShip(s);
                    placed = true;
                }
            }
        }
        
        handler.getGame().setGameState(GameState.YOUR_TURN);

    }

    
    public void makeTurn(GameHandler handler) {
        Point target = calcTurnCoord();
        System.out.println("Firing at " + target.toBSString());

        FireState fs = handler.getGame().getOtherPlayer(this).getField().evalTurn(target);
        this.fires[target.getX()][target.getY()] = true;

        this.lastCoord = target;

        if (!fs.equals(FireState.NOHIT)) {
            this.hits[target.getX()][target.getY()] = true;
            

            if (this.mode == "search") {
                this.lastHitCoord = target;
                this.firstHitCoord = target;
                this.mode = "search2nd";
            } else if (this.mode == "search2nd") {
                this.destroyDirX = target.getX() - this.lastHitCoord.getX();
                this.destroyDirY = target.getY() - this.lastHitCoord.getY();
                this.mode = "destroy";
            }
            
            if (fs.equals(FireState.FULLHIT)) {
                this.mode = "search";
                //return;
            }
            this.wasLastHit = true;
            
        } else {
            this.wasLastHit = false;
        }
       
        handler.handleFireState(fs, target, this, handler.getGame().getOtherPlayer(this));

        Player other = handler.getGame().getOtherPlayer(this);
        if (other.getField().allShipsDestroyed()) {
            handler.getGame().setGameState(GameState.YOU_LOST);
        } else {
            handler.getGame().setGameState(GameState.YOUR_TURN);
        }

    }

}
