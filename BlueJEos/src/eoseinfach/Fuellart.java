package eoseinfach;

import de.lathanda.eos.base.FillStyle;

/**
 * @author Peter Schneider
 * 
 * Diese Klase entspricht dem EOS-Konstantenpool FÜLLART.
 * 
 * Um die Füllarten aus diesem Pool zu verwenden schreiben sie zum Beispiel
 * 
 * {@code Füllart.ausgemalt}
 * 
 * Wenn sei {@code import static eos.Fuellart.*} am Anfang der Klasse ergänzen genügt
 * es {@code ausgemalt} zu schreiben.
 * 
 */
public abstract class Fuellart {
    public static FillStyle ausgemalt    = FillStyle.FILLED;
    public static FillStyle schraffiert  = FillStyle.RULED;
    public static FillStyle kariert      = FillStyle.CHECKED;
    public static FillStyle durchsichtig = FillStyle.TRANSPARENT;
    private Fuellart() {};
}
