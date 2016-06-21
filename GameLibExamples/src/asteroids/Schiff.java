package asteroids;

import java.awt.Color;
import java.awt.event.KeyEvent;

import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.base.math.Vector;
import de.lathanda.eos.game.Game;
import de.lathanda.eos.game.Sprite;
import de.lathanda.eos.game.geom.Polygon;

public class Schiff extends Sprite {
	Asteroids spiel;

	Vector bewegung;
	int hitze;
	int unverwundbar;
	int steamCounter;

	public Schiff(Asteroids spiel, int x, int y) {
		this.spiel = spiel;
		double[] xk = new double[] { -6, 5, -6 };
		double[] yk = new double[] { 4, 0, -4 };
		shape = new Polygon(xk, yk);
		shape.moveTo(x, y);
		bewegung = new Vector(0, 0);
	}

	@Override
	public void render(Picture g) {
		if (unverwundbar > 0) {
			g.setFillColor(Color.BLUE);
			g.drawEllipse(shape.getX(), shape.getY(), 10, 10);
		}
		g.setFillColor(Color.WHITE);
		g.drawShape(shape);
	}

	@Override
	public void update(Game game) {
		
		hitze = Math.max(hitze - 1, 0);
		unverwundbar = Math.max(unverwundbar - 1, 0);

		spiel.bewegen(shape, bewegung);
		if (game.isKeyDown(KeyEvent.VK_LEFT)) {
			shape.rotate(Math.PI / 32);
		}
		if (game.isKeyDown(KeyEvent.VK_RIGHT)) {
			shape.rotate(-Math.PI / 32);
		}
		if (game.isKeyDown(KeyEvent.VK_UP)) {
			bewegung = bewegung.add(new Vector(shape.getAngle()).multiply(0.1)).restrict(0, 3);
			if (steamCounter <= 0) {
				Point loc = new Point(shape.getCenter());
				loc.move(new Vector(shape.getAngle()).multiply(-6d));
				game.addSprite(new Steam(loc));
				steamCounter = 4;
			}
		}
		steamCounter--;

		if (hitze == 0 && game.isKeyDown(KeyEvent.VK_SPACE)) {
			hitze = 40;
			Vector b = new Vector(shape.getAngle());
			b.addLength(1);
			b.add(bewegung);
			game.addSprite(new Kugel(spiel, shape.getCenter(), b));
		}
	}

	public boolean processCollision(Sprite b, Game game) {
		if (unverwundbar == 0 && b instanceof Asteroid) {
			spiel.spielerGetroffen();
		}
		return false;
	}

	public void neustart() {
		shape.moveTo(0, 0);
		bewegung = new Vector(0, 0);
		hitze = 0;
		unverwundbar = 90;
	}
}
