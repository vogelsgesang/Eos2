package billard;

import java.awt.Color;

import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.math.Vector;
import de.lathanda.eos.game.Game;
import de.lathanda.eos.game.Sprite;
import de.lathanda.eos.game.geom.Circle;

public class Ball extends Sprite {
	Vector v;
	Color c;
	static boolean moving;

	public Ball(int x, int y, Color c) {
		this.c = c;
		v = new Vector(0, 0);
		shape = new Circle(5, x, y);
	}

	public void render(Picture g) {
		g.setFillColor(c);
		g.drawShape(shape);
	}

	public void update(Game game) {
		if (v.getdx() != 0 || v.getdy() != 0) {
			moving = true;
		}
		if (moving) {
			shape.move(v);
			v = v.addLength(-0.003);
		}

	}

	public boolean processCollision(Sprite b, Game game) {

		if (b instanceof Ball) {
			Ball ball2 = (Ball) b;
			Vector abstand = new Vector(shape.getCenter(), ball2.shape.getCenter());
			//Testen ob die Kollision real ist, oder nur ein Simulationsfehler
			if (v.substract(ball2.v).getProjectionLength(abstand) < 0) {
				// Ball a bewegt sich nicht auf die Kollisionsstelle zu =>
				// Fehler durch Methode der kleinen Schritte
				return true;
			}

			// Impulszerlegung
			// L Kollisionsrichtung H Senkrecht zur Kollisionsrichtung
			Vector aH = this.v.getPerpendicular(abstand);
			Vector bH = ball2.v.getPerpendicular(abstand);
			Vector aL = this.v.getProjection(abstand);
			Vector bL = ball2.v.getProjection(abstand);
			// Impulsaddition
			this.v = aH.add(bL);
			ball2.v = bH.add(aL);
			return true;
		} else if (b instanceof Border) {
			Border border = (Border) b;
			if (v.getProjectionLength(border.perpendicular) > 0) {
				return true;
			}
			Vector aH = v.getPerpendicular(border.perpendicular);
			Vector aL = v.getProjection(border.perpendicular);
			this.v = aH.substract(aL);
			return true;
		}
		return false;
	}
}
