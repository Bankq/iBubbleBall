package item;

import logic.Position;

public class Coin extends AbstractItem implements Unmovable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6518431377144896747L;
	private boolean disappear;
	
	/**
	 * Default Constructor
	 */
	public Coin() {
		super();
		disappear = false;
	}
	
	/**
	 * Constructor with position
	 * @param pos
	 */
	public Coin(Position pos) {
		super(pos);
		disappear = false;
	}
	
	/**
	 * Constructor with position and radius
	 * @param pos
	 * @param radius
	 */
	public Coin(Position pos, int radius) {
		super(pos, radius);
		disappear = false;
	}
	
	@Override
	public void trigger() {
		this.setDisappear(true);
		this.setTriggered(true);
	}

	@Override
	public void untrigger() {
		this.setTriggered(false);

	}

	public boolean isDisappear() {
		return disappear;
	}

	public void setDisappear(boolean disappear) {
		this.disappear = disappear;
	}

	/* £¨·Ç Javadoc£©
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		Coin cloned = (Coin) super.clone();
		cloned.disappear = this.disappear;
		return cloned;
	}
}
