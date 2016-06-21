package de.lathanda.eos.base.layout;

import de.lathanda.eos.base.math.Point;

/**
 * Ein Punkt mit Gewichtung. Dieser stellt den Schwerpunkt
 * bzw. Mittelpunkt einer geometrischen Figur dar. 
 * Er dient zum berechnen des Schwerpunkts zwischen mehreren Figuren
 * und als Rotationszentrum.
 *
 * @author Peter (Lathanda) Schneider
 */
public class BalancePoint {
    private final double weight;
    private final Point point;

    public BalancePoint(double weight, Point point) {
        this.weight = weight;
        this.point = point;
    }
    public BalancePoint(double weight, double x, double y) {
        this.weight = weight;
        this.point = new Point(x, y);
    }

    public double getWeight() {
        return weight;
    }

    public Point getPoint() {
        return point;
    }
    public double getX() {
        return point.getX();
    }
    public double getY() {
        return point.getY();
    }

    @Override
    public String toString() {
        return "BalancePoint{" + "weight=" + weight + ", point=" + point + '}';
    }
    
}
