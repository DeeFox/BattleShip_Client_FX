package model;

import java.util.ArrayList;
import java.util.Observable;

import gamelogic.GameHandler.FireState;
import model.Ship.Orientation;
import model.Ship.ShipType;
import util.Pair;

public class Field extends Observable {
	
	private boolean[][] opponentShots;
	private boolean[][] hits;
	private ArrayList<Ship> ships;
	private Ship[][] fields;
	
	private int[] shipCountLimits;
	
	private boolean multiplayerField;
	
	public Field() {
		this.shipCountLimits = new int[4];
		this.shipCountLimits[0] = 1;
		this.shipCountLimits[1] = 2;
		this.shipCountLimits[2] = 3;
		this.shipCountLimits[3] = 4;
		
		this.ships = new ArrayList<Ship>();
		
		this.fields = new Ship[10][10];
		this.opponentShots = new boolean[10][10];
		this.hits = new boolean[10][10];
	}
	
	public void markAsMultiPlayerField() {
		this.multiplayerField = true;
	}
	
	public boolean isMultiPlayerField() {
		return this.multiplayerField;
	}
	
	public boolean shipTypeCanBePlaced(ShipType type) {
		return (this.shipCountLimits[type.id] > 0);
	}
	
	// TODO
	public boolean placeShip(Ship ship) {
		ShipType type = ship.getType();
		
		// Check if already all ships of this type were set
		if(!shipTypeCanBePlaced(type))
			return false;
		
		// Check for collisions
		if(isSpaceOccupied(ship))
			return false;
		
		// Place ship
		if(!this.ships.contains(ship))
			this.ships.add(ship);
		ship.setPlaced(true);
		
		for(int i = 0; i < ship.getType().size; i++) {
			int posX = ship.getPosX() + (ship.getOrientation().x * i);
			int posY = ship.getPosY() + (ship.getOrientation().y * i);
			this.fields[posX][posY] = ship;
		}
		
		this.shipCountLimits[type.id]--;
		return true;
	}
	
	public boolean allShipsPlaced() {
//		boolean shipsLeft = false;
//		for(int i : this.shipCountLimits) {
//			if(i > 0)
//				shipsLeft = true;
//		}
//		return !shipsLeft;
		
		boolean shipsLeft = false;
		for(Ship s : this.ships) {
			if(!s.isPlaced()) {
				shipsLeft = true;
			}
		}
		return !shipsLeft;
	}
	
	public boolean allShipsDestroyed() {
		boolean shipsLeft = false;
		for(Ship ship : this.ships) {
			if(!ship.isDestroyed())
				shipsLeft = true;
		}
		return !shipsLeft;
	}
	
	public boolean areAdjascentFieldsFree(Point p) {
		Point[] pts = new Point[8];
		pts[0] = new Point(p.getX(), p.getY() - 1);
		pts[1] = new Point(p.getX() - 1, p.getY());
		pts[2] = new Point(p.getX() + 1, p.getY());
		pts[3] = new Point(p.getX(), p.getY() + 1);
		
		pts[4] = new Point(p.getX() - 1, p.getY() - 1);
		pts[5] = new Point(p.getX() + 1, p.getY() - 1);
		pts[6] = new Point(p.getX() + 1, p.getY() + 1);
		pts[7] = new Point(p.getX() - 1, p.getY() + 1);
		
		for(Point pt : pts) {
			if(isValidPoint(pt)) {
				if(this.fields[pt.getX()][pt.getY()] != null)
					return false;
			}
		}
		return true;
	}
	
	public boolean isSpaceOccupied(Ship ship) {
		boolean isOccupied = false;
		for(int i = 0; i < ship.getType().size; i++) {
			int posX = ship.getPosX() + (ship.getOrientation().x * i);
			int posY = ship.getPosY() + (ship.getOrientation().y * i);
			if(posX < 0 || posX > 9 || posY < 0 || posY > 9) {
				isOccupied = true;
			} else {
				if(this.fields[posX][posY] != null || 
					!areAdjascentFieldsFree(new Point(posX, posY)))
					isOccupied = true;
			}
		}
		return isOccupied;
	}
	
	public boolean alreadyFiredHere(Point p) {
		return this.opponentShots[p.getX()][p.getY()];
	}
	
	public static boolean isValidPoint(Point p) {
		return (p.getX() >= 0 && p.getX() < 10 && p.getY() >= 0 && p.getY() < 10);
	}
	
	public Ship fire(Point p) {
		this.opponentShots[p.getX()][p.getY()] = true;
		
		Ship dest = this.fields[p.getX()][p.getY()];
		if(dest != null) {
		    dest.registerHit(p);
			return dest;
		}
		return null;
	}

	public static int posFromLetter(char letter) {
		int pos = (int) letter;
		if(pos >= 65 && pos <= 90) {
			return pos - 64;
		}
		return -1;
	}
	
	public String getFieldString(Point p) {
		Ship f = this.fields[p.getX()][p.getY()];
		if(f == null) {
			if(this.opponentShots[p.getX()][p.getY()]) {
				return "+";
			} else {
				return ".";
			}
		} else {
			if(this.opponentShots[p.getX()][p.getY()]) {
				return "X";
			} else {
				return "O";
			}
		}
	}
	
	public static String getCharForNumber(int i) {
	    return i > 0 && i < 27 ? String.valueOf((char)(i + 'A' - 1)) : null;
	}

	public ArrayList<Ship> getShips() {
		return ships;
	}
	
	public FireState evalTurn(Point target) {
		Ship ship = this.fire(target);
		if(ship == null) {
			return FireState.NOHIT;
		} else {
			if(ship.isDestroyed()) {
				return FireState.FULLHIT;
			} else {
				return FireState.HIT;
			}
		}
	}

	public void addEmptyShips() {
		String[] ships = {"B","C","C","D","D","D","S","S","S","S"};
		String last = "z";
		int cnt = 1;
		for(String s : ships) {
		    if(s.equals(last)) {
                cnt++;
            } else {
                cnt = 1;
                last = s;
            }
			ShipType shipType = ShipType.getFromShort(s);
			Ship ship = new Ship(shipType, cnt, new Point(-1, -1), Orientation.HORIZONTAL);
			this.ships.add(ship);
			
		}
	}
	
	public Ship getShipFromID(String id) {
	    for(Ship s : ships) {
	        if(id.equals(s.getShipID())) {
	            return s;
	        }
	    }
	    return null;
	}
	
	public void markAsFired(Point p, boolean hit) {
	    this.opponentShots[p.getX()][p.getY()] = true;
		if(hit)
		    this.hits[p.getX()][p.getY()] = true;
		this.setChanged();
        this.notifyObservers(new Pair<Point, Boolean>(p, hit));
	}
	
	
	public boolean isHit(Point p) {
		return this.hits[p.getX()][p.getY()];
	}
}
