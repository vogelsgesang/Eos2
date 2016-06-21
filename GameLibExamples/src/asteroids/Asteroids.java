package asteroids;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Random;

import de.lathanda.eos.base.Alignment;
import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.base.math.Vector;
import de.lathanda.eos.game.Game;
import de.lathanda.eos.game.Sprite;
import de.lathanda.eos.game.geom.Shape;

public class Asteroids extends Sprite {
	public static void main(String[] args) {
		new Asteroids();
	}

	private int punkte;
	private int zustand; // 0 intro -1 gameover 1... level
	private int asteroiden;
	private Schiff schiff;
	public Random rnd = new Random();
	

	private Asteroids() {
		Game game = new Game(250, 200, Color.BLACK, "Asteroids");
		game.addSprite(this);
		schiff = new Schiff(this, 0, 0);
		game.addSprite(schiff);
	}

	public void render(Picture g) {
		
		switch (zustand) {
		case 0:
			g.setTextAlignment(Alignment.BOTTOM, Alignment.CENTER);
			g.setLineColor(Color.WHITE);
			g.drawText("A S T E R O I D S", 0, 30);
			g.drawText("F1 drücken", 0, 20);
			break;
		case -1:
			g.setTextAlignment(Alignment.BOTTOM, Alignment.CENTER);
			g.setLineColor(Color.WHITE);
			g.drawText("G A M E O V E R", 0, 30);
			g.drawText("F1 drücken", 0, 20);
			g.drawText("Punkte: " + punkte, 0, 10);
			break;
		default:
			g.setLineColor(Color.WHITE);
			g.setTextAlignment(Alignment.TOP, Alignment.LEFT);
			g.drawText("Punkte: " + punkte, -124, 99);
			g.setTextAlignment(Alignment.TOP, Alignment.RIGHT);
			g.drawText("Level:" + zustand, 124, 99);
		}
	}

	public void update(Game game) {
		switch (zustand) {
		case 0:
			if (game.isKeyPressed(KeyEvent.VK_F1)) {
				neuesLevel(game);
			}
			break;
		case -1:
			if (game.isKeyPressed(KeyEvent.VK_F1)) {
				intro();
			}
		}
	}

	public void bewegen(Shape p, Vector v) {
		p.move(v);
		if (p.getX() > 130)
			p.move(-260, 0);
		if (p.getX() < -130)
			p.move(260, 0);
		if (p.getY() > 120)
			p.move(0, -240);
		if (p.getY() < -120)
			p.move(0, 240);
	}

	public void spielerGetroffen() {
		spielAus();
	}

	public void asteroidGetroffen(int size, Game game) {
		if (zustand > 0) {
			asteroiden--;
			punkte = punkte + 100 * (4 - size);
			if (asteroiden == 0) {
				neuesLevel(game);
			}
		}
	}

	public void intro() {
		zustand = 0;
	}

	public void neuesLevel(Game game) {
		if (zustand == 0) {
			punkte = 0;
			game.clearAll();
			game.addSprite(this);
			game.addSprite(schiff);
		}
		zustand++;
		asteroiden = zustand * 13; // 1 + 3 + 3*3
		schiff.neustart();
		for (int i = 0; i < zustand; i++) {
			double a = rnd.nextInt(360) * Math.PI / 180.0;
			Point p = new Point(Math.sin(a) * 100,
					Math.cos(a) * 100);
			game.addSprite(new Asteroid(this, p, 3, a));
		}
	}

	public void spielAus() {
		zustand = -1;
	}

}
