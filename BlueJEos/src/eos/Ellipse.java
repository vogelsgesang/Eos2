package eos;

import de.lathanda.eos.geo.FilledFigure;

/**
 * @author Peter Schneider
 * 
 * Diese Klasse entspricht der EOS Klasse ELLIPSE.
 * Diese leitet alle Methodenafurufe an die Klasse {@link de.lathanda.eos.geo.Ellipse} weiter.
 * 
 * Der direkte Zugriff auf die Attribute ist nicht möglich.
 * Es müssen die entsprechenden Getter- und Settermethoden verwendet werden.
 * 
 * Alle Einheiten sind in Millimeter(double) statt in Pixel(int).
 */
public class Ellipse extends GefuellteFigur {
    private final de.lathanda.eos.geo.Ellipse ellipse;
    public Ellipse() {
        ellipse = new de.lathanda.eos.geo.Ellipse();
    }
    @Override
    protected FilledFigure getFigure() {
        return ellipse;
    }
    public void mitteySetzen(double y) {
        ellipse.setCenterY(y);
    }
    public double mitteyLesen() {
        return ellipse.getCenterY();
    }
    public void mittexSetzen(double x) {
        ellipse.setCenterX(x);
    }
    public double mittexLesen() {
        return ellipse.getCenterX();
    }
    public void radiusySetzen(double radiusy) {
        ellipse.setRadiusY(radiusy);
    }
    public double radiusyLesen() {
        return ellipse.getRadiusY();
    }
    public void radiusxSetzen(double radiusx) {
        ellipse.setRadiusX(radiusx);
    }
    public double radiusxLesen() {
        return ellipse.getRadiusX();
    }
    public void mittelpunktSetzen(double x, double y) {
        ellipse.setCenter(x, y);
    }
}
