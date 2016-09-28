package eoseinfach;

import de.lathanda.eos.geo.FilledFigure;
import de.lathanda.eos.geo.Triangle;
/**
 * @author Peter Schneider
 * 
 * Diese Klasse entspricht der EOS Klasse DREIECK.
 * Diese leitet alle Methodenafurufe an die Klasse {@link de.lathanda.eos.geo.Triangle} weiter.
 * 
 * Der direkte Zugriff auf die Attribute ist nicht möglich.
 * Es müssen die entsprechenden Getter- und Settermethoden verwendet werden.
 * 
 * Alle Einheiten sind in Millimeter(double) statt in Pixel(int).
 */
public class Dreieck extends GefuellteFigur {
    private final Triangle triangle;
    public Dreieck() {
        triangle = new Triangle();
    }
    @Override
    protected FilledFigure getFigure() {
        return triangle;
    }
    public void eckenSetzen(double x1, double y1, double x2, double y2, double x3, double y3) {
        triangle.setCorners(x1,y1,x2,y2,x3,y3);
    }
    public void spiegeleX() {
        triangle.mirrorX();
    }
    public void spiegeleY() {
        triangle.mirrorY();
    }
}
