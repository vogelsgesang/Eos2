package eos;

import java.awt.Color;

import de.lathanda.eos.geo.Circle;

/**
 * @author Peter Schneider
 * 
 * Diese Klasse entspricht der EOS Klasse KREIS.
 * 
 * Der direkte Zugriff auf die Attribute ist nicht möglich.
 * Es müssen die entsprechenden Getter- und Settermethoden verwendet werden.
 * 
 * Alle Einheiten sind in Millimeter(double) statt in Pixel(int).
 */
public class Kreis extends GefuellteFigur {
	private final Circle circle;

	public Kreis() {
		super(new Circle());
		circle = (Circle)figure;
	}
	public Kreis(double x, double y, double radius) {
		super(new Circle());
		circle = (Circle)figure;
		circle.setRadius(radius);
	}
	public Kreis(double x, double y, double radius, Color farbe) {
		super(new Circle());
		circle = (Circle)figure;
		circle.setRadius(radius);
		circle.setLineColor(farbe);
		circle.setFillColor(farbe);
	}
	public void mitteySetzen(double y) {
		circle.setCenterY(y);
	}

	public double mitteyLesen() {
		return circle.getCenterY();
	}

	public void mittexSetzen(double x) {
		circle.setCenterX(x);
	}

	public double mittexLesen() {
		return circle.getCenterX();
	}

	public void radiusSetzen(double radius) {
		circle.setRadius(radius);
	}

	public double radiusLesen() {
		return circle.getRadius();
	}

	public void mittelpunktSetzen(double x, double y) {
		circle.setCenter(x, y);
	}
}
