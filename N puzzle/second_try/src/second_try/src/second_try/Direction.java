package second_try;

public enum Direction {
	LEFT("left"),
	RIGHT("right"),
	UP("up"),
	DOWN("down");
	
	private final String direction;
	
	private Direction(String direction) {
		this.direction = direction;
	}
	
	public String toString() {
		return this.direction;
	}
}
