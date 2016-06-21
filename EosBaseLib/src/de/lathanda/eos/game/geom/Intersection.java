package de.lathanda.eos.game.geom;

import de.lathanda.eos.base.math.Vector;
import de.lathanda.eos.base.math.Range;

/**
 * \brief Überlappungsberechnung
 *
 * Diese Klasse stellt Funktionen für die Ermittlung von Überlappungen von
 * Umrissen zur Verfügung.
 *
 *
 * @author Lathanda
 *
 */
public class Intersection {

    /**
     * Berechnet ob zwei Umrisse überlappen
     *
     * @param a Erster Umriss
     * @param b Zweiter Umriss
     * @return Wahr, wenn beide Umrisse gemeinsame Punkte besitzen
     */
    public static boolean intersects(Shape a, Shape b) {
    	//yes this is ugly, but java doesn't support polymorph parameters
    	//and i don't know a better solution atm.
    	switch (a.getOutlineType()) {
            case GROUP:
                return groupOutline((ShapeGroup) a, b);
            case DOT:
                return b.contains(((Dot) a).p);
            case RECTANGLE:
                switch (b.getOutlineType()) {
                    case RECTANGLE:
                        return true;
                    case POLYGON:
                        return polygonPolygon((Rectangle) a, (Polygon) b);
                    case CIRCLE:
                        return polygonCircle((Rectangle) a, (Circle) b);
                    case DOT:
                        return a.contains(((Dot) b).p);
                    case GROUP:
                        return groupOutline((ShapeGroup) b, a);
                    case UNDEFINED:
                        break;
                    default:
                        break;
                }
            case POLYGON:
                switch (b.getOutlineType()) {
                    case RECTANGLE:
                        return polygonPolygon((Rectangle) b, (Polygon) a);
                    case POLYGON:
                        return polygonPolygon((Polygon) a, (Polygon) b);
                    case CIRCLE:
                        return polygonCircle((Polygon) a, (Circle) b);
                    case DOT:
                        return a.contains(((Dot) b).p);
                    case GROUP:
                        return groupOutline((ShapeGroup) b, a);
                    case UNDEFINED:
                        throw new RuntimeException("unknown outline typ!");
                }
            case CIRCLE:
                switch (b.getOutlineType()) {
                    case RECTANGLE:
                        return polygonCircle((Rectangle) b, (Circle) a);
                    case POLYGON:
                        return polygonCircle((Polygon) b, (Circle) a);
                    case CIRCLE:
                        return circleCircle((Circle) a, (Circle) b);
                    case DOT:
                        return a.contains(((Dot) b).p);
                    case GROUP:
                        return groupOutline((ShapeGroup) b, a);
                    case UNDEFINED:
                        throw new RuntimeException("unknown outline typ!");
                }
            case UNDEFINED:
                throw new RuntimeException("unknown outline typ!");
        }
        throw new RuntimeException("unknown outline typ!");
    }

    /**
     * Berechnet ob eine Umrissgruppe mit einem Umriss überlappt.
     *
     * @param a Umrissgruppe
     * @param b Umriss
     * @return Überlappen?
     */
    private static boolean groupOutline(ShapeGroup a, Shape b) {
        for (Shape o : a.getOutlines()) {
            if (o.intersects(b)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Berechnet ob zwei Polygone überlappen
     *
     * @param p1 Erstes Polygon
     * @param p2 Zweites Polygon
     * @return Wahr, wenn die Polygone gemeinsame Punkte besitzen
     */
    private static boolean polygonPolygon(Polygon p1, Polygon p2) {
        // we use separated axis theorem

        Vector v;

        Range projectionA, projectionB;
        int a, b; // edge from index a to index b
        Polygon p;
        // for all edges

        for (int i = 0; i < p1.n + p2.n; i++) {
            if (i < p1.n) {
                a = i;
                b = (a + 1) % p1.n;
                p = p1;
            } else {
                a = i - p1.n;
                b = (a + 1) % p2.n;
                p = p2;
            }
            // get edge, rotated by 90°
            v = new Vector(
                    p.coordinates_final_y[b] - p.coordinates_final_y[a],
                    p.coordinates_final_x[a] - p.coordinates_final_x[b]
            );

            projectionA = projectPolygon(v, p1);
            projectionB = projectPolygon(v, p2);

            // and check if they overlap
            if (!projectionA.overlap(projectionB)) {
                // they do not intersect, finished
                return false;
            }
        }
        return true;
    }

    /**
     * Die Methode projiziert das Polygon auf den Vektor. Ein Schatten wird
     * erzeugt.
     *
     * @param axis Projektionsachse
     * @param polygon Polygon
     * @return Schatten auf der Achse
     */
    private static Range projectPolygon(Vector axis, Polygon polygon) {
        Range result = new Range(0, 0);
        double scalar = axis.dotProduct(polygon.coordinates_final_x[0],
                polygon.coordinates_final_y[0]);
        result.min = scalar;
        result.max = scalar;
        for (int i = 1; i < polygon.n; i++) {
            result.extend(axis.dotProduct(
                    polygon.coordinates_final_x[i],
                    polygon.coordinates_final_y[i])
            );
        }
        return result;
    }

    /**
     * Berechnet ob ein Polygon und ein Kreis überlappen
     *
     * @param p Polygon
     * @param c Kreis
     * @return Wahr, wenn das Polygon und der Kreis gemeinsame Punkte haben
     */
    private static boolean polygonCircle(Polygon p, Circle c) {
        return p.getDistance(c.p) < c.radius;
    }

    /**
     * Berechnet ob zwei Kreise überlappen
     *
     * @param c1 Erster Kreis
     * @param c2 Zweiter Kreis
     * @return Wahr, wenn die beiden Kreis gemeinsame Punkte haben
     */
    private static boolean circleCircle(Circle c1, Circle c2) {
        double dx = c2.p.getX() - c1.p.getX();
        double dy = c2.p.getY() - c1.p.getY();
        return (c1.radius + c2.radius) * (c1.radius + c2.radius) > dx * dx + dy * dy;
    }
}
