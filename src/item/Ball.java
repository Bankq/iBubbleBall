package item;

import logic.Position;

public class Ball extends ConcreteItem implements Gravity, CircleArea{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7656456298368676937L;
	public static final int NORMAL_BALL = 0;
	public static final int METAL_BALL = 1;
	public static final int BALLOON = 2;
	private int state;
	private int radius;
	
	/**
	 * Default Constructor
	 */
	public Ball() {
		state = NORMAL_BALL;
		radius = 0;
	}
	
	/**
	 * Constructor with position
	 * @param pos
	 */
	public Ball(Position pos) {
		super(pos);
		state = NORMAL_BALL;
		radius = 0;
	}
	
	/**
	 * Constructor with position and radius
	 * @param pos
	 * @param radius
	 */
	public Ball(Position pos, int radius) {
		super(pos);
		state = NORMAL_BALL;
		this.radius = radius;
	}

	/**
	 * @return state
	 */
	public int getState() {
		return state;
	}

	/**
	 * @param state 要设置的 state
	 */
	public void setState(int state) {
		if (state != NORMAL_BALL && state != METAL_BALL && state != BALLOON) return;
		this.state = state;
	}

	/**
	 * @return radius
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * @param radius 要设置的 radius
	 */
	public void setRadius(int radius) {
		this.radius = radius;
	}
	
	/* （非 Javadoc）
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		Ball cloned = (Ball) super.clone();
		cloned.radius = this.radius;
		cloned.state = this.state;
		return cloned;
	}
	
}
