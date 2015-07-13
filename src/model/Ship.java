package model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import javafx.scene.image.Image;

public class Ship extends Observable {
	public enum ShipType {
		BATTLESHIP	("Battleship", 0, 5),
		CRUISER		("Cruiser", 1, 4),
		DESTROYER	("Destroyer", 2, 3),
		SUBMARINE	("Submarine", 3, 2),
		MARKER		("Marker", 4, 1);
		
		public final String name;
		public final int id;
		public final int size;
		
		private static final Map<String, ShipType> shortNames;
	    static
	    {
	    	shortNames = new HashMap<String, ShipType>();
	    	shortNames.put("B", ShipType.BATTLESHIP);
	    	shortNames.put("C", ShipType.CRUISER);
	    	shortNames.put("D", ShipType.DESTROYER);
	    	shortNames.put("S", ShipType.SUBMARINE);
	    }
		
		private ShipType(String name, int id, int size) {
			this.id = id;
			this.name = name;
			this.size = size;
		}
		
		public static ShipType getFromShort(String sh) {
			return ShipType.shortNames.get(sh);
		}
		public static String getFromType(ShipType t) {
		    for(String k : shortNames.keySet()) {
		        if(t.equals(shortNames.get(k))) {
		            return k;
		        }
		    }
		    return "";
		}
	}
	
	public enum Orientation {
		HORIZONTAL (1,  0), 
		VERTICAL   (0,  1);
		
		public final int x;
		public final int y;
		
		private Orientation(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public char shortName() {
			return (this.equals(Orientation.HORIZONTAL)) ? 'h' : 'v';
		}
		
		public static Orientation fromString(String o) {
            return (o.equals("h")) ? Orientation.HORIZONTAL : Orientation.VERTICAL;
        }
	}
	
	private int id;
	private int posX;
	private int posY;
	private Orientation orientation;
	private ShipType type;
	private int hitCounter = 0;
	private boolean[] hits;
	private boolean isPlaced = false;
	private boolean isSelected = false;
	
	private Map<Integer, Image> imageRes;

	
	public Ship(ShipType type, int id, Point pos, Orientation orientation) {
		this.type = type;
		this.id = id;
		this.posX = pos.getX();
		this.posY = pos.getY();
		this.orientation = orientation;
		this.hits = new boolean[type.size];
		this.imageRes = new HashMap<>();
		this.loadImages();
	}
	
	private void loadImages() {
	    switch (this.type) {
        case BATTLESHIP:
            imageRes.put(1, new Image("ships_r7_c1.png"));
            imageRes.put(2, new Image("ships_r7_c2.png"));
            imageRes.put(3, new Image("ships_r7_c3.png"));
            imageRes.put(4, new Image("ships_r7_c4.png"));
            imageRes.put(5, new Image("ships_r7_c5.png"));
            imageRes.put(-1, new Image("ships_r8_c1.png"));
            imageRes.put(-2, new Image("ships_r8_c2.png"));
            imageRes.put(-3, new Image("ships_r8_c3.png"));
            imageRes.put(-4, new Image("ships_r8_c4.png"));
            imageRes.put(-5, new Image("ships_r8_c5.png"));
            break;
        case CRUISER:
            imageRes.put(1, new Image("ships_r1_c1.png"));
            imageRes.put(2, new Image("ships_r1_c2.png"));
            imageRes.put(3, new Image("ships_r1_c3.png"));
            imageRes.put(4, new Image("ships_r1_c4.png"));
            imageRes.put(-1, new Image("ships_r2_c1.png"));
            imageRes.put(-2, new Image("ships_r2_c2.png"));
            imageRes.put(-3, new Image("ships_r2_c3.png"));
            imageRes.put(-4, new Image("ships_r2_c4.png"));
            break;
        case DESTROYER:
            imageRes.put(1, new Image("ships_r6_c1.png"));
            imageRes.put(2, new Image("ships_r6_c2.png"));
            imageRes.put(3, new Image("ships_r6_c3.png"));
            imageRes.put(-1, new Image("ships_r5_c1.png"));
            imageRes.put(-2, new Image("ships_r5_c2.png"));
            imageRes.put(-3, new Image("ships_r5_c3.png"));
            break;
        case MARKER:
            break;
        case SUBMARINE:
            imageRes.put(1, new Image("ships_r4_c1.png"));
            imageRes.put(2, new Image("ships_r4_c2.png"));
            imageRes.put(-1, new Image("ships_r3_c1.png"));
            imageRes.put(-2, new Image("ships_r3_c2.png"));
            break;
        default:
            break;
        
        }
	}
	
	public Image getImage(int i) {
	    switch(i) {
	    case 0:
	        if(hits[i]) {
	            return imageRes.get(-1);
	        }
	        return imageRes.get(1);
	    case 1:
	        if(hits[i]) {
                return imageRes.get(-2);
            }
            return imageRes.get(2);
	    case 2:
	        if(hits[i]) {
                return imageRes.get(-3);
            }
            return imageRes.get(3);
	    case 3:
	        if(hits[i]) {
                return imageRes.get(-4);
            }
            return imageRes.get(4);
	    case 4:
	        if(hits[i]) {
                return imageRes.get(-5);
            }
            return imageRes.get(5);
	    default:
	        System.out.println("this should not happen: " + i);
	        return null;
	    }
	}
	
	public void registerHit(Point p) {
        for(int i = 0; i < type.size; i++) {
            int pX = posX + (orientation.x * i);
            int pY = posY + (orientation.y * i);
            if(pX == p.getX() && pY == p.getY()) {
                hits[i] = true;
            }
        }
    }
	
	public void damageAt(int i) {
	    this.hits[i] = true;
	}
	
	public void updateHits(boolean[] hits) {
	    this.hits = hits;
	}
	
	public boolean isHit(int x, int y) {
	    switch(this.orientation) {
        case HORIZONTAL:
            if(x >= this.posX && x <= this.posX + this.getSize()) {
                return hits[x - this.posX - 1];
            } else {
                return false;
            }
            
        case VERTICAL:
            if(y >= this.posY && y <= this.posY + this.getSize()) {
                return hits[y - this.posY - 1];
            } else {
                return false;
            }
        default:
            return false;
	    
	    }
	}
	
	public boolean[] getHits() {
	    return hits;
	}
	
	public void select() {
	    isSelected = true;
	    /*
	    this.setChanged();
	    this.notifyObservers(this);
	    */
	}
	
	public void deselect() {
	    isSelected = false;
	    /*
	    this.setChanged();
	    this.notifyObservers(this);
	    */
	}
	
	public boolean isSelected() {
	    return isSelected;
	}
	
	public boolean isDestroyed() {
	    boolean destroyed = true;
        for(boolean h : this.hits) {
            if(!h) {
                destroyed = false;
            }
        }
        return destroyed;
	}
	
	public String getShipID() {
	    return ShipType.getFromType(this.type) + id;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public Orientation getOrientation() {
		return orientation;
	}

	public ShipType getType() {
		return type;
	}
	
	public int getSize() {
		return this.type.size;
	}
	
	public int getHitCounts() {
		return this.hitCounter;
	}
	
	public String toString() {
		return this.type.name;
	}

	public boolean isPlaced() {
		return isPlaced;
	}

	public void setPlaced(boolean isPlaced) {
		this.isPlaced = isPlaced;
		this.setChanged();
        this.notifyObservers(this);
	}
	
	public void setPosition(Point p) {
		this.posX = p.getX();
		this.posY = p.getY();
		this.setChanged();
		this.notifyObservers(this);
	}
	
	public void setOrientation(Orientation o) {
		this.orientation = o;
		this.setChanged();
		this.notifyObservers(this);
	}
	
	public Point getPos() {
		return new Point(this.posX, this.posY);
	}
	
	public String toString2() {
		return this.type.name + ", " + this.type.size + ", " + this.posX + ", " + this.posY + ", " + this.orientation.shortName() + ", " + this.isPlaced;
	}	
}
