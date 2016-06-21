package de.lathanda.eos.robot;
/**
 * Richtung des Roboters
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public enum Direction {
	SOUTH( 0,-1, 0),
	WEST(-1, 0, 1),
	NORTH( 0, 1, 2),
	EAST( 1, 0, 3);
	static {
		WEST.left = SOUTH;
		WEST.right = NORTH;
		EAST.left = NORTH;
		EAST.right = SOUTH;
		NORTH.left = WEST;
		NORTH.right = EAST;
		SOUTH.left = EAST;
		SOUTH.right = WEST;
	}
	public final int dx;
	public final int dy;
	public final int index;
	private Direction left;
	private Direction right;
	private Direction(int dx, int dy, int index) {
		this.dx = dx;
		this.dy = dy;
		this.index = index;
	}
	public final Direction getLeft() {
		return left;
	}
	public final Direction getRight() {
		return right;
	}
	public Direction getBack() {
		return left.left;
	}
	public static Direction getDirection(int index) {
		switch(index) {
		case 0: return SOUTH;
		case 1: return WEST;
		case 2: return NORTH;
		case 3: return EAST;
		default:
			return EAST;
		}
	}
	public float getAngle() {
		switch(index) {
		case 0: return 180f;
		case 1: return 90f;
		case 2: return 0f;
		case 3: return 270f;
		default:
			return 270;
		}		
	}
	@Override
	public String toString() {
		switch (this) {
		case EAST:
			return "E";
		case SOUTH:
			return "S";
		case NORTH:
			return "N";
		case WEST:
			return "W";			
		}
		return "";
	}
	
}
