package eos;

import java.awt.Color;

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
    	super(new de.lathanda.eos.geo.Ellipse());
        ellipse = (de.lathanda.eos.geo.Ellipse)figure;
    }
	public Ellipse(double x, double y, double breite, double hoehe) {
    	super(new de.lathanda.eos.geo.Ellipse());
        ellipse = (de.lathanda.eos.geo.Ellipse)figure;
        ellipse.setRadiusX(breite / 2);
        ellipse.setRadiusY(hoehe / 2);
        ellipse.setCenter(x + breite / 2, y + hoehe /2);
	}
	public Ellipse(double x, double y, double d) {
    	super(new de.lathanda.eos.geo.Ellipse());
        ellipse = (de.lathanda.eos.geo.Ellipse)figure;
        ellipse.setRadiusX(d / 2);
        ellipse.setRadiusY(d / 2);
        ellipse.setCenter(x, y);
	}
	public Ellipse(double x, double y, double breite, double hoehe, Color farbe) {
    	super(new de.lathanda.eos.geo.Ellipse());
        ellipse = (de.lathanda.eos.geo.Ellipse)figure;
        ellipse.setRadiusX(breite / 2);
        ellipse.setRadiusY(hoehe / 2);
        ellipse.setCenter(x + breite / 2, y + hoehe /2);
        ellipse.setLineColor(farbe);
        ellipse.setFillColor(farbe);
	}
	public Ellipse(double x, double y, double a, Color farbe) {
    	super(new de.lathanda.eos.geo.Ellipse());
        ellipse = (de.lathanda.eos.geo.Ellipse)figure;
        ellipse.setRadiusX(a / 2);
        ellipse.setRadiusY(a / 2);
        ellipse.setCenter(x + a / 2, y + a /2);
        ellipse.setLineColor(farbe);
        ellipse.setFillColor(farbe);
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
