package de.lathanda.eos.game.geom;

import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.base.math.Vector;

/**
 * \brief Rechteck
 *
 * Ein Rechteck ist ein Sonderfall eines Polygons. Man muss aufpassen, wo das
 * Rotationszentrum des Rechteckes liegt. In der Regel ist das Zentrum in der
 * Mitte. Bei einigen Konstruktoren kann man explizit das Zentrum oder die
 * Ränder angeben, hier ist dann (0, 0) das Drehzentrum.
 *
 * @author Lathanda
 *
 */
public class Rectangle extends Polygon {

    /**
     * Leeres Rechteck muss noch mit Daten befüllt werden
     */
    protected Rectangle() {
        super();
    }

    /**
     * Erzeugt ein Rechteck um das Zentrum z.
     *
     * @param z Mitte
     * @param width Breite
     * @param height Höhe
     */
    public Rectangle(Point z, double width, double height) {
        this(width, height);
        moveTo(z);
    }

    /**
     * Erzeugt ein Rechteck, welches an der Position (0,0) zentriert ist.
     *
     * @param width Breite
     * @param height Höhe
     */
    public Rectangle(double width, double height) {
        super(new double[]{-width / 2, -width / 2, width / 2, width / 2},
                new double[]{-height / 2, height / 2, height / 2, -height / 2});
    }

    /**
     * Erzeugt ein Rechteck, welches exakt die angegeben Maße hat. Das
     * Drehzentrum liegt jedoch bei (0,0), weshalb eine Roation unerwartete
     * Ergebnisse erzeugen kann. Ist eine Rotation gewünscht sollte
     * Rectangle(Point,double,double) verwendet werden.
     *
     * @param left linke Grenze
     * @param bottom untere Grenze
     * @param width Breite
     * @param height Höhe
     */
    public Rectangle(double left, double bottom, double width, double height) {
        super(new double[]{left, left, left + width, left + width},
                new double[]{bottom, bottom + height, bottom + height, bottom});
    }

    /**
     * Erzeugt einen Balken zwischen a und b. a und b sind die Mitten der
     * Abschlusskanten.
     *
     * @param a Anfang
     * @param b Ende
     * @param breadth Breite
     */
    public Rectangle(Point a, Point b, double breadth) {
        Vector d = new Vector(a, b).rotateRightAngleCounterClockwise().setLength(breadth / 2);
        setVertices(
                new double[]{a.getX() - d.getdx(), a.getX() + d.getdx(), b.getX() + d.getdx(), b.getX() - d.getdx()},
                new double[]{a.getY() - d.getdy(), a.getY() + d.getdy(), b.getY() + d.getdy(), b.getY() - d.getdy()}
        );
    }

    /* (non-Javadoc)
     * @see de.lathanda.sgl.geom2d.Polygon#getOutlineType()
     */
    public Types getOutlineType() {
        if (angle == 0) {
            return Types.RECTANGLE;
        } else {
            return Types.POLYGON;
        }
    }
}
