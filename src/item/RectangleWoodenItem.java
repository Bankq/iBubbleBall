package item;

import logic.Position;
import logic.Vector;

public class RectangleWoodenItem extends WoodenItem implements RectangleArea{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2737260950551814294L;
	private Position[] vertex;
	
	/**
	 * Default constructor
	 */
	public RectangleWoodenItem() {
		super();
		vertex = null;
	}
	
	/**
	 * Constructor with position
	 * @param pos
	 */
	public RectangleWoodenItem(Position pos) {
		super(pos);
		vertex = null;
	}
	
	/**
	 * Constructor with position and vertex
	 * @param pos
	 * @param vertex
	 */
	public RectangleWoodenItem(Position pos, Position[] vertex) {
		super(pos);
		this.vertex = vertex;
	}
	
	public Position[] getVertex() {
		return vertex;
	}

	/**
	 * Set a new vertex array.
	 * Position of the item would change correspondingly.
	 * 
	 * Warning:
	 * This function only checks the validity of the array.
	 * Programmer MUST ensure points in the array are legal and are able to form a rectangle.
	 */
	public void setVertex(Position[] vertex) {
		if (vertex != null && vertex.length != vertexCnt) System.err.println("Warning in setVertex(): length of the given array is less than vertexCnt");
		int xSum = 0, ySum = 0;
		for (int i=0;i<vertexCnt;i++) {
			xSum += vertex[i].getX();
			ySum += vertex[i].getY();
		}
		this.getPosition().setX(xSum / vertexCnt);
		this.getPosition().setY(ySum / vertexCnt);
		this.vertex = vertex;
	}
	
	/**
	 * Move the item to newPos and update the coordinate of its vertices
	 */
	public void updateVertex(Position newPos) {
		int xOffset = newPos.getX() - this.getPosition().getX();
		int yOffset = newPos.getY() - this.getPosition().getY();
		
		for (int i=0;i<vertexCnt;i++) {
			this.vertex[i].setX(this.vertex[i].getX() + xOffset);
			this.vertex[i].setY(this.vertex[i].getY() + yOffset);
		}
		this.setPosition(newPos);
	}
	
	/**
	 * Rotate the item and change the position of its vertices
	 * @param angle
	 */
	public void rotateVertex(double angle) {
		for (int i=0;i<vertexCnt;i++) {
			Vector vec = new Vector(vertex[i].getX()- this.getPosition().getX(), vertex[i].getY()-this.getPosition().getY());
			Vector vec2 = new Vector(vec.getX()*Math.cos(angle) - vec.getY()*Math.sin(angle), vec.getX()*Math.sin(angle)+vec.getY()*Math.cos(angle));
			vertex[i].setXY((int)(vec2.getX() + this.getPosition().getX()), (int)(vec2.getY() + this.getPosition().getY()));
		}
	}
	
	/* £¨·Ç Javadoc£©
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		RectangleWoodenItem cloned = (RectangleWoodenItem) super.clone();
		cloned.vertex = new Position[vertexCnt];
		for (int i=0;i<vertexCnt;i++)
			cloned.vertex[i] = (Position) this.vertex[i].clone();
		return cloned;
	}
}
