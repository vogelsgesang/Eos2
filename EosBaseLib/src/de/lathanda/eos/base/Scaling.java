package de.lathanda.eos.base;
/**
 * Skalierungsmodus f&uuml;r Bilder.
 *
 * @author Peter (Lathanda) Schneider
 */
public enum Scaling {
	STRETCH,
	CUT,
	FIT;

	@Override
	public String toString() {
		switch(this) {
		case STRETCH:
			return "<->";
		case CUT:
			return "|-|";
		case FIT:
			return "<=>";
		}
		return "";
	}	
}
