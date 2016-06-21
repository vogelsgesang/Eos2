package de.lathanda.eos.base;

/**
 * Ausrichtung von Text.
 *
 * @author Peter (Lathanda) Schneider
 */
public enum Alignment {
    CENTER, 
    LEFT,
    RIGHT, 
    TOP,
    BOTTOM;

	@Override
	public String toString() {
		switch(this) {
		case CENTER:
			return "*";
		case LEFT:
			return "<-";
		case RIGHT:
			return "->";
		case TOP:
			return "^";
		case BOTTOM:
			return "_";
		};
		return "";
	}
	
}
