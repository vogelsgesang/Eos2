package pachinko;

import java.awt.Color;

import de.lathanda.eos.base.Picture;
import de.lathanda.eos.game.Sprite;
import de.lathanda.eos.game.geom.Circle;


public class Pin extends Sprite {
	public Pin( double x, double y) {
		shape = new Circle(3, x, y);
	}
	@Override
	public void render(Picture p) {
		p.setFillColor(Color.DARK_GRAY);
		p.drawShape(shape);
	}

	public double getX() {
		return shape.getX();
		
	}
	public double getY() {
		return shape.getY();	
	}
}
