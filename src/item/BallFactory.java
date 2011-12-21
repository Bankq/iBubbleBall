package item;

import logic.Position;

public class BallFactory extends AbstractItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5969878151127052568L;
	private int targetState;
	
	/**
	 * Constructor with target state
	 * @param targetState
	 */
	public BallFactory(int targetState) {
		this.targetState = targetState;
	}
	
	/**
	 * Constructor with position and target state
	 * @param pos
	 * @param targetState
	 */
	public BallFactory(Position pos, int targetState) {
		super(pos);
		this.targetState = targetState;
	}
	
	public BallFactory(Position pos, int targetState, int radius) {
		super(pos, radius);
		this.targetState = targetState;
	}

	/**
	 * @return targetState
	 */
	public int getTargetState() {
		return targetState;
	}

	@Override
	public void trigger() {
		this.setTriggered(true);
		
	}

	@Override
	public void untrigger() {
		this.setTriggered(false);

	}
	
	/* £¨·Ç Javadoc£©
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		BallFactory cloned = (BallFactory) super.clone();
		cloned.targetState = this.targetState;
		return cloned;
	}
}
