package item;

import logic.Position;

public abstract class WoodenItem extends ConcreteItem implements Gravity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4276315404983913000L;

	/**
	 * Default Constructor
	 */
	public WoodenItem() {
		super();
	}
	
	/**
	 * Constructor with position
	 * @param pos
	 */
	public WoodenItem(Position pos) {
		super(pos);
	}
	
	/* £¨·Ç Javadoc£©
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		WoodenItem cloned = (WoodenItem) super.clone();
		return cloned;
	}
}
