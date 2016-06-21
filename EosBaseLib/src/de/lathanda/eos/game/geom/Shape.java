package de.lathanda.eos.game.geom;

import de.lathanda.eos.game.Sprite;
import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.base.math.Vector;
import java.text.MessageFormat;

/**
 * \brief Form
 *
 * Formen haben mehrere Aufgaben:
 * <ul>
 * <li> Zeichenprimitive
 * <li> Geometrische Grundlage für Kollisionsberechnung im 2 Dimensionalen
 * <li> Prüfen ob Mausklicks innerhalb eines Areals waren
 * <li> Speichern von Ort und Richtungsinformationen
 * <li> Grafisches Visuallisieren von Ort und Richtungsangaben
 * </ul>
 *
 * Die Methode {@link Picture#drawShape(Shape)} zeichnet eine Form.
 *
 * Formen in diesem Paket kennen sich untereinander und es ist für jedes Paar
 * ein Abstands und Entfernungsalgorithmus implementiert. Außerdem kann jede
 * Form durch {@link #contains(de.lathanda.eos.base.math.Point)} prüfen, 
 * ob ein Punkt innerhalb liegt.
 *
 * Über {@link de.lathanda.eos.base.math.Point#move(Vector)} kann eine Form bewegt werden, 
 * alle Formen können auch als Punkte {@link de.lathanda.eos.base.math.Point} verwendet werden.
 * Über die Methode
 * {@link #setAngle(double)}, {@link #getAngle()}, {@link #rotate(double)} kann
 * eine Form gedreht werden. {@link #move{double}} kann die Form in diese
 * Richtung bewegen. 0° sind in Richtung (1,0) also nach rechts.
 *
 *
 * @author Lathanda
 */
public abstract class Shape implements Comparable<Shape> {

    /**
     * \brief Linke Grenze
     */
    protected double left;
    /**
     * \brief Untere Grenze
     */
    protected double bottom;
    /**
     * \brief Rechte Grenze
     */
    protected double right;
    /**
     * \brief Obere Grenze
     */
    protected double top;
    /**
     * \brief Drehwinkel
     */
    protected double angle;
    /**
     * \brief Ort
     */
    protected Point p;
    /**
     * \brief ID des zugehörigen Weltobjekts
     */
    protected Integer id; // needed as unique id
    /**
     * \brief Zugehöriges Weltobjekt
     */
    protected Sprite sprite;

    /**
     * neue Form
     */
    protected Shape() {
    }
    /**
     * Liefert das Rotationszentrum der Form. In der Regel ist dies die Mitte.
     * @return Zentrum
     */
    public Point getCenter() {
    	return p;
    }
    /**
     * Setzt den Sprite, diese Methode wir implizit durch die
     * Kollisionserkennung gesetzt.
     *
     * @param sprite Sprite
     */
    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        this.id = sprite.getID();
    }

    /**
     * Liefert die ID des zugehörigen Sprites
     *
     * @return ID des zugehörigen Sprites
     */
    public Integer getID() {
        return id;
    }

    /**
     * Liefert das zugehörige Weltobjekt
     *
     * @return Weltobjekt
     */
    public Sprite getSprite() {
        return sprite;
    }

    /**
     * Setzt den Drehwinkel
     *
     * @param angle Drehwinkel in Bogenmaß
     */
    public void setAngle(double angle) {
        this.angle = angle;
        angleChanged();
    }

    /**
     * Frägt den Drehwinkel ab
     *
     * @return angle Drehwinkel im Bogenmaß
     */
    public double getAngle() {
        return angle;
    }

    /**
     * Dreht die Form. Um zu vermeiden, dass sich Rundungsfehler ansammeln
     * sollte {@link #setAngle(double)} benutzt werden.
     *
     * @param angle Winkeländerung
     */
    public void rotate(double angle) {
        this.angle += angle;
        this.angleChanged();
    }

    /**
     * Liefert die linke Grenze
     *
     * @return Linke Grenze
     */
    public double getLeft() {
        return left;
    }

    /**
     * Liefert die rechte Grenze
     *
     * @return Rechte Grenze
     */
    public double getRight() {
        return right;
    }

    /**
     * Liefert die Obere Grenze
     *
     * @return Obere Grenze
     */
    public double getBottom() {
        return bottom;
    }

    /**
     * Liefert die untere Grenze
     *
     * @return Untere Grenze
     */
    public double getTop() {
        return top;
    }

    /**
     * Gibt die Breite des umgebenden Rechtecks der Form zurück
     *
     * @return Breite
     */
    public double getWidth() {
        return getRight() - getLeft();
    }

    /**
     * Gibt die Höhe des umgebenden Rechtecks der Form zurück
     *
     * @return Höhe
     */
    public double getHeight() {
        return getTop() - getBottom();
    }

    /**
     * Vergleicht zwei Formen anhand der ID des Sprites, erlaubt es Formen in
     * Bäumen zu organisieren.
     */
    @Override
    public int compareTo(Shape o) {
        return id - o.id;
    }

    /**
     * Prüft ob ein Punkt innerhalb der Form liegt.
     *
     * @param p Punkt
     * @return Wahr wenn der Punkt innerhalb der Form liegt
     */
    public boolean contains(Point p) {
        return contains(p.getX(), p.getY());
    }

    /**
     * Testet ob diese Form mit der Form b überlappt.
     * {@link Intersection#intersects(Shape, Shape)}
     *
     * @param b Zweite Form
     * @return Wahr wenn beide Formen gemeinsame Punkte haben
     */
    public boolean intersects(Shape b) {
        return Intersection.intersects(this, b);
    }

    /**
     * Berechnet den Abstand zwischen zwei Formen. Negativ bedeutet die beiden
     * Formen überlappen. {@link Distance#distance(Shape, Shape)}
     *
     * @param b Zweite Form
     * @return Abstand zwischen beiden Formen
     */
    public double distance(Shape b) {
        return Distance.distance(this, b);
    }

    /**
     * Ermittelt ob der Punkt (x,y) innerhalb der Form liegt.
     *
     * @param x x Koordinate
     * @param y y Koordinate
     * @return wahr, wenn (x,y) innerhalb der Form liegt.
     */
    public abstract boolean contains(double x, double y);

    /**
     * Ermittelt den Abstand zwischen dem Punkt p und der Form.
     *
     * @param p Punkt
     * @return Abstand zwischen Punkt und Form
     */
    public final double getDistance(Point p) {
        return getDistance(p.getX(), p.getY());
    }

    /**
     * Ermittelt den Abstand zwischen dem Punkt (x,y) und der Form.
     *
     * @param x x Koordinate
     * @param y y Koordinate
     * @return Abstand zwischen der Form und dem Punkt (x,y)
     */
    public abstract double getDistance(double x, double y);

    /**
     * Art der Form, für die Auswahl der Algorithmen
     *
     * @return Art der Form
     */
    abstract Types getOutlineType();

    /* (non-Javadoc)
     * @see de.lathanda.sgl.geom2d.Point#getX()
     */
    public double getX() {
        return p.getX();
    }

    /* (non-Javadoc)
     * @see de.lathanda.sgl.geom2d.Point#getY()
     */
    public double getY() {
        return p.getY();
    }

    /* (non-Javadoc)
     * @see de.lathanda.sgl.geom2d.Point#moveTo(double, double)
     */
    public void moveTo(double x, double y) {
        p.moveTo(x, y);
        positionChanged();
    }

    /* (non-Javadoc)
     * @see de.lathanda.sgl.geom2d.Point#moveTo(de.lathanda.sgl.geom2d.Point)
     */
    public void moveTo(Point p) {
        this.p.moveTo(p);
        positionChanged();
    }

    /* (non-Javadoc)
     * @see de.lathanda.sgl.geom2d.Point#move(double, double)
     */
    public void move(double dX, double dY) {
        p.move(dX, dY);
        positionChanged();
    }

    /* (non-Javadoc)
     * @see de.lathanda.sgl.geom2d.Point#move(de.lathanda.sgl.geom2d.Vector)
     */
    public void move(Vector v) {
        p.move(v);
        positionChanged();
    }

    /**
     * Bewegt die Form nach Vorne. Vorne wird durch den Winkel festgelegt.
     *
     * @param length Bewegungsweite
     */
    public void move(double length) {
        move(new Vector(angle).setLength(length));
    }

    /**
     * Wird aufgerufen, wenn sich die Position verändert hat.
     */
    protected abstract void positionChanged();

    /**
     * Wird aufgerufen wenn sich der Winkel geändert hat.
     */
    protected abstract void angleChanged();

    /**
     * Lesbare Darstellung der Form
     *
     * @return Information
     */
    @Override
    public String toString() {
        return MessageFormat.format("({0}|{1})/({2}|{3})", new Object[]{left,
            bottom, right, top});
    }
    public abstract void draw(Picture picture);
}
