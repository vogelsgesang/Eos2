package tron;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

import de.lathanda.eos.base.Alignment;
import de.lathanda.eos.base.Picture;
import de.lathanda.eos.game.Game;
import de.lathanda.eos.game.Sprite;

public class GameOver extends Sprite {

	static final Font TITLE = new Font(Font.SANS_SERIF, Font.BOLD, 24);
	static final Font SUB_TITLE = new Font(Font.SERIF, Font.BOLD, 16);
	static final Font WINNER = new Font(Font.SERIF, Font.BOLD, 30);
	private int winner;

	public GameOver(int winner) {
		super(-1);
		this.winner = winner;
	}

	@Override
	public void render(Picture g) {
		g.setTextAlignment(Alignment.CENTER, Alignment.CENTER);
		g.setFont(TITLE);
		g.drawText("TRON", 0, 10);
		g.setFont(SUB_TITLE);
		g.drawText("G A M E O V E R, Press F1 to continue", 0, 0);
		g.setFont(WINNER);
		switch (winner) {
		case 0:
			g.setLineColor(Color.RED);
			g.drawText("Player one won", 0, -20);
			break;
		case 1:
			g.setLineColor(Color.BLUE);
			g.drawText("Player two won", 0, -20);
			break;
		case 2:
			g.setLineColor(Color.BLACK);
			g.drawText("Loosers", 0, -20);
			break;
		}
	}

	@Override
	public void update(Game game) {
		if (game.isKeyDown(KeyEvent.VK_F1)) {
			game.clearAll();
			game.addSprite(new Intro());
		}
	}
}
