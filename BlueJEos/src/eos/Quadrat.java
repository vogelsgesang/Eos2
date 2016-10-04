package eos;

import de.lathanda.eos.geo.Square;

/**
 * @author Peter Schneider
 * 
 * Diese Klasse entspricht der EOS Klasse QUADRAT.
 *  
 * Der direkte Zugriff auf die Attribute ist nicht möglich.
 * Es müssen die entsprechenden Getter- und Settermethoden verwendet werden.
 * 
 * Alle Einheiten sind in Millimeter(double) statt in Pixel(int).
 */
public class Quadrat extends GefuellteFigur {
	private final Square square;

	public Quadrat() {
		super(new Square());
		square = (Square)figure;
	}

	public void seitenlaengeSetzen(double seitenlaenge) {
		square.setSide(seitenlaenge);
	}

	public double seitenlaengeLesen() {
		return square.getSide();
	}

	public void linksObenSetzen(double links, double oben) {
		square.setLeftTop(links, oben);
	}
}
