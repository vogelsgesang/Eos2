package billard;

import java.awt.Color;

import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.math.Vector;
import de.lathanda.eos.game.Sprite;
import de.lathanda.eos.game.geom.Rectangle;

public class Border extends Sprite {
	Vector perpendicular;

	public Border(int x, int y, int w, int h, Vector perpendicular) {
		shape = new Rectangle(x, y, w, h);
		this.perpendicular = perpendicular;
	}

	@Override
	public void render(Picture g) {
		g.setFillColor(Color.BLACK);
		g.drawShape(shape);
	}
}
