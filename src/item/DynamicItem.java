package item;

import logic.Position;

public abstract class DynamicItem extends Item implements CircleArea{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6531709677878090544L;
	private int radius;
	private boolean functioning;
	
	/**
	 * Default constructor
	 */
	public DynamicItem() {
		radius = 0;
		functioning = false;
	}
	
	/**
	 * Constructor with position
	 * @param pos
	 */
	public DynamicItem(Position pos) {
		super(pos);
		radius = 0;
		functioning = false;
	}
	
	/**
	 * Constructor with position and radius
	 * @param pos
	 * @param radius
	 */
	public DynamicItem(Position pos, int radius) {
		super(pos);
		this.radius = radius;
		functioning = false;
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

	/**
	 * @return functioning
	 */
	public boolean isFunctioning() {
		return functioning;
	}

	/**
	 * @param functioning 要设置的 functioning
	 */
	public void setFunctioning(boolean functioning) {
		this.functioning = functioning;
	}
	
	/* （非 Javadoc）
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		DynamicItem cloned = (DynamicItem) super.clone();
		cloned.functioning = this.functioning;
		cloned.radius = this.radius;
		return cloned;
	}
}
