package tron;

import java.awt.Color;
import java.awt.event.KeyEvent;

import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.Sound;
import de.lathanda.eos.game.Game;
import de.lathanda.eos.game.Sprite;
import de.lathanda.eos.game.geom.Rectangle;

public class Player2 extends Sprite {
	private Level level;
	private int outoforder = 0;
	private Sound boom = new Sound("sound/explosion/bomb-02.wav");
	public Player2(Level level) {
		this.level = level;
		shape = new Rectangle(-0.5, -0.5, 1, 1);
		shape.moveTo(50, 0);
		shape.setAngle(Math.PI * 3.0 / 2.0);
	}

	@Override
	public void render(Picture g) {
		g.setFillColor(Color.BLUE);
		g.drawShape(shape);
		if (outoforder > 0) {
			outoforder++;
			switch (outoforder % 3) {
			case 0:
				g.setFillColor(Color.RED);
				break;
			case 1:
				g.setFillColor(Color.YELLOW);
				break;
			case 2:
				g.setFillColor(Color.GRAY);
				break;
			}
			g.drawEllipse(shape.getX(), shape.getY(), 4, 4);
		}
	}

	@Override
	public void update(Game game) {
		if (level.isRunning()) {
			game.addSprite(new Block(Color.BLUE, shape.getX(), shape.getY(), 1, 1));

			if (game.isKeyDown(KeyEvent.VK_LEFT)) {
				shape.setAngle(Math.PI * 3.0 / 2.0);
			}

			if (game.isKeyDown(KeyEvent.VK_RIGHT)) {
				shape.setAngle(Math.PI / 2.0);
			}
			if (game.isKeyDown(KeyEvent.VK_UP)) {
				shape.setAngle(0);
			}

			if (game.isKeyDown(KeyEvent.VK_DOWN)) {
				shape.setAngle(Math.PI);
			}
			shape.move(
					Math.sin(shape.getAngle()) * 1.1,
					Math.cos(shape.getAngle()) * 1.1
			);
		}
	}

	public void boom() {
		if (outoforder == 0) {
			outoforder = 1;
			boom.play();
		}
	}

	public boolean processCollision(Sprite b, Game game) {
		if (!level.isRunning()) return true;
		boom();
		if (b instanceof Player1) {
			level.gameOver(2, game);
		} else {
			level.gameOver(0, game);
		}
		return true;
	}
}
