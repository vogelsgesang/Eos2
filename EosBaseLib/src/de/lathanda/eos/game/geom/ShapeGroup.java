package de.lathanda.eos.game.geom;

import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.math.Matrix;
import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.base.math.Vector;
import java.util.ArrayList;

/**
 * \brief Formen Gruppe
 *
 * Die Klasse gruppiert mehrere Formen. Welche dadurch gleichzeitig manipuliert
 * werden können. Formen Gruppen dürfen konkav sein. Allerdings werden alle
 * Methoden proportional zur Anzahl der Teilformen langsamer, da alle Methoden
 * an alle Teilumrisse delegiert werden.
 *
 * @author Lathanda
 *
 */
public class ShapeGroup extends Shape {

    /**
     * \brief Formen
     */
    private ArrayList<Shape> list;
    /**
     * \brief Ort vor eventuellen Verschiebungen
     */
    private Point previousLocation;
    /**
     * \brief Winkel vor eventuellen Drehungen
     */
    private double previousAngle;

    /**
     * Neue Formen Gruppe
     */
    public ShapeGroup() {
        p = new Point(0, 0);
        previousLocation = new Point(0, 0);
        angle = 0;
        previousAngle = 0;
        list = new ArrayList<>();
    }

    /**
     * Fügt eine neue Form hinzu. Wird die Gruppe verändert wird die
     * hinzugefügte Form benfalls verändert. Eine Änderung an der Form ändert
     * die die Lage der Form innerhalb der Gruppe.
     *
     * @param o Form
     */
    public void add(Shape o) {
        list.add(o);
    }

    /**
     * Entfernt die Form aus der Gruppe.
     *
     * @param o Form
     */
    public void remove(Shape o) {
        list.remove(o);
    }

    /**
     * Liefert eine Liste der Formen der Gruppe.
     *
     * @return Liste der Formen
     */
    public ArrayList<Shape> getOutlines() {
        return list;
    }

    @Override
    public boolean contains(double x, double y) {
        for (Shape o : list) {
            if (o.contains(x, y)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public double getDistance(double x, double y) {
        double diff = Double.POSITIVE_INFINITY;
        for (Shape o : list) {
            diff = Math.min(o.getDistance(x, y), diff);
        }
        return diff;
    }

    @Override
    public Types getOutlineType() {
        return Types.GROUP;
    }

    /**
     * Aktuallisiert Daten
     */
    @Override
    protected void positionChanged() {
        Vector diff = new Vector(previousLocation, p);
        previousLocation.move(diff);
        for (Shape o : list) {
            o.move(diff);
        }
    }

    /**
     * Aktuallisiert Daten
     */
    @Override
    protected void angleChanged() {
        rotate(angle - previousAngle);
        previousAngle = angle;
    }

    /**
     * Dreht die Gruppe.
     *
     * @param diff Drehwinkel im Bogenmaß
     */
    @Override
    public void rotate(double diff) {
        this.angle += diff;
        Matrix m = new Matrix(diff);
        for (Shape o : list) {
            Vector d = new Vector(p, o.p);
            Point p2 = new Point(p);
            d = m.transform(d);
            p2.move(d);
            o.moveTo(p2);
            o.setAngle(o.getAngle() + diff);
        }
    }

    @Override
    public double getLeft() {
        left = Double.POSITIVE_INFINITY;
        double actLeft;
        for (Shape o : list) {
            actLeft = o.getLeft();
            if (left > actLeft) {
                left = actLeft;
            }
        }
        return left;
    }

    @Override
    public double getRight() {
        right = Double.NEGATIVE_INFINITY;
        double actRight;
        for (Shape o : list) {
            actRight = o.getRight();
            if (right < actRight) {
                right = actRight;
            }
        }
        return right;
    }

    @Override
    public double getBottom() {
        bottom = Double.POSITIVE_INFINITY;
        double actBottom;
        for (Shape o : list) {
            actBottom = o.getBottom();
            if (bottom > actBottom) {
                bottom = actBottom;
            }
        }
        return bottom;
    }

    @Override
    public double getTop() {
        top = Double.NEGATIVE_INFINITY;
        double actTop;
        for (Shape o : list) {
            actTop = o.getTop();
            if (top < actTop) {
                top = actTop;
            }
        }
        return top;
    }

	@Override
	public void draw(Picture picture) {
        for (Shape o : list) {
            picture.drawShape(o);
        }	
	}
}
