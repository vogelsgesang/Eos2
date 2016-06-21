package eos;

import java.awt.Color;
/**
 * @author Peter Schneider
 * 
 * Diese Klasse entspricht dem EOS-Konstantenpool FARBE.
 * 
 * Im Gegensatz zu den Eos Farben sind Javafarben unveränderlich.
 * Wenn Sie andere Farben benötigen verwenden sie die Javaklasse.
 * {@code new Color(rot, gruen, blau)}
 * 
 * Um eine Farbe aus diesem Pool zu verwenden schreiben sie zum Beispiel
 * 
 * {@code Farbe.gruen}
 * 
 * Wenn sie {@code import static eos.Farbe.*;} am Anfang der Klasse ergänzen genügt
 * es {@code gruen} zu schreiben.
 * 
 */
public abstract class Farbe {
    public static final Color gelb      = Color.YELLOW;
    public static final Color rot       = Color.RED;
    public static final Color gruen     = Color.GREEN;
    public static final Color blau      = Color.BLUE;
    public static final Color weiss     = Color.WHITE;
    public static final Color schwarz   = Color.BLACK;
    public static final Color braun     = new Color(118,80,8);
    public static final Color hellblau  = new Color(123,177,244);
    public static final Color hellgruen = new Color(87,225,4);
    public static final Color grau      = Color.GRAY;
    public static final Color hellgrau  = Color.LIGHT_GRAY;
    public static final Color pink      = new Color(245,0,250);
    public static final Color orange    = new Color(255,128,0);
    public static final Color weinrot   = new Color(128,0,0);
    public static final Color tuerkis   = new Color(0,255,255);
    private Farbe() {}
}
