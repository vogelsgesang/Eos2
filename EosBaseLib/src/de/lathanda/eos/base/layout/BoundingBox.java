package de.lathanda.eos.base.layout;

import de.lathanda.eos.base.math.Point;

/**
 * Ein einfaches Rechteck, welches garantiert, dass eine Geometrische Figur 
 * vollständig innerhalb liegt.
 * Ob es sich um das minimale Rechteck handelt ist hierbei nicht garantiert.
 * Die Bounding Box dient zum Berechnen von Rotationszentren und
 * für die Prüfung, ob ein Punkt innerhalb liegt.
 *
 * @author Peter (Lathanda) Schneider
 */
public class BoundingBox {
    private double left;
    private double right;
    private double top;
    private double bottom;
    private boolean valid;
    public BoundingBox() {
        left = Double.POSITIVE_INFINITY;
        right = Double.NEGATIVE_INFINITY;
        bottom = Double.POSITIVE_INFINITY;
        top = Double.NEGATIVE_INFINITY;
        valid = false;
    }
    public BoundingBox(double left, double right, double bottom, double top) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        valid = true;
    }
    public void add(Point p) {
        add(p.getX(), p.getY());
    }
    public void add(double x, double y) {
        if (left > x) {
            left = x;
        }
        if (right < x) {
            right = x;
        }
        if (bottom > y) {
            bottom = y;
        }
        if (top < y) {
            top = y;
        }
        valid = true;
    }
    public void add(BoundingBox b) {
        if (left > b.left) {
            left = b.left;
        }
        if (right < b.right) {
            right = b.right;
        }
        if (bottom > b.bottom) {
            bottom = b.bottom;
        }
        if (top < b.top) {
            top = b.top;
        }
        valid = true;
    }
    public double getArea() {
        if (valid) {
            return (right - left) *(top-bottom);
        } else {
            return 0;
        }
    }
    public double getArea(double drawWidth) {
        if (valid) {
            return (right - left + drawWidth) *(top - bottom + drawWidth);
        } else {
            return 0;
        }
    }
    public double getLeft() {
        return left;
    }

    public double getRight() {
        return right;
    }

    public double getTop() {
        return top;
    }

    public double getBottom() {
        return bottom;
    }
    public boolean isValid() {
        return valid;
    }
    public Point getCenter() {
    	return new Point((left + right) / 2, (top + bottom) / 2);
    }
	@Override
	public String toString() {
		return "BoundingBox("+left+","+top+","+right+","+bottom+")";
	}
    
}
