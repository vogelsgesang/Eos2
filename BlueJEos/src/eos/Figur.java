package eos;

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
	private static boolean autowindow = true;
	private static Window window;
	protected static void setAutoWindow(boolean value) {
		autowindow = value;
	}
	protected final Figure figure;
	
    protected Figur(Figure figure) {
    	this.figure = figure;
    	if (autowindow) {
    		if (window == null) {
    			Figur.window = new Window();
    		}
    		window.addFigure(figure);
    	}
    }

    public void verschieben(double dx, double dy) {
    	figure.move(dx, dy);
    }

    public void strecken(double streckungsfaktor) {
    	figure.scale(streckungsfaktor);
    }

    public void verschiebenNach(double x, double y) {
    	figure.moveTo(x, y);
    }

    public void drehen(double winkel) {
    	figure.rotate(winkel);
    }

    public void drehenAn(double x, double y, double winkel) {
    	figure.rotateAround(x, y, winkel);
    }

    public void streckenAn(double x, double y, double faktor) {
    	figure.scaleAt(x, y, faktor);
    }

    public void sichtbarSetzen(boolean sichtbar) {
    	figure.setVisible(sichtbar);
    }

    public boolean sichtbarLesen() {
        return figure.getVisible();
    }
    public void xSetzen(double x) {
    	figure.setX(x);
    }
    public double xLesen() {
        return figure.getX();
    }
    public void ySetzen(double y) {
    	figure.setY(y);
    }
    public double yLesen() {
        return figure.getY();
    }
}
