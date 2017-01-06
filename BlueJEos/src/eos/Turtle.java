package eos;

import java.awt.Color;

import de.lathanda.eos.base.FillStyle;
import de.lathanda.eos.base.LineStyle;
import de.lathanda.eos.geo.Plotter;

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
    	super(new Plotter());
        plotter = (Plotter)figure;
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
    public void fuellartSetzen(FillStyle fuellart) {
    	plotter.setFillStyle(fuellart);
    }
    public void fuellfarbeSetzen(Color farbe) {
    	plotter.setFillColor(farbe);
    }
    public void zentrumSetzen(double x, double y) {
    	plotter.setCenter(x, y);
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
    public void links(double winkel) {
        plotter.turnLeft(winkel);
    }
    public void rechts(double winkel) {
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
        plotter.setLineColor(farbe);
    }
    public Color farbeLesen() {
    	return plotter.getLineColor();
    }
    public void allesLoeschen() {
    	plotter.clearAll();
    }
}
