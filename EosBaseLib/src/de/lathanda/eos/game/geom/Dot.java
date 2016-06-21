package de.lathanda.eos.game.geom;

import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.math.Point;



/**
 * \brief Punkt
 * 
 * Diese Klasse stellt einen Umriss mit minimaler Größe dar.
 * 
 * @author Lathanda
 * 
 */
public class Dot extends Shape {
	/**
	 * 
	 * @param x
	 *            x Position
	 * @param y
	 *            y Position
	 */
	public Dot(int x, int y) {
		p = new Point(x,y);
		bottom    = y;
		top = y + 1;
		left   = x;
		right  = x + 1;
		angle = 0;
	}

	/**
	 * Umrissart
	 * 
	 * @return Punktumriss
	 */
        @Override
	public Types getOutlineType() {
		return Types.DOT;
	}

	/**
	 * prüft ob sich der Punkt bei (x,y) befindet
	 * 
	 * @param x
	 *            x Koordinate
	 * @param y
	 *            y Koordinate
	 * @return Wahr wenn die Werte identisch sind, was fast unmöglich ist
	 */
	@Override
	public boolean contains(double x, double y) {
		return p.getX() == x && p.getY() == y;
	}

	/**
	 * @param x
	 *            x Koordinate
	 * @param y
	 *            y Koordinate
	 * @return Abstand zu diesem Punkt
	 */
	@Override
	public double getDistance(double x, double y) {
		return p.getDistance(x, y);
	}

	/**
	 * Aktuallisiert Daten
	 */
	protected void positionChanged( ) {
		this.left = p.getX();
		this.right = p.getX() + 1;
		this.bottom = p.getY();
		this.top = p.getY() + 1;
	}
	/**
	 * Tut nichts
	 */
	protected void angleChanged() {}

	@Override
	public void draw(Picture picture) {
		picture.drawRect(p.getX(), p.getY(), 0.25, 0.25);
		
	}	
}
