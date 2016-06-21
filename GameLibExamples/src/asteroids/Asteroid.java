package asteroids;

import java.awt.Color;

import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.base.math.Vector;
import de.lathanda.eos.game.Game;
import de.lathanda.eos.game.Sprite;
import de.lathanda.eos.game.geom.Circle;

public class Asteroid extends Sprite {
	Asteroids spiel;
	int art;
	Vector v;

	public Asteroid(Asteroids spiel, Point p, int art, double winkel) {
		this.spiel = spiel;
		this.art = art;
		shape = new Circle(art * 5, p);
		v = new Vector(winkel).multiply((4 - art) * 0.25);
	}

	public void render(Picture g) {
		g.setFillColor(Color.GRAY);
		g.drawShape(shape);
	}

	public void update(Game game) {
		spiel.bewegen(shape, v);
	}

	public boolean processCollision(Sprite b, Game game) {
		if (b instanceof Kugel) {
			game.removeSprite(this);
			spiel.asteroidGetroffen(art, game);
			if (art > 1) {
				double winkel = spiel.rnd.nextDouble();
				game.addSprite(new Asteroid(spiel, shape.getCenter(), art - 1, winkel + Math.PI * 2 / 3));
				game.addSprite(new Asteroid(spiel, shape.getCenter(), art - 1, winkel + Math.PI * 4 / 3));
				game.addSprite(new Asteroid(spiel, shape.getCenter(), art - 1, winkel));
			}
		}
		return false;
	}
}
