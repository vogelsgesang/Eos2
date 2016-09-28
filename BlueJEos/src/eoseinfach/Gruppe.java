package eoseinfach;

import de.lathanda.eos.base.FillStyle;
import de.lathanda.eos.base.LineStyle;
import de.lathanda.eos.geo.Figure;
import de.lathanda.eos.geo.Group;
import java.awt.Color;

/**
 * @author Peter Schneider
 * 
 * Diese Klasse entspricht der EOS Klasse GRUPPE.
 * 
 * Der direkte Zugriff auf die Attribute ist nicht möglich.
 * Es müssen die entsprechenden Getter- und Settermethoden verwendet werden.
 * 
 * Alle Einheiten sind in Millimeter(double) statt in Pixel(int).
 * 
 */
public class Gruppe extends Figur {
	private final Group group;

	public Gruppe() {
		group = new Group();
	}

	@Override
	protected Figure getFigure() {
		return group;
	}

	public void randfarbeSetzen(Color farbe) {
		group.setLineColor(farbe);
	}

	public void randartSetzen(LineStyle randart) {
		group.setLineStyle(randart);
	}

	public void randstaerkeSetzen(double randstaerke) {
		group.setLineWidth(randstaerke);
	}

	public void fuellartSetzen(FillStyle fuellart) {
		group.setFillStyle(fuellart);
	}

	public void fuellfarbeSetzen(Color farbe) {
		group.setFillColor(farbe);
	}

	public void schlucke(Figur figur) {
		group.addFigure(figur.getFigure());
	}
	
	public void kopiere(Figur figur) {
		group.copyFigure(figur.getFigure());
	}

	public void spiegleX() {
		group.mirrorX();
	}

	public void spiegleY() {
		group.mirrorY();
	}

	public void setzeZentrum(double x, double y) {
		group.setCenter(x, y);
	}
}
