package item;

import logic.Position;

public class AccelerationMark extends AbstractItem{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1569273244176339991L;
	public static final int LEFT_DIRECTION = 0;
	public static final int RIGHT_DIRECTION = 1;
	private int direction;
	private double velocity;
	
	/**
	 * Constructor with direction
	 * @param direction
	 */
	public AccelerationMark(int direction) {
		this.direction = direction;
	}
	
	/**
	 * Constructor with position and direction
	 * @param pos
	 * @param direction
	 */
	public AccelerationMark(Position pos, int direction) {
		super(pos);
		this.direction = direction;
	}
	
	/**
	 * Constructor with position, direction and radius
	 * @param pos
	 * @param direction
	 * @param radius
	 */
	public AccelerationMark(Position pos, int direction, int radius) {
		super(pos, radius);
		this.direction = direction;
	}
	
	/**
	 * Constructor with position, direction and velocity
	 * @param pos
	 * @param direction
	 * @param velocity
	 */
	public AccelerationMark(Position pos, int direction, double velocity) {
		super(pos);
		this.direction = direction;
		this.velocity = velocity;
	}

	/**
	 * @return direction
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * @param direction 要设置的 direction
	 */
	public void setDirection(int direction) {
		if (direction != LEFT_DIRECTION && direction != RIGHT_DIRECTION) return;
		this.direction = direction;
	}

	/**
	 * @return velocity
	 */
	public double getVelocity() {
		return velocity;
	}

	/**
	 * @param velocity 要设置的 velocity
	 */
	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	@Override
	public void trigger() {
		this.setTriggered(true);
		
	}

	@Override
	public void untrigger() {
		this.setTriggered(false);
		
	}
	
	/* （非 Javadoc）
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		AccelerationMark cloned = (AccelerationMark) super.clone();
		cloned.direction = this.direction;
		cloned.velocity = this.velocity;
		return cloned;
	}

}
