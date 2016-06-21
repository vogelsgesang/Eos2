package examples;

import static eos.Ausrichtung.*;
import eos.Fenster;
import eos.TextFeld;
/**
 * @author Peter Schneider
 *
 * Spielerei mit einem Textfeld.
 */
public class Text {
    public static void main(String[] args) {
        Text text = new Text();
        text.neuezeile("Hallo");
        text.neuezeile("Das ist ein Textfeld");
    }
    private TextFeld textfeld;
    private Fenster fenster;
    public Text() {
        fenster = new Fenster();
        textfeld = new TextFeld();
        fenster.zeichne(textfeld);
        textfeld.drehen(20);
        textfeld.ausrichtungHorizontalSetzen(linksbuendig);
    }
    public void neuezeile(String text) {
        textfeld.zeileHinzufuegen(text);
    }
    
}
