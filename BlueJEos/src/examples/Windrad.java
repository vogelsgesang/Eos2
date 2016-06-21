package examples;

import static eos.Farbe.*;
import static eos.Fuellart.*;
import static eos.Linienart.*;
import eos.Ellipse;
import eos.Fenster;
import eos.Gruppe;
import eos.Rechteck;
import eos.SchrittUhr;

/**
 * @author Peter Schneider
 *
 * Ein Windrad.
 */
public class Windrad {

    public static void main(String[] args) {
        Windrad w = new Windrad();
        for (int i = 0; i < 1000; i++) {
            w.schritt();
        }
    }
    SchrittUhr uhr;
    Gruppe rad;
    Rechteck turm;
    Fenster f;

    public Windrad() {
        f = new Fenster();
        uhr = new SchrittUhr(50);
        turm = new Rechteck();
        f.zeichne(turm);
        turm.eckenSetzen(-10, 20, 10, -80);
        turm.fuellfarbeSetzen(blau);
        rad = new Gruppe();
        rad.setzeZentrum(0, 0);
        f.zeichne(rad);
        rad.verschieben(0, 20);
        for (int i = 0; i < 30; i++) {
            Ellipse blatt = new Ellipse();
            blatt.radiusxSetzen(30);
            blatt.radiusySetzen(10);
            blatt.verschieben(40, 20);
            blatt.fuellfarbeSetzen(gruen);
            blatt.fuellartSetzen(kariert);
            blatt.randfarbeSetzen(gelb);
            blatt.randartSetzen(gestrichpunktelt);
            blatt.randstaerkeSetzen(2);
            rad.schlucke(blatt);
            rad.drehen(12);
        }
    }

    public void schritt() {
        rad.drehen(1);
        uhr.weiter();
    }
}
