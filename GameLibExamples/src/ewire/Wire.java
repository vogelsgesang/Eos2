package ewire;

import java.awt.Color;

import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.game.Sprite;
import de.lathanda.eos.game.geom.Rectangle;

public class Wire extends Sprite {

	public Wire(int x1, int y1, int x2, int y2) {
		shape = new Rectangle(new Point(x1, y1), new Point(x2, y2), 4);
	}

	@Override
	public void render(Picture g) {
		g.setFillColor(Color.BLUE);
		g.drawShape(shape);
	}
}
