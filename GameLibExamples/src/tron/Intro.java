package tron;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

import de.lathanda.eos.base.Alignment;
import de.lathanda.eos.base.Picture;
import de.lathanda.eos.game.Game;
import de.lathanda.eos.game.Sprite;

public class Intro extends Sprite {
	static final Font TITLE = new Font(Font.SANS_SERIF, Font.BOLD, 24);
	static final Font SUB_TITLE = new Font(Font.SERIF, Font.BOLD, 16);
	boolean rdyP1 = false;
	boolean rdyP2 = false;

	@Override
	public void render(Picture g) {
		g.setLineColor(Color.BLACK);
		g.setTextAlignment(Alignment.CENTER, Alignment.CENTER);
		g.setFont(TITLE);
		g.drawText("TRON", 0, 20);
		g.setFont(SUB_TITLE);
		if (rdyP1) {
			g.drawText("Player 1 ready, (asdw)", 0, 10);
		} else {
			g.drawText("Player 1 press A to start, (asdw)", 0, 10);
		}
		if (rdyP2) {
			g.drawText("Player 2 ready, (arrow keys)", 0, -10);
		} else {
			g.drawText("Player 2 press <- to start, (arrow keys)", 0, -10);
		}
	}

	@Override
	public void update(Game game) {
		if (game.isKeyPressed(KeyEvent.VK_A)) {
			rdyP1 = true;
		}
		if (game.isKeyPressed(KeyEvent.VK_LEFT)) {
			rdyP2 = true;
		}
		if (rdyP1 && rdyP2) {
			game.clearAll();
			game.addSprite(new Level());
		}
	}
}
