package eos;

import de.lathanda.eos.geo.Window;
import java.awt.Color;
/**
 * @author Peter Schneider
 * 
 * Diese Klasse entspricht der EOS Klasse FENSTER.
 *  
 * Der direkte Zugriff auf die Attribute ist nicht möglich.
 * Es müssen die entsprechenden Getter- und Settermethoden verwendet werden.
 * 
 * Alle Einheiten sind in Millimeter(double) statt in Pixel(int).
 */
public class Fenster {
    private final Window window;
    public Fenster() {
        window = new Window();
        Figur.setAutoWindow(false);
    }
    public void gitterfarbeSetzen(Color color) {
         window.setGridColor(color); 
    }
    public Color gitterfarbeLesen() {
        return window.getGridColor();
    }
    public void hintergrundfarbeSetzen(Color color) {
        window.setBackgroundColor(color);
    }
    public Color hintergrundfarbeLesen() {
        return window.getBackgroundColor();
    }
    public void hoeheSetzen(double hoehe) {
        window.setHeight(hoehe);
    }
    public double hoeheLesen() {
        return window.getHeight();
    }
    public void breiteSetzen(double breite) {
        window.setWidth(breite);
    }
    public double breiteLesen() {
        return window.getWidth();
    }
    public void obenSetzen(double oben) {
        window.setTop(oben);
    }
    public double obenLesen() {
        return window.getTop();
    }
    public void linksSetzen(double links) {
        window.setLeft(links);
    }
    public double linksLesen() {
        return window.getLeft();
    }
    public void nameSetzen(String name) {
        window.setTitle(name);
    }
    public String nameLesen() {
        return window.getTitle();
    }
    public void strichabstandSetzen(double abstand) {
        window.setGridWidth(abstand);
    }
    public double strichabstandLesen() {
        return window.getGridWidth();
    }
    public void verschiebe(double dx, double dy) {
        window.move(dx, dy);
    }
    public void zeichne(Figur figur) {
        window.addFigure(figur.figure);
    }
    public void gitterein() {
        window.setGridVisible(true);
    }
    public void gitteraus() {
        window.setGridVisible(false);
    }
    public void zoomSetzen(double zoom) {
    	window.setZoom(zoom);
    }
    public void mitteSetzen(double x, double y) {
    	window.setCenter(x, y);
    }
    public double mausXLesen() {
    	return window.getCursorX();
    }
    public double mausYLesen() {
    	return window.getCursorY();
    }
    public boolean mausGedruecktLesen() {
    	return window.isCursorDown();
    }
    public boolean mausKlickLesen() {
    	return window.isCursorClick();
    }
    
}
