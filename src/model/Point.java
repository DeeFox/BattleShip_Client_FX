package model;

public class Point {
	private int x;
	private int y;
	
	public Point(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public String toString() {
		return "(" + this.x + "," + this.y + ")";
	}

	public String toBSString() {
		return "" + Field.getCharForNumber(this.y+1) + "" + (this.x+1);
	}
	
	public boolean equals(Point op) {
		if(op == null)
			return false;
		return (op.getX() == this.x && op.getY() == this.y);
	}
	
}
