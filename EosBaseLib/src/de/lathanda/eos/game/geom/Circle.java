package de.lathanda.eos.game.geom;

import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.math.Point;

/**
 * \brief Kreis
 *
 * Die Kreis Objekte verwalten die Daten eines Kreises. Sie können über den
 * Mittelpunkt bewegt werden. Der Ort des Kreises und damit sein
 * Rotationszentrum ist also immer der Mittelpunkt. Über
 * {@link ShapeGroup#add(Shape)} kann man eine Form bauen die anderes Rotationszentrum besitzt.
 *
 * Beispiel 
 * \code 
 *   class Ball extends Sprite { 
 *      private Circle ball; 
 *      ... andere Attribute, Konstruktoren etc. 
 *      Shape getShape() { 
 *        return ball; 
 *      } 
 *      ... //andere Methoden 
 *      boolean processCollision(Sprite b, Game game) { 
 *        if (b instanceof Ball) { 
 *          Circle ball2 = ((Ball)b).ball;
 *           ...  //Berechnung des Impulses zwischen this.ball und b.ball return true; 
 *        } 
 *        ... //Kollisionen mit anderen Klassen 
 *        return false; 
 *      } 
 *      ... // andere Methoden 
 *      public void update(Game game) { 
 *        ball.move(...); 
 *        //ein Vektor oder zwei Werte für ΔX und ΔY um den Kreis zu bewegen 
 *      }
 *    } 
 * \endcode 
 * Man beachte hier insbesondere, dass ein Objekt der Klasse Ball auf private
 * Attribute anderer Objekte der Klasse Ball zugreifen darf. Entscheidend ist
 * für private lediglich die Klasse, ob es das selbe Objekt ist spielt keine
 * Rolle.
 *
 * @author Lathanda
 *
 */
public class Circle extends Shape {

    /**
     * \brief Radius
     */
    protected double radius;

    /**
     * Erzeugt einen Kreis mit Mittelpunkt (0,0)
     *
     * @param radius Radius
     */
    public Circle(double radius) {
        this(radius, 0, 0);
    }

    /**
     * Erzeugt einen Kreis.
     *
     * @param radius Radius
     * @param p Mittelpunkt
     */
    public Circle(double radius, Point p) {
        this(radius, p.getX(), p.getY());
    }

    /**
     * Erzeugt einen Kreis.
     *
     * @param radius Radius
     * @param x x Koordinate
     * @param y y Koordinate
     */
    public Circle(double radius, double x, double y) {
        super();
        this.radius = radius;
        left = x - radius;
        right = x + radius;
        bottom = y - radius;
        top = y + radius;
        p = new Point(x, y);
    }

    /**
     * Liefert den Typ des Umrisses, dies dient der Auswahl des korrekten
     * Kollisionsberechnugnsverfahrens.
     * @return 
     */
    @Override
    Types getOutlineType() {
        return Types.CIRCLE;
    }

    /**
     * Prüft ob der Punkt p innerhalb des Kreises liegt.
     *
     * @param p Punkt
     * @return
     */
    @Override
    public boolean contains(Point p) {
        return contains(p.getX(), p.getY());
    }

    /**
     * Prüft ob der Punkt (x,y) innerhalb des Kreises liegt.
     *
     * @param x x Koordinate
     * @param y y Koordinate
     * @return 
     */
    @Override
    public boolean contains(double x, double y) {
        double dx = this.p.getX() - x;
        double dy = this.p.getY() - y;
        return dx * dx + dy * dy < radius * radius;
    }

    /**
     * Ermittel den Abstand des Punktes (x,y) vom Kreis. Negativ bedeutet der
     * Punkt liegt innerhalb.
     *
     * @param x x Koordinate
     * @param y y Koordinate
     * @return 
     */
    @Override
    public double getDistance(double x, double y) {
        return p.getDistance(x, y) - radius;
    }

    /**
     * Aktuallisiert Daten
     */
    @Override
    protected void positionChanged() {
        left = p.getX() - radius;
        right = p.getX() + radius;
        bottom = p.getY() - radius;
        top = p.getY() + radius;
    }

    /**
     * Tut nichts, da ein Kreis sich durch drehen nicht ändert.
     */
    @Override
    protected void angleChanged() {
    }

	@Override
	public void draw(Picture picture) {
		picture.drawEllipse(p.getX(), p.getY(), radius, radius);
		
	}
}
