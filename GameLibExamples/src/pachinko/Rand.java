package pachinko;

import java.awt.Color;

import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.base.math.Vector;
import de.lathanda.eos.game.Sprite;
import de.lathanda.eos.game.geom.Rectangle;


public class Rand extends Sprite {
	private double x1;
	private double y1;
	private double x2;
	private double y2;
	public Rand(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		shape = new Rectangle(new Point(x1, y1), new Point(x2, y2), 2);
	}

	@Override
	public void render(Picture p) {
		p.setLineColor(Color.BLACK);
		p.drawLine(x1, y1, x2, y2);
	}

	public Vector getLot() {
		//Lot bedeutet Koordinaten tauschen und eine auf Minus setzen.
		return new Vector(y1 - y2, x2 - x1);
		
	}
}
