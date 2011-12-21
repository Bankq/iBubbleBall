package item;

import logic.Position;

public abstract class AbstractItem extends Item implements CircleArea{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3098851932286158819L;
	private boolean triggered;
	private int radius;
	
	/**
	 * Default Constructor
	 */
	public AbstractItem() {
		triggered = false;
		radius = 0;
	}
	
	/**
	 * Constructor with position
	 * @param pos
	 */
	public AbstractItem(Position pos) {
		super(pos);
		triggered = false;
		radius = 0;
	}
	
	public AbstractItem(Position pos, int radius) {
		super(pos);
		triggered = false;
		this.radius = radius;
	}
	
	public boolean isTriggered() {
		return triggered;
	}
	
	/**
	 * @param triggered 要设置的 triggered
	 */
	public void setTriggered(boolean triggered) {
		this.triggered = triggered;
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
		AbstractItem cloned = (AbstractItem) super.clone();
		cloned.radius = this.radius;
		cloned.triggered = this.triggered;
		return cloned;
	}

	public abstract void trigger();
	public abstract void untrigger();
}
