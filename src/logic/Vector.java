package logic;

import java.io.Serializable;

public class Vector implements Serializable, Cloneable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3537391058325501953L;
	private double x;		// x component
	private double y;		// y component
	
	/**
	 * Constructor
	 * @param x
	 * @param y
	 */
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Default Constructor
	 */
	public Vector() {
		this.x = 0;
		this.y = 0;
	}

	/**
	 * @return x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x x to be set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @return y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y y to be set
	 */
	public void setY(double y) {
		this.y = y;
	}
	
	public double getMagnitude() {
		return Math.sqrt(x*x+y*y);
	}
	
	/* £¨·Ç Javadoc£©
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		Vector cloned = (Vector) super.clone();
		cloned.x = this.x;
		cloned.y = this.y;
		return cloned;
	}
}
