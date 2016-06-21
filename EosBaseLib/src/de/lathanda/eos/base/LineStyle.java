package de.lathanda.eos.base;

/**
 * Linienarten.
 *
 * @author Peter (Lathanda) Schneider
 */
public enum LineStyle {
    DASHED,
    SOLID,
    DOTTED,
    DASHED_DOTTED,
    INVISIBLE;

	@Override
	public String toString() {
		switch (this) {
		case DASHED:
			return "- - -";
		case SOLID:
			return "_____";
		case DOTTED:
			return ".....";
		case DASHED_DOTTED:
			return "- . -";
		case INVISIBLE:
			return "";
		}
		return "";
	}
	
}
