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
    
    private final boolean DEBUG = true;

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
    private boolean[][] destroyedShips;
    private ArrayList<Point> currentShip;
    private boolean hardMode = true;

    public AIPlayer() {
        super("Computer");

        this.tries = new boolean[10][10];
        this.hits = new boolean[10][10];
        this.fires = new boolean[10][10];
        this.destroyedShips = new boolean[10][10];
        this.currentShip = new ArrayList<Point>();
    }

    private boolean alreadyTriedOnAdjascent(Point p) {
        Point[] pts = new Point[8];
        pts[0] = new Point(p.getX(), p.getY() - 1);
        pts[1] = new Point(p.getX() - 1, p.getY());
        pts[2] = new Point(p.getX() + 1, p.getY());
        pts[3] = new Point(p.getX(), p.getY() + 1);
        pts[4] = new Point(p.getX() - 1, p.getY() - 1);
        pts[5] = new Point(p.getX() + 1, p.getY() - 1);
        pts[6] = new Point(p.getX() - 1, p.getY() + 1);
        pts[7] = new Point(p.getX() + 1, p.getY() + 1);


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
        debug("Searching better search coord");
        boolean validCoord = false;
        Point tmp = new Point(-1, -1);
        while (!validCoord) {
            int ty = rnd.nextInt(10);
            int tx = ty % 2;
            tx += (rnd.nextInt(5) * 2);
            tmp = new Point(ty, tx);
            if (!alreadyFiredHere(tmp) && !isCellExcluded(tmp) && Field.isValidPoint(tmp))
                validCoord = true;
            
            debug("Trying " + ty + "," + tx + " isValid: " + validCoord);
        }
        this.tries[tmp.getX()][tmp.getY()] = true;
        debug("Result: " + tmp.getX() + ", " + tmp.getY());
        return tmp;
    }

    private void debug(String string) {
        if(DEBUG) {
            System.out.println("++ " + string);
        }
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
            
            debug("Searching 2nd field. Trying:");
            ArrayList<Point> newTries = new ArrayList<Point>(tries);
            for (Point tr : tries) {
                debug("Field " + tr.toBSString() + ": isValid:" + Field.isValidPoint(tr));
                if(Field.isValidPoint(tr)) {
                    debug("alreadyFiredHere: " + alreadyFiredHere(tr) + ", isExcluded: " + isCellExcluded(tr));
                }
                if (!Field.isValidPoint(tr) || alreadyFiredHere(tr) || isCellExcluded(tr)) {
                    newTries.remove(tr);
                    debug("-> This try is out.");
                }
            }
            tries = newTries;
            
            debug("Result: " + tries.toString());
            int size = tries.size();
            int pos = rnd.nextInt(size);
            Point theTry = tries.get(pos);
            debug("We choose: " + theTry.toBSString());
            return theTry;
        } else if (mode.equals("destroy")) {
            Point target = new Point(this.lastCoord.getX() + this.destroyDirX, this.lastCoord.getY() + this.destroyDirY);
            debug("Destroying Ship!");
            debug("Field " + target.toBSString() + ": isValid:" + Field.isValidPoint(target));
            if(Field.isValidPoint(target)) {
                debug("lastHit: " + this.wasLastHit + ", alreadyFiredHere: " + alreadyFiredHere(target) + ", isExcluded: " + isCellExcluded(target));
            }
            if (!Field.isValidPoint(target) || !this.wasLastHit || alreadyFiredHere(target) || isCellExcluded(target)) {
                this.destroyDirX = this.destroyDirX * -1;
                this.destroyDirY = this.destroyDirY * -1;
                this.lastCoord = this.firstHitCoord;
                
                target = new Point(this.lastCoord.getX() + this.destroyDirX, this.lastCoord.getY() + this.destroyDirY);
                debug("The Target is " + target.toBSString());
            }
            return target;
        }
        return null;
    }


    private boolean isCellExcluded(Point p) {
        Point[] pts = new Point[8];
        pts[0] = new Point(p.getX(), p.getY() - 1);
        pts[1] = new Point(p.getX() - 1, p.getY());
        pts[2] = new Point(p.getX() + 1, p.getY());
        pts[3] = new Point(p.getX(), p.getY() + 1);
        pts[4] = new Point(p.getX() - 1, p.getY() - 1);
        pts[5] = new Point(p.getX() + 1, p.getY() - 1);
        pts[6] = new Point(p.getX() - 1, p.getY() + 1);
        pts[7] = new Point(p.getX() + 1, p.getY() + 1);

        for (Point pt : pts) {
            if (Field.isValidPoint(pt)) {
                if (this.destroyedShips[pt.getX()][pt.getY()])
                    return true;
            }
        }
        return false;
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
        debug("Firing at " + target.toBSString());

        FireState fs = handler.getGame().getOtherPlayer(this).getField().evalTurn(target);
        this.fires[target.getX()][target.getY()] = true;

        this.lastCoord = target;

        if (!fs.equals(FireState.NOHIT)) {
            this.hits[target.getX()][target.getY()] = true;
            debug("We hit " + target.toBSString());

            if (this.mode == "search") {
                this.lastHitCoord = target;
                this.firstHitCoord = target;
                this.mode = "search2nd";
                debug("Switch to SEARCH2nd");
            } else if (this.mode == "search2nd") {
                this.destroyDirX = target.getX() - this.lastHitCoord.getX();
                this.destroyDirY = target.getY() - this.lastHitCoord.getY();
                this.mode = "destroy";
                debug("Switch to DESTROY");
            }
            
            // Register as Hit for current Ship
            this.currentShip.add(target);
            
            if (fs.equals(FireState.FULLHIT)) {
                this.mode = "search";
                debug("FULL HIT, Switching to SEARCH");
                
                // Register full hit
                for(Point p : this.currentShip) {
                    this.destroyedShips[p.getX()][p.getY()] = true;
                }
                this.currentShip.clear();
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
