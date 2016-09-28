package eoseinfach;

import de.lathanda.eos.geo.Figure;
import de.lathanda.eos.geo.Window;

/**
 * @author Peter Schneider
 *
 * Diese Klasse ist allen Figuren gemein. Sie hat keine Entsprechung in EOS
 *
 * Sie realisisert grundlegende geometrische Transformationen, welche
 * grundsätzlich für alle Figuren möglich sind.
 */
public abstract class Figur {
	private static final Window window = new Window();
    public Figur() {
    	window.addFigure(this.getFigure());
    }

    protected abstract Figure getFigure();

    public void verschieben(double dx, double dy) {
        getFigure().move(dx, dy);
    }

    public void strecken(double streckungsfaktor) {
        getFigure().scale(streckungsfaktor);
    }

    public void verschiebenNach(double x, double y) {
        getFigure().moveTo(x, y);
    }

    public void drehen(double winkel) {
        getFigure().rotate(winkel);
    }

    public void drehenAn(double x, double y, double winkel) {
        getFigure().rotateAround(x, y, winkel);
    }

    public void streckenAn(double x, double y, double faktor) {
        getFigure().scaleAt(x, y, faktor);
    }

    public void sichtbarSetzen(boolean sichtbar) {
        getFigure().setVisible(sichtbar);
    }

    public boolean sichtbarLesen() {
        return getFigure().getVisible();
    }
    public void xSetzen(double x) {
        getFigure().setX(x);
    }
    public double xLesen() {
        return getFigure().getX();
    }
    public void ySetzen(double y) {
        getFigure().setY(y);
    }
    public double yLesen() {
        return getFigure().getY();
    }
}
