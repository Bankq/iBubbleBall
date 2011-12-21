package item;

import logic.Position;

public class Magnet extends DynamicItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1549149563122988542L;
	public static final int FORCE = 100;		// This is not decided yet
	
	/**
	 * Default constructor
	 */
	public Magnet() {
		
	}
	
	/**
	 * Constructor with position
	 * @param pos
	 */
	public Magnet(Position pos) {
		super(pos);
	}
	
	/**
	 * Constructor with position and radius
	 * @param pos
	 * @param radius
	 */
	public Magnet(Position pos, int radius) {
		super(pos, radius);
	}
	
	/* £¨·Ç Javadoc£©
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		Magnet cloned = (Magnet) super.clone();
		return cloned;
	}
}
