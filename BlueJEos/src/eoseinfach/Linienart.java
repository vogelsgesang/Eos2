package eoseinfach;

import de.lathanda.eos.base.LineStyle;

/**
 * @version 0.1
 * @author Peter Schneider
 * 
 * Diese Klasse entspricht dem EOS-Konstantenpool LINIENART.
 * 
 * Um die Füllarten aus diesem Pool zu verwenden schreiben sie zum Beispiel
 * 
 * {@code Linienart.durchgezogen}
 * 
 * Wenn sei {@code import static eos.LinienartFuellart.*} am Anfang der Klasse ergänzen genügt
 * es {@code durchgezogen} zu schreiben.
 * 
 */
public abstract class Linienart {
    public static LineStyle gestrichelt      = LineStyle.DASHED;
    public static LineStyle durchgezogen     = LineStyle.SOLID;
    public static LineStyle gepunktelt       = LineStyle.DOTTED;
    public static LineStyle gestrichpunktelt = LineStyle.DASHED_DOTTED;
    public static LineStyle unsichtbar       = LineStyle.INVISIBLE;
    private Linienart(){}
}
