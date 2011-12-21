package item;

import logic.Position;

public class Flag extends AbstractItem{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8915491892602046168L;
	
	/**
	 * Default Constructor
	 */
	public Flag() {
		super();
	}
	
	/**
	 * Constructor with position
	 * @param pos
	 */
	public Flag(Position pos) {
		super(pos);
	}
	
	/**
	 * Constructor with position and radius
	 * @param pos
	 * @param radius
	 */
	public Flag(Position pos, int radius) {
		super(pos, radius);
	}

	@Override
	public void trigger() {
		// do nothing

	}

	@Override
	public void untrigger() {
		// do nothing

	}

	/* £¨·Ç Javadoc£©
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		Flag cloned = (Flag) super.clone();
		return cloned;
	}
}
