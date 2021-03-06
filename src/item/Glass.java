package item;

import logic.Position;
import logic.Vector;

public class Glass extends ConcreteItem implements Unmovable, RectangleArea{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1272325501557723213L;
	private double energyThreshold;
	private boolean disappear;
	private Position[] vertex;
	
	/**
	 * Default Constructor
	 */
	public Glass() {
		this.energyThreshold = 0; 
		this.vertex = null;
	}
	
	/**
	 * Constructor with position
	 * @param pos
	 */
	public Glass(Position pos) {
		super(pos);
		this.energyThreshold = 0;
		this.vertex = null;
	}
	
	/**
	 * Constructor with position and threshold
	 * @param pos
	 * @param energyThreshold
	 */
	public Glass(Position pos, double energyThreshold) {
		super(pos);
		this.energyThreshold = energyThreshold;
		this.vertex = null;
	}
	
	public Glass(Position pos, double energyThreshold, Position[] vertex) {
		super(pos);
		this.energyThreshold = energyThreshold;
		this.vertex = vertex;
	}
	
	/**
	 * Decide if the specified speed could pass the threshold check
	 * @param speed
	 * @return
	 */
	public boolean pass(float mass, Vector speed) {
		double v = speed.getMagnitude();
		double energy = .5 * mass * v * v;
		return energy > energyThreshold;
	}

	/**
	 * @return energyThreshold
	 */
	public double getEnergyThreshold() {
		return energyThreshold;
	}

	/**
	 * @param energyThreshold 要设置的 energyThreshold
	 */
	public void setEnergyThreshold(double energyThreshold) {
		this.energyThreshold = energyThreshold;
	}

	/**
	 * @return disappear
	 */
	public boolean isDisappear() {
		return disappear;
	}

	/**
	 * @param disappear 要设置的 disappear
	 */
	public void setDisappear(boolean disappear) {
		this.disappear = disappear;
	}

	public Position[] getVertex() {
		return vertex;
	}

	public void setVertex(Position[] vertex) {
		if (vertex != null && vertex.length != vertexCnt) System.err.println("Warning in setVertex(): length of the given array is less than vertexCnt");
		int xSum = 0, ySum = 0;
		for (int i=0;i<vertexCnt;i++) {
			xSum += vertex[i].getX();
			ySum += vertex[i].getY();
		}
		this.getPosition().setX(xSum / 4);
		this.getPosition().setY(ySum / 4);
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
	
	/* （非 Javadoc）
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		Glass cloned = (Glass) super.clone();
		cloned.disappear = this.disappear;
		cloned.energyThreshold = this.energyThreshold;
		cloned.vertex = new Position[vertexCnt];
		for (int i=0;i<vertexCnt;i++)
			cloned.vertex[i] = (Position) this.vertex[i].clone();
		return cloned;
	}
}
