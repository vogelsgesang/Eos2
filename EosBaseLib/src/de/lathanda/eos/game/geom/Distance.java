package de.lathanda.eos.game.geom;

/**
 * \brief Entfernungsberechnung
 *
 * Diese Klasse stellte statische Funktionen für die Entfernungsberechnung zwischen
 * Umrissen zur Verfügung.
 *
 * @author Lathanda
 *
 */
public class Distance {

    /**
     * Berechnet den Abstand zwischen zwei Umrissen
     *
     * @param a Erster Umriss
     * @param b Zweiter Umriss
     * @return Abstand zwischen den beiden Umrissen, Werte <= 0 bedeuten eine
     * Überschneidung.
     */
    public static double distance(Shape a, Shape b) {
    	//yes this is ugly, but java doesn't support polymorph parameters
    	//and i don't know a better solution atm.
        switch (a.getOutlineType()) {
            case DOT:
                return b.getDistance(a.p);
            case GROUP:
                return groupOutline((ShapeGroup) a, b);
            case RECTANGLE:
                switch (b.getOutlineType()) {
                    case RECTANGLE:
                        return polygonPolygon((Rectangle) b, (Rectangle) a);
                    case POLYGON:
                        return polygonPolygon((Rectangle) a, (Polygon) b);
                    case CIRCLE:
                        return a.getDistance(b.p) - ((Circle) b).radius;
                    case DOT:
                        return a.getDistance(b.p);
                    case GROUP:
                        return groupOutline((ShapeGroup) b, a);
                    case UNDEFINED:
                        throw new RuntimeException("unknown outline typ!");
                }
            case POLYGON:
                switch (b.getOutlineType()) {
                    case RECTANGLE:
                        return polygonPolygon((Rectangle) b, (Polygon) a);
                    case POLYGON:
                        return polygonPolygon((Polygon) a, (Polygon) b);
                    case CIRCLE:
                        return a.getDistance(b.p) - ((Circle) b).radius;
                    case DOT:
                        return a.getDistance(b.p);
                    case GROUP:
                        return groupOutline((ShapeGroup) b, a);
                    case UNDEFINED:
                        throw new RuntimeException("unknown outline typ!");
                }
            case CIRCLE:
                return a.getDistance(b.p) - ((Circle) b).radius;
            case UNDEFINED:
                throw new RuntimeException("unknown outline typ!");
        }
        throw new RuntimeException("unknown outline typ!");
    }

    /**
     * Berechnet den Abstand zu einer Umrissgruppe. Positive Werte sind korrekt.
     * Negative Wert sind nur Näherungen.
     *
     * @param a Umrissgruppe
     * @param b Umriss
     * @return minimaler Abstand zwischen Umrissgruppe und Umriss
     */
    private static double groupOutline(ShapeGroup a, Shape b) {
        double distance = Double.POSITIVE_INFINITY;
        for (Shape o : a.getOutlines()) {
            distance = Math.min(o.distance(b), distance);
        }
        return distance;
    }

    /**
     * Berechnet den Abstand zwischen zwei Polygonen
     *
     * @param p1 Erstes Polygon
     * @param p2 Zweites Polygon
     * @return Abstand
     */
    private static double polygonPolygon(Polygon p1, Polygon p2) {
        double distance;
        if (p1.intersects(p2)) {
            distance = Double.NEGATIVE_INFINITY;
            for (int i = p1.coordinates_final_x.length; i-- > 0;) {
                distance = Math.max(distance, p2.getDistance(
                        p1.coordinates_final_x[i],
                        p1.coordinates_final_y[i]));
            }
            for (int i = p2.coordinates_final_x.length; i-- > 0;) {
                distance = Math.max(distance, p1.getDistance(
                        p2.coordinates_final_x[i],
                        p2.coordinates_final_y[i]));
            }
            distance = Math.min(distance, 0);
        } else {
            distance = Double.POSITIVE_INFINITY;
            for (int i = p1.coordinates_final_x.length; i-- > 0;) {
                distance = Math.min(distance, p2.getDistance(
                        p1.coordinates_final_x[i],
                        p1.coordinates_final_y[i]));
            }
            for (int i = p2.coordinates_final_x.length; i-- > 0;) {
                distance = Math.min(distance, p1.getDistance(
                        p2.coordinates_final_x[i],
                        p2.coordinates_final_y[i]));
            }
        }
        return distance;
    }
}
