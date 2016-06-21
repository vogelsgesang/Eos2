package ewire;

import java.awt.Color;

import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.math.Vector;
import de.lathanda.eos.game.Game;
import de.lathanda.eos.game.Sprite;
import de.lathanda.eos.game.geom.Circle;
import de.lathanda.eos.game.geom.ShapeGroup;

public class Paddle extends Sprite {
	boolean burn;

	public Paddle(int x, int y, double angle) {
		ShapeGroup shapeGroup = new ShapeGroup();
		Circle circleLeft = new Circle(5, -15, 0);
		Circle circleRight = new Circle(5, 15, 0);
		shapeGroup.add(circleLeft);
		shapeGroup.add(circleRight);
		shapeGroup.setAngle(angle);
		shapeGroup.moveTo(x, y);
		shape = shapeGroup;
	}

	@Override
	public void render(Picture g) {
		if (burn) {
			g.setFillColor(Color.ORANGE);
		} else {
			g.setFillColor(Color.LIGHT_GRAY);
		}
		g.drawShape(shape);
	}

	@Override
	public void update(Game game) {
		burn = false;
		Vector diff = new Vector(shape.getCenter(), game.getMouse());
		if (!diff.isZero()) {
			diff.restrict(0, 1);
		}
		shape.move(diff);
		if (game.isMouseDown(1)) {
			shape.rotate(-0.01);
		}
		if (game.isMouseDown(3)) {
			shape.rotate(0.01);
		}
	}

	@Override
	public boolean processCollision(Sprite b, Game game) {
		burn = true;

		return false;
	}
}
