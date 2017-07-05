package de.lathanda.eos.gui.diagram;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import de.lathanda.eos.util.GuiToolkit;


/**
 * Objecte dieser Klasse verpacken ein Graphics2D Objekt und
 * stellen alle Methoden zur Verfügung die zum Zeichnen von Diagrammen notwendig sind.
 * Alle Operationen sind auf mm skaliert.
 * Die Font werden auf der Basis von 72 dpi normiert.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Drawing {
	/**
	 * Hilfsobjekt zum berechnen der Fontausmaße.
	 */
    private static final BufferedImage DUMMY = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
    /**
     * Dummy Zeichenziel.
     */
    private static final Graphics2D DUMMY_G = DUMMY.createGraphics();
    /**
     * Skalierung.
     */
    private final float scale;
    /**
     * Fontskalierung. Je nach Renderingziel kann diese stark Variieren.
     */
    private final float fontScale2pixel;
    /**
     * Zeichen Schriftart.
     */
    private Font font;
    /**
     * Original Schriftart
     */
    private Font originalFont;
    /**
     * Verschiebungsstack.
     */
    private final LinkedList<Translation> transformations = new LinkedList<>();
    /**
     * Aktuelle Verschiebung
     */
    private Translation translation = new Translation();
    /**
     * Zeichenziel.
     */
    private Graphics2D g;
    
    /**
     * Erzeugt eine Zeichnung mit Bildschirmpixeldichte.
     * Bevor das Objekt sinnvoll genutzt werden kann muss init aufgerufen werden. 
     */
    public Drawing() {
        this(GuiToolkit.getScreenResolution(), DUMMY_G);
    }
    /**
     * Erzeugt eine Zeichnung.
     * Bevor das Objekt sinnvoll genutzt werden kann muss init aufgerufen werden. 
     * @param dpi Pixeldichte
     */
    public Drawing(float dpi) {
        this(dpi, DUMMY_G);
    }
    /**
     * Erzeugt eine Zeichnung
     * @param dpi Pixeldichte
     * @param g Zeichenziel
     */
    public Drawing(float dpi, Graphics2D g) {
        scale = dpi / 25.4f;
        fontScale2pixel = dpi / 72f; 
        this.g = g;
        setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON); 

        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);          
    }
    /**
     * Initialisiert die Zeichnung mit dem Zeichenziel.
     * @param g Zeichenziel.
     */
    public void init(Graphics2D g) {
        while (!transformations.isEmpty()) {
            popTransform();
        }
        this.g = g;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON); 

        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);  
    }
    /**
     * Ermittelt die Breite eines Textes.
     * @param text Text.
     * @return Breite des Textes in mm.
     */
    public float stringWidth(String text) {          
        return g.getFontMetrics(font).stringWidth(text) / scale;
    }
    /**
     * Liefert den Abstand zwischen Oberlinie und Unterlinie der Schriftart.
     * @return
     */
    public float getAscent() {
        return g.getFontMetrics(font).getAscent() / scale;
    }
    /**
     * Liefert die Zeilenhöhe der Schriftart.
     * @return
     */
    public float getHeight() {
        return g.getFontMetrics(font).getHeight() / scale;
    }
    /**
     * Zeichnet eine Linie.
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    public void drawLine(float x1, float y1, float x2, float y2) {
        g.drawLine(xp(x1), yp(y1), xp(x2), yp(y2));
    }
    /**
     * Zeichnet einen Text.
     * @param text
     * @param x
     * @param y
     */
    public void drawString(String text, float x, float y) {
        g.drawString(text, xp(x), yp(y));
    }
    /**
     * Setzt die Schriftart.
     * @param font
     */
    public void setFont(Font font) {
    	originalFont = font;
        this.font = font.deriveFont(font.getSize2D() * fontScale2pixel);
        g.setFont(this.font);
    }
    /**
     * Liefert aktuelle Schriftart
     * @return
     */
    public Font getFont() {
    	return originalFont;
    }
    /**
     * Setzt die Breite gezeichneter Linien.
     * @param width
     */
    public void setDrawWidth(float width) {
        g.setStroke(new BasicStroke(width * scale));
    }
    /**
     * Setzt die Farbe in der gezeichnet wird.
     * @param c
     */
    public void setColor(Color c) {
        g.setColor(c);
    }
    /**
     * Liefert die Farbe in der gezeichnet wird.
     */
    public Color getColor() {
        return g.getColor();
    } 
    /**
     * Zeichnet eine Ellipse.
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void drawEllipse(float x, float y, float width, float height) {
        g.drawOval(xp(x), yp(y), wp(x, width), hp(y, height));
    }    
    /**
     * Zeichnet ein Rechteck.
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void drawRect(float x, float y, float width, float height) {
        g.drawRect(xp(x), yp(y), wp(x, width), hp(y, height));
    }
    /**
     * Mal ein Rechteck aus.
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void fillRect(float x, float y, float width, float height) {
        g.fillRect(xp(x), yp(y), wp(x, width), hp(y, height));
    }    
    /**
     * Zeichnet ein Polygon.
     * @param x x-Koordinaten
     * @param y y-Koordinaten
     * @param n Anzahl der Punkte.
     */
    public void drawPolygon(float[] x, float[] y, int n) {
        int[] ix = new int[n];
        int[] iy = new int[n];
        for(int i = n; i --> 0; ) {
            ix[i] = xp(x[i]);
            iy[i] = yp(y[i]);
        }
        g.drawPolygon(ix, iy, n);
    }
    private int xp(float x) {
    	return (int)((x + translation.x)*scale);
    }
    private int yp(float y) {
    	return (int)((y + translation.y)*scale);
    }
    private int wp(float x, float w) {
    	return xp(x+w)-xp(x);
    }
    private int hp(float y, float h) {
    	return yp(y+h)-yp(y);
    }
    /**
     * Zeichnet einen Pfeil.
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param size Größe der Spitze
     */
    public void drawArrow(float x1, float y1, float x2, float y2, float size) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        float length = (float)Math.sqrt(dx*dx + dy*dy);
        dx = dx * size / length;
        dy = dy * size / length;
        float xb = x2 - dx;
        float yb = y2 - dy;
        drawLine(x1, y1, x2, y2);
        drawLine(x2, y2, xb - dy*0.5f, yb + dx*0.5f);
        drawLine(x2, y2, xb + dy*0.5f, yb - dx*0.5f);
    }    
    /**
     * Sichert die aktuelle Transformation.
     */
    public void pushTransform() {
        transformations.push(translation.copy());
    }
    /**
     * Stellt die zuletzt gesicherte Transformation wieder her.
     */
    public void popTransform() {
        translation = transformations.pop();
    }
    /**
     * Verschiebt das Koordiantensystem.
     * Es sollte vorher pushTransform() und später popTransform() verwendet werden.
     * @param dx
     * @param dy
     */
    public void translate(float dx, float dy) {
        translation.add(dx, dy);
    }
    /**
     * Rechnet mm in Pixel um.
     * @param mm
     * @return
     */
    public int convertmm2pixel(float mm) {
        return (int)(mm * scale);
    }
    /**
     * Rechnet Pixel in mm um.
     * @param pixel
     * @return
     */
    public float convertpixel2mm(int pixel) {
        return pixel / scale;
    }    
    private static class Translation {
    	float x;
    	float y;
    	Translation copy() {
    		Translation t = new Translation();
    		t.x = this.x;
    		t.y = this.y;
    		return t;
    	}
    	void add(float dx, float dy) {
    		x += dx;
    		y += dy;
    	}
    }
}
