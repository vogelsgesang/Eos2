package eoseinfach;

import de.lathanda.eos.base.LineStyle;
import de.lathanda.eos.geo.Figure;
import de.lathanda.eos.geo.Plotter;
import java.awt.Color;

/**
 * @author Peter Schneider
 * 
 * Diese Klasse entspricht der EOS Klasse TURTLE.
 * 
 * Der direkte Zugriff auf die Attribute ist nicht möglich.
 * Es müssen die entsprechenden Getter- und Settermethoden verwendet werden.
 * 
 * Alle Einheiten sind in Millimeter(double) statt in Pixel(int).
 */
public class Turtle extends Figur {
    private final Plotter plotter;
    public Turtle() {
        plotter = new Plotter();
    }
    @Override
    protected Figure getFigure() {
        return plotter;
    }
    public void turtlexSetzen(double x) {
        plotter.setPenX(x);
    }
    public double turtlexLesen() {
        return plotter.getPenX();
    }
    public void turtleySetzen(double y) {
        plotter.setPenY(y);
    }
    public double turtleyLesen() {
        return plotter.getPenY();
    }
    public void linienStaerkeSetzen(double staerke) {
        plotter.setLineWidth(staerke);
    }

    public void linienartSetzen(LineStyle linienart) {
        plotter.setLineStyle(linienart);
    }
    public void kursSetzen(double winkel) {
        plotter.setAngle(winkel);
    }
    public double kursLesen() {
        return plotter.getAngle();
    }
    public void turtleSichtbarSetzen(boolean b) {
        plotter.setPenVisible(b);
    }
    public boolean turtleSichtbarLesen() {
        return plotter.getPenVisible();
    }
    public void linksdrehen(double winkel) {
        plotter.turnLeft(winkel);
    }
    public void rechtsdrehen(double winkel) {
        plotter.turnRight(winkel);
    }
    public void vor(double laenge) {
        plotter.moveForward(laenge);
    }
    public void zurueck(double laenge) {
        plotter.moveBackward(laenge);
    }
    public void setzeTurtleXY(double x, double y) {
        plotter.movePenTo(x, y);
    }
    public void stiftAb() {
        plotter.startPlotting();
    }
    public void stiftAuf() {
        plotter.stopPlotting();
    }
    public void farbeSetzen(Color farbe) {
        plotter.setPenColor(farbe);
    }
}
