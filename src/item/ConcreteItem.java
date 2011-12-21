package item;

import logic.Position;

public abstract class ConcreteItem extends Item {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2324599225173619641L;
	private double density;
	private double rigidity;
	private double friction;
	
	/**
	 * Default Constructor
	 */
	public ConcreteItem() {
		this.density = 0;
		this.rigidity = 0;
		this.friction = 0;
	}
	
	/**
	 * Constructor with position
	 * @param pos
	 */
	public ConcreteItem(Position pos) {
		super(pos);
		this.density = 0;
		this.rigidity = 0;
		this.friction = 0;
	}
	
	/**
	 * Constructor with all the private attributes
	 * @param pos
	 * @param density
	 * @param rigidity
	 * @param friction
	 */
	public ConcreteItem(Position pos, double density, double rigidity, double friction) {
		super(pos);
		this.density = density;
		this.rigidity = rigidity;
		this.friction = friction;
	}

	/**
	 * @return density
	 */
	public double getDensity() {
		return density;
	}

	/**
	 * @param density 要设置的 density
	 */
	public void setDensity(double density) {
		this.density = density;
	}

	/**
	 * @return rigidity
	 */
	public double getRigidity() {
		return rigidity;
	}

	/**
	 * @param rigidity 要设置的 rigidity
	 */
	public void setRigidity(double rigidity) {
		this.rigidity = rigidity;
	}

	/**
	 * @return friction
	 */
	public double getFriction() {
		return friction;
	}

	/**
	 * @param friction 要设置的 friction
	 */
	public void setFriction(double friction) {
		this.friction = friction;
	}
	
	/* （非 Javadoc）
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		ConcreteItem cloned = (ConcreteItem) super.clone();
		cloned.friction = this.friction;
		cloned.density = this.density;
		cloned.rigidity = this.rigidity;
		return cloned;
	}
}
