package de.lathanda.eos.base;

import java.awt.Color;

/**
 * Linien-/Randeigenschaften.
 *
 * @author Peter (Lathanda) Schneider
 */
public class LineDescriptor {
	public static final LineDescriptor NONE = new LineDescriptor(Color.BLACK, LineStyle.INVISIBLE, 0);
    private Color color;
    private LineStyle type;
    private double thickness;
    public LineDescriptor() {
        this(Color.BLACK, LineStyle.SOLID, 0.25f);
    }
    public LineDescriptor(Color color, LineStyle type, double thickness) {
        this.color = color;
        this.type = type;
        this.thickness = thickness;
    }
    public LineDescriptor(LineDescriptor org) {
        this.color = org.color;
        this.type = org.type;
        this.thickness = org.thickness;
    }
    public Color getColor() {
        return color;
    }
    public void setColor(Color c) {
        this.color = c;
    }
    public LineStyle getLineStyle() {
        return type;
    }
    public void setLineStyle(LineStyle lt) {
        type = lt;
    }
    public double getDrawWidth() {
        return thickness;
    }
    public void setDrawWidth(double width) {
        this.thickness = width;
    }
    public void scale(double factor) {
        thickness *= factor;
    }
    public LineDescriptor copy() {
        return new LineDescriptor(this);
    }
}
