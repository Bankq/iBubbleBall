package item;

import logic.Position;
import logic.Vector;

public class Bat extends DynamicItem implements RectangleArea{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8575963505124587192L;
	public static final int ANGLE_SPEED = 100;		// This is not decided yet
	private Position[] vertex;
	
	/**
	 * Default constructor
	 */
	public Bat() {
		
	}
	
	/**
	 * Constructor with position
	 * @param pos
	 */
	public Bat(Position pos) {
		super(pos);
	}
	
	/**
	 * Constructor with position and radius
	 * @param pos
	 * @param radius
	 */
	public Bat(Position pos, int radius) {
		super(pos, radius);
	}

	public Position[] getVertex() {
		return vertex;
	}

	public void setVertex(Position[] vertex) {
		if (vertex != null && vertex.length != vertexCnt) System.err.println("Warning in setVertex(): length of the given array is less than vertexCnt");
		this.vertex = vertex;
		this.getPosition().setXY((vertex[0].getX()+vertex[3].getX())/2, (vertex[0].getY()+vertex[3].getY())/2);
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
		Bat cloned = (Bat) super.clone();
		cloned.vertex = new Position[vertexCnt];
		for (int i=0;i<vertexCnt;i++)
			cloned.vertex[i] = (Position) this.vertex[i].clone();
		return cloned;
	}
}
