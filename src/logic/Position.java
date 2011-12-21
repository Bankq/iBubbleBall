package logic;

import java.io.Serializable;

public class Position implements Serializable, Cloneable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5226233070292398741L;
	private int x;		// x coordinate
	private int y;		// y coordinate
	
	/**
	 * Constructor
	 * @param x
	 * @param y
	 */
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Default Constructor
	 */
	public Position() {
		this.x = 0;
		this.y = 0;
	}

	/**
	 * @return x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x x to be set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y y to be set
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 */
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public static double distanceBetween(Position p1, Position p2) {
		int xDist = p1.getX() - p2.getX();
		int yDist = p1.getY() - p2.getY();
		return Math.sqrt(xDist * xDist + yDist * yDist);
	}

	/* £¨·Ç Javadoc£©
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		Position cloned = (Position) super.clone();
		cloned.x = this.x;
		cloned.y = this.y;
		return cloned;
	}
	
	
}
