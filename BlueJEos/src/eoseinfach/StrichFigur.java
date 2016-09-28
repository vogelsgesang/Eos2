package eoseinfach;

import de.lathanda.eos.base.LineStyle;
import de.lathanda.eos.geo.LineFigure;
import java.awt.Color;

/**
 * @author Peter Schneider
 * 
 * Diese Klasse entspricht keiner EOS Klasse. Sie ist die Oberklasse
 * aller Figuren die einen Rand haben.
 * 
 * Der direkte Zugriff auf die Attribute ist nicht möglich.
 * Es müssen die entsprechenden Getter- und Settermethoden verwendet werden.
 * 
 * Alle Einheiten sind in Millimeter(double) statt in Pixel(int).
 * 
 */
public abstract class StrichFigur extends Figur {

    public StrichFigur() {
    }
    @Override
    protected abstract LineFigure getFigure();
    
    public void randfarbeSetzen(Color farbe) {
        getFigure().setLineColor(farbe);
    }
    public Color randfarbeLesen() {
        return getFigure().getLineColor();
    }
    public void randartSetzen(LineStyle randart) {
        getFigure().setLineStyle(randart);
    }
    public LineStyle randartLesen() {
        return getFigure().getLineStyle();
    }
    public void randstaerkeSetzen(double randstaerke) {
        getFigure().setLineWidth(randstaerke);
    }
    public double randstaerkeLesen() {
        return getFigure().getLineWidth();
    }
}
