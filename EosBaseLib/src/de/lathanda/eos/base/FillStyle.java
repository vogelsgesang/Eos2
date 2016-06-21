package de.lathanda.eos.base;

/**
 * F&uuml;llarten.
 *
 * @author Peter (Lathanda) Schneider
 */
public enum FillStyle {
    FILLED,
    RULED,
    CHECKED,
    TRANSPARENT;
	@Override
	public String toString() {
		switch (this) {
		case FILLED:
			return "\u25A0";
		case RULED:
			return "\u25A4";
		case CHECKED:
			return "\u25A6";
		case TRANSPARENT:
			return "\u25A1";
		}
		return "";
	}
	
}
