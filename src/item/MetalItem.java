package item;

import logic.Position;

public class MetalItem extends ConcreteItem {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4010362393405736396L;

	/**
	 * Default Constructor
	 */
	public MetalItem() {
		super();
	}
	
	/**
	 * Constructor with position
	 * @param pos
	 */
	public MetalItem(Position pos) {
		super(pos);
	}
	
	/* £¨·Ç Javadoc£©
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		MetalItem cloned = (MetalItem) super.clone();
		return cloned;
	}
}
