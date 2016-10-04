package eos;

import de.lathanda.eos.geo.Rectangle;

/**
 * @author Peter Schneider
 * 
 * Diese Klasse entspricht der EOS Klasse RECHTECK.
 * 
 * Der direkte Zugriff auf die Attribute ist nicht möglich.
 * Es müssen die entsprechenden Getter- und Settermethoden verwendet werden.
 * 
 * Alle Einheiten sind in Millimeter(double) statt in Pixel(int).
 */
public class Rechteck extends GefuellteFigur {
	private final Rectangle rectangle;
	protected Rechteck(Rectangle r) {
		super(r);
		rectangle = r;
	}
	public Rechteck() {
		super(new Rectangle());
		rectangle = (Rectangle)figure;
	}
	public Rechteck(double x, double y, double breite, double hoehe) {
		super(new Rectangle());
		rectangle = (Rectangle)figure;
		rectangle.setCorners(x, y, x + breite, y + hoehe);
	}
	public Rechteck(double x, double y, double a) {
		super(new Rectangle());
		rectangle = (Rectangle)figure;
		rectangle.setCorners(x, y, x + a, y + a);
	}

	public void hoeheSetzen(double hoehe) {
		rectangle.setHeight(hoehe);
	}

	public double hoeheLesen() {
		return rectangle.getHeight();
	}

	public void breiteSetzen(double breite) {
		rectangle.setWidth(breite);
	}

	public double breiteLesen() {
		return rectangle.getWidth();
	}

	public void linksObenSetzen(double links, double oben) {
		rectangle.setLeftTop(links, oben);
	}

	public void rechtsUntenSetzen(double rechts, double unten) {
		rectangle.setRightBottom(rechts, unten);
	}

	public void eckenSetzen(double links, double oben, double rechts, double unten) {
		rectangle.setCorners(links, oben, rechts, unten);
	}

	public void linksSetzen(double links) {
		rectangle.setLeft(links);
	}

	public double linksLesen() {
		return rectangle.getLeft();
	}

	public void rechtSetzen(double rechts) {
		rectangle.setRight(rechts);
	}

	public double rechtsLesen() {
		return rectangle.getRight();
	}

	public void obenSetzen(double oben) {
		rectangle.setTop(oben);
	}

	public double obenLesen() {
		return rectangle.getTop();
	}

	public void untenSetzen(double unten) {
		rectangle.setBottom(unten);
	}

	public double untenLesen() {
		return rectangle.getBottom();
	}
}
