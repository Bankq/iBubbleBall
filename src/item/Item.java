package item;

import java.io.Serializable;

import logic.Position;

public class Item implements Serializable, Cloneable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9107345731783354095L;
	private Position position;		// Position of the item
	
	/**
	 * Default Constructor
	 */
	public Item() {
		this.position = new Position(0,0);
	}
	
	/**
	 * Constructor
	 * @param pos
	 */
	public Item(Position pos) {
		this.position = pos;
	}

	/**
	 * @return position
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * @param position position to be set
	 */
	public void setPosition(Position position) {
		this.position = position;
	}

	/* £¨·Ç Javadoc£©
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		Item cloned = (Item) super.clone();
		cloned.position = (Position) this.position.clone();
		return cloned;
	}
	
	
}
