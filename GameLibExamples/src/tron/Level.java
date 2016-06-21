package tron;

import java.awt.Color;
import java.awt.Font;
import java.text.MessageFormat;

import de.lathanda.eos.base.Alignment;
import de.lathanda.eos.base.Picture;
import de.lathanda.eos.game.Game;
import de.lathanda.eos.game.Sprite;

public class Level extends Sprite {
	boolean running = false;
	Player1 p1;
	Player2 p2;
	static final Font COUNTDOWN = new Font(Font.SERIF, Font.BOLD, 40);
	public final static int PLAYER1 = 0;
	public final static int PLAYER2 = 1;
	public final static int WALL1 = 2;
	public final static int WALL2 = 3;
	public final static int WALL = 4;
	int countDown = 150;

	public Level() {
		p1 = new Player1(this);
		p2 = new Player2(this);
	}
	public void init(Game game) {
		game.addSprite(p1);
		game.addSprite(p2);
		game.addSprite(new Block(Color.BLACK, 0, -99, 300, 2));
		game.addSprite(new Block(Color.BLACK, 0,  99, 300, 2));
		game.addSprite(new Block(Color.BLACK, -149, 0, 2, 200));
		game.addSprite(new Block(Color.BLACK,  149, 0, 2, 200));		
	}

	@Override
	public void render(Picture g) {
		g.setFont(COUNTDOWN);
		g.setTextAlignment(Alignment.CENTER, Alignment.CENTER);
		if (countDown > 0) {
			switch (countDown % 3) {
			case 0:
				g.setLineColor(Color.RED);
				break;
			case 1:
				g.setLineColor(Color.GRAY);
				break;
			case 2:
				g.setLineColor(Color.YELLOW);
				break;
			}
			g.drawText(MessageFormat.format(
					"{0,number, 0.00} sec until start", countDown / 30.0), 0, 0);
		}
	}

	@Override
	public void update(Game game) {
		if (countDown > 0) {
			countDown--;
		}
		if (countDown == 0) {
			running = true;
			countDown = -1;
		}
	}
	public boolean isRunning() {
		return running;
	}
	public void gameOver(int winner, Game game) {
		running = false;
		game.addSprite(new GameOver(winner));
	}
}
