package de.lathanda.eos.base;

import de.lathanda.eos.base.layout.Dimension;
import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.base.layout.Transform;
import de.lathanda.eos.game.geom.Shape;
import de.lathanda.eos.util.GuiToolkit;

import java.awt.Color;
import java.awt.Font;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Abstrakte Zeichenschnittstelle.
 * Unabhängig vom verwendeten Grafiksystem.
 *
 * @author Peter (Lathanda) Schneider
 */
public abstract class Picture {
    /**
     * multiplying this constant converts internal mm coordinates into pixels.
     */
    protected final double scaleBase;  
    protected double scale;
    protected double centerX;          //[mm]
    protected double centerY;          //[mm]
    protected double halfwidth;        //[mm]
    protected double halfheight;       //[mm]
  
    //Pen
    protected LineDescriptor line;
    protected FillDescriptor fill;   
    
    //Text
    protected Alignment vertical   = Alignment.TOP;
    protected Alignment horizontal = Alignment.LEFT;
    protected double hspace = 1;
    protected double vspace = 1;
    protected Font font;
    
    public Picture() {       
        scaleBase = GuiToolkit.getScreenResolution() / 25.4f;
        scale = 1.0f;
        centerX = 0;
        centerY = 0;
        line = new LineDescriptor();
        fill = new FillDescriptor();
    } 
    /**
     * Minimale sichtbare x-Koordinate. 
     * @return x in [mm]
     */
    public double getMinX() {
        return centerX - halfwidth;
    }
    /**
     * Minimale sichatbare y-Koordinate.
     * @return y in [mm]
     */
    public double getMinY() {
        return centerY - halfheight;
    }
    /**
     * Maximale sichtbare x-Koordinate.
     * @return x in [mm]
     */
    public double getMaxX() {
        return centerX + halfwidth;
    }
    /**
     * Maximale sichtbare y-Koordiante.
     * @return y in [mm]
     */
    public double getMaxY() {
        return centerY + halfheight;
    }    
    /**
     * Transformation anwenden.
     * @param tf neue Transformation
     */
    public abstract void applyTransform(Transform tf);
    /**
     * Linie zeichnen.
     * @param x1 Anfang x
     * @param y1 Anfang y
     * @param x2 Ende x
     * @param y2 Ende y
     */
    public abstract void drawLine(double x1, double y1, double x2, double y2);
    /**
     * Linie zeichnen
     * @param a Anfangspunkt
     * @param b Endpunkt
     */
    public final void drawLine(Point a, Point b) {
        drawLine(a.getX(), a.getY(), b.getX(), b.getY());
    }
    /**
     * Linienzug zeichnen.
     * @param points Sequenz von Punkten
     */
    public final void drawLine(List<Point> points) {
        Iterator<Point> i = points.iterator();
        Point prev = i.next();
        Point act;
        while (i.hasNext()) {
            act = i.next();
            drawLine(act, prev);
            prev = act;
        }
    }
    /**
     * Polygon zeichnen mit Even-Odd-Füllung.
     * @param x x-Koordinaten
     * @param y y-Koordinaten
     */
    public abstract void drawPolygon(double[] x, double[] y);
    /**
     * Polygon zeichnen mit Even-Odd-Füllung.
     * @param points Liste der Eckpunkte.
     */
    public final void drawPolygon(List<Point> points) {
        double[] xPoints = new double[points.size()]; 
        double[] yPoints = new double[points.size()]; 
        int n = 0;
        for (Point p : points) {
            xPoints[n] = p.getX();
            yPoints[n] = p.getY();
            n++;
        }
        drawPolygon(xPoints, yPoints);
    }
    /**
     * Polygon zeichnen mit Even-Odd-Füllung.
     * @param points Liste der Eckpunkte.
     */ 
    public final void drawPolygon(Point[] points) {
        double[] xPoints = new double[points.length]; 
        double[] yPoints = new double[points.length]; 
        int n = 0;
        for (Point p : points) {
            xPoints[n] = p.getX();
            yPoints[n] = p.getY();
            n++;
        }
        drawPolygon(xPoints, yPoints);
    }
    /**
     * Rechteck zeichnen.
     * @param x x Ecke
     * @param y y Ecke
     * @param width Breite
     * @param height Höhe
     */
    public abstract void drawRect(double x, double y, double width, double height);
    /**
     * Bildzeichnen.
     * @param image Bilddaten
     * @param x x Ecke
     * @param y y Ecke
     * @param width Breite
     * @param height Höhe
     */
    public abstract void drawImage(Image image, double x, double y, double width, double height);
    /**
     * @param image Bilddaten
     * @param x x Ecke
     * @param y y Ecke
     * @param width Breite
     * @param height Höhe
     * @param scale Skalierungsmodus
     */
    public abstract void drawImage(Image image, double x, double y, double width, double height, Scaling scale);
    /**
     * @param image Bilddaten
     * @param x x Ecke
     * @param y y Ecke
     * @param width Breite
     * @param height Höhe
     * @param mirror Spiegelung
     * @param angle Drehwinkel
     */
    public abstract void drawImage(Image image, double x, double y, double width, double height, boolean mirror, double angle);
    /**
     * Rechteck zeichnen mit Mittelpunkt (0/0).
     * Wird zusammen mit Transformationen verwendet. 
     * @param width Breite
     * @param height Höhe
     */
    public final void drawRect(double width, double height) {
        drawRect(-width/2,-height/2,width,height);
    }
    /**
     * Ellipse zeichnen.
     * @param p Mittelpunkt
     * @param radiusX Waagerechter Radius
     * @param radiusY Senkrechter Radius
     */
    public void drawEllipse(Point p, double radiusX, double radiusY) {
    	drawEllipse(p.getX(), p.getY(), radiusX, radiusY);
    }
    /**
     * Ellipse zeichnen
     * @param x x Mittelpunkt
     * @param y y Mittelpunkt
     * @param radiusX Waagerechter Radius
     * @param radiusY Senkrechter Radius
     */
    public abstract void drawEllipse(double x, double y, double radiusX, double radiusY);
    /**
     * Ellipse zeichnen mit Mittelpunkt (0/0).
     * Wird zusammen mit Transformationen verwendet. 
     * @param radiusX
     * @param radiusY
     */
    public final void drawEllipse(double radiusX, double radiusY) {
        drawEllipse(0, 0, radiusX, radiusY);
    }
    /**
     * Textausrichtung festlegen
     * @param vertical Vertikale Ausrichtung
     * @param horizontal Horizontale Ausrichtung
     */
    public final void setTextAlignment(Alignment vertical, Alignment horizontal) {
        this.vertical = vertical;
        this.horizontal = horizontal;
    }
    /**
     * Texteinrückung setzen.
     * @param hspace Waagerechter Randabstand
     * @param vspace Senkrechter Randabstand
     */
    public final void setTextSpacing(double hspace, double vspace) {
        this.hspace = hspace;
        this.vspace = vspace;
    }
    /**
     * Schriftart festlegen. 
     * @param font Schriftart.
     */
    public final void setFont(Font font) {
        //Fontsize 20 is about 5mm height
        this.font = font;        
    }
    /**
     * Schriftart festlegen.
     * @param fontname Name der Schriftart.
     * @param size Schriftgröße.
     */
    public final void setFont(String fontname, int size) {
        //Fontsize 20 is about 5mm height
        font = new Font(fontname, Font.PLAIN, size);
    }
    /**
     * Text zeichnen.
     * @param text Text.
     * @param x x-Koordinate
     * @param y y-Koordinate
     */
	protected abstract void drawStringAt(String text, double x, double y);
	/**
	 * Text zeichnen.
	 * @param text Text
	 * @param shape Rechteck auf das der Text geschrieben werden soll.
	 */
	public void drawString(String text, Shape shape) {
		drawText(text, shape.getLeft(), shape.getBottom(), shape.getWidth(), shape.getHeight());
	}
	/**
	 * Mehrzeiligen Text zeichnen.
     * Vorher sollte die Textausrichtung festgelegt werden!
	 * @param text Liste der Textzeilen
	 * @param left Linke Grenze des Rechtecks das beschrieben werden soll. 
	 * @param bottom Untere Grenze des Rechtecks das beschrieben werden soll.
	 * @param width Breite des Rechtecks das beschrieben werden soll.
	 * @param height Höhe des Rechtecks das beschrieben werden soll.
	 */
    public void drawText(List<String> text, double left, double bottom, double width, double height) {
    	drawText(text.toArray(new String[text.size()]), left, bottom, width, height);
    }
	/**
	 * Mehrzeiligen Text zeichnen.
     * Vorher sollte die Textausrichtung festgelegt werden!
	 * @param text Ferld der Textzeilen
	 * @param left Linke Grenze des Rechtecks das beschrieben werden soll. 
	 * @param bottom Untere Grenze des Rechtecks das beschrieben werden soll.
	 * @param width Breite des Rechtecks das beschrieben werden soll.
	 * @param height Höhe des Rechtecks das beschrieben werden soll.
	 * @return 
	 */    
    public void drawText(String[] text, double left, double bottom, double width, double height) {
        double lineheight = getStringHeight();
        double textheight = (lineheight + vspace) * text.length - vspace;
        double descent = getStringDescent();
        double y = 0;
        double dx = left + width / 2;
        double dy = bottom + height / 2;
        switch (vertical) {
        case CENTER:
            y = textheight/2 - lineheight + descent;
            break;
        case LEFT:
        case TOP:
            y = height/2 - vspace - lineheight + descent;
            break;
        case RIGHT:
        case BOTTOM:
            y = -height/2 + textheight - lineheight + vspace + descent;
            break;
        }
        for(String textline: text) {
            double textwidth = getStringWidth(textline);
            double x = 0;
            switch (horizontal) {
            case CENTER:
                x = -textwidth/2;
                break;
            case LEFT:
            case TOP:
                x = -width/2 + hspace;
                break;
            case RIGHT:
            case BOTTOM:
                x = width/2 - textwidth - hspace;
                break;                
            }
            drawStringAt(textline, dx + x, dy +y);
            y -= lineheight + vspace;
        }
    }
    /**
     * Text in Rechteck zeichnen.
     * @param text Text
     * @param x x Ecke
     * @param y y Ecke
     * @param width Breite
     * @param height Höhe
     */
    public final void drawText(String text, double x, double y, double width, double height) {
        drawText(split(text), x, y, width, height);
    }
    /**
     * Text zeichnen.
     * Vorher sollte die Textausrichtung festgelegt werden!
     * @param text Text.
     * @param x Orientierungspunkt x-Koordinate
     * @param y Orientierungspunkt y-Koordinate
     */
    public final void drawText(String text, double x, double y) {
        List<String> list = split(text);
        drawText(list, x, y, 0, 0);
    }
    /**
     * Ermittelt die ungefähre Breite des Textes im aktuellen Kontext.
     * @param text Text
     * @return Breite des Textes
     */
    protected abstract double getStringWidth(String text);
    /**
     * Ermittelt die ungefähre Höhe einer Zeile im aktuellen Kontext.
     * @return Höhe einer Zeile
     */
    protected abstract double getStringHeight();
    /**
     * Ermittelt die ungefähre Unterlänge im aktuellen Kontext.
     * @return Unterlänge einer Zeile
     */
    protected abstract double getStringDescent();
    /**
     * Ermittelt die ungefähren Ausmaße mehrerer Textzeilen.
     * @param text Liste der Textzeilen
     * @return Ausmaße
     */
    public Dimension getTextDimension(List<String> text) {
        Dimension dim = new Dimension();
        dim.setHeight((getStringHeight() + vspace) * text.size() + vspace);
        for(String textline: text) {
            double textwidth = getStringWidth(textline)+2*hspace;
            if (textwidth > dim.getWidth()) {
                dim.setWidth(textwidth);
            }
        }
        return dim;
    }
    /**
     * Ermittelt die ungefähren Ausmaße eines Textes
     * @param text Text
     * @return Ausmaße
     */
    public Dimension getTextDimension(String text) {
        return getTextDimension(split(text));
    }
    /**
     * Zerlegt einen Text in mehrere Zeilen anhand der Steuerzeichen.
     * @param text Text
     * @return Liste der Textzeilen
     */
    private List<String> split(String text) {
        StringTokenizer st = new StringTokenizer(text, "\n\r\f", false);
        LinkedList<String> list = new LinkedList<>();
        while(st.hasMoreTokens()) {
            list.add(st.nextToken());
        }
        return list;
    }
    /**
     * Legt fest wie ausgemalt wird.
     * @param fill Füllart
     */
    public void setFill(FillDescriptor fill) {
        this.fill = fill;
    }
    /**
     * Legt fest wie Linien gezeichnet werden.
     * @param line Linienart
     */
    public void setLine(LineDescriptor line) {
        this.line = line;
    }
    /**
     * Setzt die Linienfarbe.
     * @param c Linienfarbe
     */
    public void setLineColor(Color c) {
    	line.setColor(c);
    	line.setLineStyle(LineStyle.SOLID);
    }
    /**
     * Setzt die Linienbreite.
     * @param w Linienbreite
     */
    public void setLineWidth(double w) {
    	line.setDrawWidth(w);
    }
    /**
     * Setzt die Füllfarbe.
     * @param c Füllfarbe
     */
    public void setFillColor(Color c) {
    	fill.setColor(c);
    	fill.setFillStyle(FillStyle.FILLED);
    }
    /**
     * Zeichnet eine Form.
     * @param shape Form
     */
    public void drawShape(Shape shape) {
    	shape.draw(this);
    }
    /**
     * Verschiebt das Koordinatensystem.
     * @param dx x Verschiebung
     * @param dy y Verschiebung
     */
    public abstract void translate(double dx, double dy);
    /**
     * Rotiert das Koordinatensystem.
     * @param angle Rotationswinkel
     */
    public abstract void rotate(double angle);
    /**
     * Sichert die Aktuelle Transformation um sie später wiederherzustellen.
     */
    public abstract void pushTransform();
    /**
     * Stellt die vorher gesicherte Transformation wieder her.
     */
    public abstract void restoreTransform();
}
