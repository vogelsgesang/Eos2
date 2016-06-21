package billard;

import java.awt.Color;

import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.base.math.Vector;
import de.lathanda.eos.game.Game;

public class WhiteBall extends Ball {
	Point loc;
	int y;
	boolean drawLine = false;

	public WhiteBall(int x, int y) {
		super(x, y, Color.WHITE);
	}

	public void render(Picture g) {
		super.render(g);
		if (drawLine) {
			g.setLineColor(Color.BLUE);
			g.setLineWidth(1);
			g.drawLine(loc, shape.getCenter());
		}
	}

	public void update(Game game) {
		if (!moving) {
			loc = game.getMouse();
			if (game.isMouseDown(1)) {
				// shot
				v = new Vector(shape.getCenter(), loc).multiply(0.02);
			}
			drawLine = true;
		} else {
			drawLine = false;
		}
		moving = false;
		super.update(game);
	}
}
