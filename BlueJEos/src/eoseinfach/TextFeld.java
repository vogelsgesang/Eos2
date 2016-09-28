package eoseinfach;

import de.lathanda.eos.base.Alignment;
import de.lathanda.eos.geo.FilledFigure;
import de.lathanda.eos.geo.TextField;
import java.awt.Color;

/**
 * @author Peter Schneider
 * 
 * Diese Klasse entspricht der EOS Klasse TEXTFELD.
 * 
 * Der direkte Zugriff auf die Attribute ist nicht möglich.
 * Es müssen die entsprechenden Getter- und Settermethoden verwendet werden.
 * 
 * Alle Einheiten sind in Millimeter(double) statt in Pixel(int).
 */
public class TextFeld extends Rechteck {
    private final TextField textfield;
    public TextFeld() {
        textfield = new TextField();
    }
    @Override
    protected FilledFigure getFigure() {
        return textfield;
    }
    public void schriftfarbeSetzen(Color farbe) {
        textfield.setTextColor(farbe);
    }
    public Color schriftfarbeLesen() {
        return textfield.getTextColor();
    }
    public void schriftgroesseSetzen(int groesse) {
        textfield.setTextSize(groesse);
    }
    public int schriftgroesseLesen() {
        return textfield.getTextSize();
    }
    public void schriftartSetzen(String schriftart) {
        textfield.setFont(schriftart);
    }
    public String schriftartLesen() {
        return textfield.getFont();
    }
    public void ausrichtungVertikalSetzen(Alignment ausrichtung) {
        textfield.setAlignmentVertical(ausrichtung);
    }
    public Alignment ausrichtungVertikalLesen() {
        return textfield.getAlignmentVertical();
    }
    public void ausrichtungHorizontalSetzen(Alignment ausrichtung) {
        textfield.setAlignmentHorizontal(ausrichtung);
    }
    public void groesseAutomatischAnpassenSetzen(boolean auto) {
        textfield.setAutoAdjust(auto);
    }
    public boolean groesseAutomatischAnpassenLesen() {
        return textfield.getAutoAdjust();
    }
    public Alignment ausrichtungHorizontalLesen() {
        return textfield.getAlignmentHorizontal();
    }

    public void zeileHinzufuegen(String text) {
        textfield.appendLine(text);
    }
    public void zeileLoeschen() {
        textfield.deleteLine();
    }
}
