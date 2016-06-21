package asteroids;

import java.awt.Color;

import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.base.math.Vector;
import de.lathanda.eos.game.Game;
import de.lathanda.eos.game.Sprite;
import de.lathanda.eos.game.geom.Circle;


public class Kugel extends Sprite {
	Vector v;
	int zeit;
	Asteroids spiel;

	public Kugel(Asteroids spiel, Point p, Vector v) {
		this.spiel = spiel;
		this.v = v;
		shape = new Circle(1, p);
		zeit = 300; // 5 Sekunden
	}


	public void render(Picture g) {
		g.setFillColor(Color.BLUE);
		g.drawShape(shape);
	}

	public void update(Game game) {
		spiel.bewegen(shape, v);
		zeit--;
		if (zeit <= 0) {
			game.removeSprite(this);
		}
	}

	public boolean processCollision(Sprite b, Game game) {
		if (b instanceof Asteroid) {
			game.removeSprite(this);
		}
		return false;
	}
}
