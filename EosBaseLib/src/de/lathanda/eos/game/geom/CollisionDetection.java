package de.lathanda.eos.game.geom;

import de.lathanda.eos.game.Game;
import de.lathanda.eos.game.Sprite;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * \brief Kollisionserkennung
 *
 * Diese Klasse ist nicht gegen parallele Zugriffe gesichert (nicht Threadsafe),
 * daher benötigt jeder Thread sein eigenes Objekt.
 *
 * Objekte dieser Klasse sind in der Lage zweidimensionale Kollisionen effizient
 * zu berechnen. Hierzu wird die Kollision in mehreren Phasen berechnen.
 *
 * Zuerst werden Überlappungen über umgebende Rechtecke angenähert.
 *
 * Danach werden potenielle Kollisionen einzeln überprüft.
 *
 * Details:
 *
 * Es wird eine Liste der x Intervalle der Rechtecke erzeugt. 1.x0, 1.x1, 2.x0,
 * 3.x0, 2.x1, 3.x1, 4.x0, 4.x1 Hier überlappen Intervall 2 und 3. Daher kann
 * hier eine Kollision vorliegen. Nun werden die y Grenzen des Kandiaten
 * geprüft. Besteht die Kollision auch diesen Vortest, wird die exakte
 * Berechnung durchgeführt
 *
 * Dieses Verfahren ist im Prinzip immer noch O(n²). Dies lässt sich aber
 * grundsätzlich nicht vermeiden, da es n² Kollisionen geben kann.
 *
 * @author Lathanda
 */
public class CollisionDetection {

	/**
	 * \brief Sortierte Liste der Spielobjekte.
	 */
	private final SortedSet<Sprite> list = new TreeSet<>();
	private final Game game;
	/**
	 * Neues Objekt
	 */
	public CollisionDetection(Game game) {
		this.game = game;
	}

	/**
	 * Registriert ein Spielobjekt für die Kollisionserkennung, dies darf
	 * niemals von {@link Sprite#render(Picture)} aus geschehen. Neue Objekte
	 * dürfen nur über {@link Sprite#update()} registriert werden.
	 *
	 * @param sprite
	 *            Spielobjekt
	 */
	public void addObject(Sprite sprite) {
		list.add(sprite);
	}

	/**
	 * Entfernt ein Spielobjekt aus der Kollisionserkennung
	 *
	 * @param sprite
	 *            Zu löschendes Weltobjekt
	 */
	public void removeObject(Sprite sprite) {
		list.remove(sprite);
	}

	/**
	 * Berechnet alle Kollisionen. Abhängig von der Anzahl der Objekte und
	 * tatsächlich vorliegender Kollisionen kann dieser Aufruf sehr lange
	 * dauern.
	 *
	 */
	public void checkCollision() {
		/*
		 * result is the only set that scales with O(n²), but we cannot avoid
		 * that effect, as there can be so many collisions
		 */

		SortedSet<ShapeOrder> xList = new TreeSet<>();
		SortedSet<Shape> open = new TreeSet<>();
		Collision col;

		// phase 1: sort objects
		Shape shape;
		for (Sprite w : list) {
			shape = w.getShape();
			if (shape != null) {
				shape.setSprite(w);
				xList.add(new ShapeOrder(ShapeOrder.OrderType.X0, shape));
				xList.add(new ShapeOrder(ShapeOrder.OrderType.X1, shape));
			} else {
				// this is legal
				// it can be used in order to temporary prevent collisions
			}
		}

		// phase 2: scan for potential collisions
		// we enter every potential x collision into a tree structure
		for (ShapeOrder shapeOrder : xList) {
			switch (shapeOrder.bracket) {
			case OPEN:
				open.add(shapeOrder.shape);
				break;
			case CLOSE:
				open.remove(shapeOrder.shape);

				// every id that is open collides with this id
				for (Shape id : open) {
					if (id.getBottom() < shapeOrder.shape.getTop() && shapeOrder.shape.getBottom() < id.getTop()) {
						col = new Collision(shapeOrder.shape, id);
						// the rectangle bounds intersect, now we check if they
						// really collide
						// this avoids testing every pair with the really slow
						// exact calculations
						if (col.verifyCollision()) {
							if (!col.a.getSprite().processCollision(col.b.getSprite(), game)) {
								col.b.getSprite().processCollision(col.a.getSprite(), game);
							}
						}
					}
				}
				break;
			}
		}
	}
}
