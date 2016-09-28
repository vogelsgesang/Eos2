package eoseinfach;

import de.lathanda.eos.base.FillStyle;
import de.lathanda.eos.geo.FilledFigure;
import java.awt.Color;

/**
 * @author Peter Schneider
 * 
 * Diese Klasse entspricht der EOS Klasse GEFÜLLTEFIGUR.
 * 
 * Der direkte Zugriff auf die Attribute ist nicht möglich.
 * Es müssen die entsprechenden Getter- und Settermethoden verwendet werden.
 * 
 * Alle Einheiten sind in Millimeter(double) statt in Pixel(int).
 * 
 */
public abstract class GefuellteFigur extends StrichFigur {
	@Override
	protected abstract FilledFigure getFigure();

	public void fuellartSetzen(FillStyle fuellart) {
		getFigure().setFillStyle(fuellart);
	}

	public FillStyle fuellartLesen() {
		return getFigure().getFillStyle();
	}

	public void fuellfarbeSetzen(Color farbe) {
		getFigure().setFillColor(farbe);
	}

	public Color fuellfarbeLesen() {
		return getFigure().getFillColor();
	}
}
