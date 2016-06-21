package de.lathanda.eos.base.math;

import java.text.MessageFormat;

/**
 * \brief Ort
 *
 * Diese Klasse stellt einen Ort dar.
 *
 * @author Lathanda
 */
public class Point {

    /**
     * \brief x Koordinate
     */
    protected double x;
    /**
     * \brief y Koordinate
     */
    protected double y;

    /**
     * Neuer Punkt
     *
     * @param x x Koordinate
     * @param y y Koordinate
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Neuer Punkt bei (0,0) + v
     *
     * @param v Verschiebungsvektor
     */
    public Point(Vector v) {
        this.x = v.getdx();
        this.y = v.getdy();
    }

    /**
     * Neuer Punkt
     *
     * @param p Punkt
     */
    public Point(Point p) {
        this.x = p.getX();
        this.y = p.getY();
    }
    /**
     * Skaliert den Punkt relativ zum Ursprung.
     * @param factor
     */
    public void scale(double factor) {
    	x *= factor;
    	y *= factor;
    }
    /**
     * X Koordinate abfragen
     *
     * @return x Koordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Y Koordinate abfragen
     *
     * @return y Koordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Abstand zu einem zweiten Punkt berechnen.
     *
     * @param b Zweiter Punkt
     * @return Abstand zwischen diesem Punkt und dem Punkt b, im Euklidischen
     * Raum
     */
    public double getDistance(Point b) {
        return getDistance(b.getX(), b.getY());
    }

    /**
     * Abstand zu einem Koordiantenpaar.
     *
     * @param x x Koordinate
     * @param y y Koordinate
     * @return Abstand zwischen diesem Punkt und dem Punkt (x, y), im
     * Euklidischen Raum
     */
    public double getDistance(double x, double y) {
        return Math.sqrt((x - this.x) * (x - this.x) + (y - this.y) * (y - this.y));
    }
    public void negateX() {
        x = -x;
    }
    public void negateY() {
        y = -y;
    }
    
    public double getPhi() {
	double phi = Math.atan(y/x);
	if (x < 0) {
            return phi + Math.PI;
	} else {
            return phi;
        }
    }
    public double getR() {
        return Math.sqrt(x*x+y*y);
    }
    /**
     * Verschiebt den Punkt
     *
     * @param dX x Verschiebung
     * @param dY y Verschiebung
     */
    public void move(double dX, double dY) {
        x += dX;
        y += dY;
    }

    /**
     * Verschiebt den Punkt
     *
     * @param v Verschiebungsvektor
     */
    public void move(Vector v) {
        x += v.getdx();
        y += v.getdy();
    }

    /**
     * Verschiebt den Punkt zu den Koordinaten (x,y)
     *
     * @param x x Koordinate
     * @param y y Koordinate
     */
    public void moveTo(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Verschiebt den Punkt zu den Koordinaten (x,y)
     *
     * @param p Neuer Ort
     */
    public void moveTo(Point p) {
        this.x = p.getX();
        this.y = p.getY();
    }

    /**
     * Debugginginformationen
     *
     * @return Zeichenkette für Fehlersuche
     */
    @Override
    public String toString() {
        return MessageFormat.format("({0,number,#.00}|{1,number,#.00})",
                new Object[]{x, y});
    }
}
