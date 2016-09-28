package eoseinfach;

import de.lathanda.eos.base.Alignment;

/**
 * @author Peter Schneider
 * 
 * Diese Klase entspricht dem EOS-Konstantenpool AUSRICHTUNG.
 * 
 * Sie wird für die Klasse {@link eos.TextFeld} benötigt.
 * 
 * Um die Ausrichtungen aus diesem Pool zu verwenden schreiben sie zum Beispiel
 * 
 * {@code Ausrichtung.zentriert}
 * 
 * Wenn sie {@code import static eos.Ausrichtung.*;} am Anfang der Klasse ergänzen genügt
 * es {@code zentriert} zu schreiben.
 * 
 */
public abstract class Ausrichtung {
	public static final Alignment zentriert = Alignment.CENTER;
	public static final Alignment linksbuendig = Alignment.LEFT;
	public static final Alignment rechtsbuendig = Alignment.RIGHT;
	public static final Alignment obenbuendig = Alignment.TOP;
	public static final Alignment untenbuendig = Alignment.BOTTOM;

	private Ausrichtung() {
	};
}
