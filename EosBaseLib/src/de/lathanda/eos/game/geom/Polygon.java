package de.lathanda.eos.game.geom;

import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.math.Matrix;
import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.base.math.Vector;

/**
 * \brief Polygon
 *
 * Umriss eines konvexen Polygons (Ohne Einbuchtungen). Um Einbuchtungen zu
 * realisieren muss man eine Umrissgruppe bauen ({@link ShapeGroup}).
 *
 * Beispiel 
 * \code 
 *   double[] x = new double[] { -6, 5, -6 }; 
 *   double[] y = new double[] { 4, 0, -4 }; 
 *   dreieck = new Polygon(x, y); 
 * \endcode 
 * übersetzt ist dies die Punktfolge (-6, 4) -> (5, 0) -> (-6, -4) -> Anfang
 *
 * Tritt beim Erzeugen eines Polygons ein Fehler auf kann dies im Grunde an
 * folgenden Ursachen liegen.
 * <ul>
 * <li> Das Polygon ist nicht konvex
 * <li> Die beiden Felder sind nicht gleich groß
 * <li> Die Punkte sind nicht im Uhrzeigersinn angegeben
 * </ul>
 * Das Innere liegt auf der rechten Seite der Punktkette. Diese Annahme ist sehr
 * wichtig für die Erkennung von Kollisionen.
 *
 * @author Lathanda
 *
 */
public class Polygon extends Shape {

    /**
     * \brief Original (relative) Koordinaten (x1,x2,x3...)
     */
    /* package */
    double[] coordinates_x;
    /**
     * \brief Original (relative) Koordinaten (y1,y2,y3...)
     */
    /* package */
    double[] coordinates_y;
    /**
     * \brief Transformierte (absolute) Koordinaten x
     */
    /* package */
    double[] coordinates_final_x;
    /**
     * \brief Transformierte (absolute) Koordinaten y
     */
    /* package */
    double[] coordinates_final_y;
    /**
     * \brief Anzahl der Punkte
     */
    /* package */
    int n;

    /**
     * Leeres Polygon, Daten müssen nachträglich gesetzt werden
     */
    protected Polygon() {
        p = new Point(0, 0);
    }

    /**
     * Erzeugt ein konvexes Polygon mit den Ecken im Uhrzeigersinn. Wenn die
     * Ecken nicht konvex oder nicht im Uhrzeigersinn sind, wird eine
     * Laufzeitausnahme ausgelöst.
     *
     * @param x x Koordinaten
     * @param y y Koordinaten
     */
    public Polygon(double[] x, double[] y) {
        p = new Point(0, 0);
        setVertices(x, y);
    }

    /**
     * Erzeugt eine Kopie
     *
     * @param poly Original
     */
    public Polygon(Polygon poly) {
        p = new Point(poly.p);
        angle = poly.angle;
        setVertices(poly.coordinates_x, poly.coordinates_y);
    }

    /**
     * Setzt neue Ecken im Uhrzeigersinn. Wenn die Ecken nicht konvex oder nicht
     * im Uhrzeigersinn sind, wird eine Laufzeitausnahme ausgelöst.
     *
     * @param x x Koordinaten
     * @param y y Koordinaten
     */
    public final void setVertices(double[] x, double[] y) {
        n = x.length;
        coordinates_x = new double[n];
        coordinates_y = new double[n];
        Vector previous = new Vector(x[0] - x[n - 1], y[0] - y[n - 1]);
        Vector actual;
        for (int i = 0; i < n; i++) {
            coordinates_x[i] = x[i];
            coordinates_y[i] = y[i];
        }
        int j; // next point
        for (int i = 0; i < n; i++) {
            j = (i + 1) % n;
            actual = new Vector(coordinates_x[j] - coordinates_x[i],
                    coordinates_y[j] - coordinates_y[i]);
            if (previous.crossproduct(actual) < 0) {
                throw new ConcaveCornerRuntimeException(i,
                        new Point(coordinates_x[(i - 1 + n) % n], coordinates_y[(i - 1 + n) % n]),
                        new Point(coordinates_x[i], coordinates_y[i]),
                        new Point(coordinates_x[j], coordinates_y[j]));
            }
            previous = actual;
        }
        coordinates_final_x = new double[n];
        coordinates_final_y = new double[n];
        transform();
    }

    /* (non-Javadoc)
     * @see de.lathanda.sgl.geom2d.Outline#getOutlineType()
     */
    public Types getOutlineType() {
        return Types.POLYGON;
    }

    /**
     * Berechnet aus den ursprünglichen Koordinaten, die verschobenen und
     * gedrehten Koordinaten. Es ist wichtig, dass immer von den original Daten
     * ausgegangen wird um eine Anhäufung von Rundungsfehler zu vermeiden.
     */
    private void transform() {
        if (coordinates_x == null) {
            return;
        }

        Matrix m = new Matrix(p.getX(), p.getY(), angle, 1, 1);
        m.transform(coordinates_x, coordinates_y, coordinates_final_x,
                coordinates_final_y);
        right = Double.NEGATIVE_INFINITY;
        left = Double.POSITIVE_INFINITY;
        top = Double.NEGATIVE_INFINITY;
        bottom = Double.POSITIVE_INFINITY;
        for (int i = 0; i < n; i++) {
            if (coordinates_final_x[i] < left) {
                left = coordinates_final_x[i];
            }
            if (coordinates_final_x[i] > right) {
                right = coordinates_final_x[i];
            }
            if (coordinates_final_y[i] < bottom) {
                bottom = coordinates_final_y[i];
            }
            if (coordinates_final_y[i] > top) {
                top = coordinates_final_y[i];
            }
        }
    }

    /**
     * Prüft ob der Punkt (x,y) innerhalb des Polygons liegt.
     *
     * @param x x Koordinate
     * @param y y Koordinate
     * @return Wahr, wenn (x,y) innerhalb des Polygons liegt
     */
    @Override
    public boolean contains(double x, double y) {
        Vector v;
        Vector w;
        for (int i = 0, j; i < n; i++) {
            j = (i + 1) % n;
            v = new Vector(coordinates_final_x[j] - coordinates_final_x[i],
                    coordinates_final_y[j] - coordinates_final_y[i]);
            w = new Vector(x - coordinates_final_x[i], y
                    - coordinates_final_y[i]);
            if (v.crossproduct(w) < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Berechnet den Abstand zwischen dem Polygon und dem Punkt. Ein negativer
     * Wert bedeutet, dass der Punkt innerhalb liegt mit dem entsprechendem
     * Abstand zum Rand.
     *
     * @param x x Koordinate
     * @param y y Koordinate
     * @return Abstand zwischen dem Punkt und dem Polygon
     */
    @Override
    public double getDistance(double x, double y) {
        Vector v;
        Vector w;
        double distance = Double.POSITIVE_INFINITY;
        double edgeDistance = 0;
        double projection = 0;
        boolean within = true;
        for (int i = 0, j; i < n; i++) {
            j = (i + 1) % n;
            v = new Vector(coordinates_final_x[j] - coordinates_final_x[i],
                    coordinates_final_y[j] - coordinates_final_y[i]);
            w = new Vector(x - coordinates_final_x[i],
                    y - coordinates_final_y[i]);
            edgeDistance = w.getPerpendicularLength(v);
            projection = w.getProjectionLength(v);

            if (edgeDistance > 0) {
                within = false;
            } else {
                edgeDistance = -edgeDistance;
            }
            distance = Math.min(distance, w.getLength());
            if (0 < projection && projection < v.getLength()) {
                distance = Math.min(distance, edgeDistance);
            }
        }
        return (within) ? -distance : distance;
    }

    /**
     * Aktuallisiert Daten
     */
    protected void positionChanged() {
        transform();
    }

    /**
     * Aktuallisiert Daten
     */
    protected void angleChanged() {
        transform();
    }

	@Override
	public void draw(Picture picture) {
		picture.drawPolygon(coordinates_final_x, coordinates_final_y);
		
	}
}
