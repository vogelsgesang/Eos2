package asteroids;

import java.awt.Color;

import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.game.Game;
import de.lathanda.eos.game.Sprite;


public class Steam extends Sprite {
	int age;
	Point loc;
	public Steam(Point p) {
		age = 100;
		loc = p;
	}
	@Override
	public void render(Picture p) {
		if (age > 50) {
			p.setFillColor(new Color(1f, (age-50f)/50f, 0.5f, 0.5f));
		} else {
			float brightness = age / 100f;
			p.setFillColor(new Color(brightness, brightness, brightness, 0.5f));
		}
		float radius = (150f-age)/15f;
		p.drawEllipse(loc, radius, radius);
	}

	@Override
	public void update(Game game) {
		if(age-- == 0) {
			game.removeSprite(this);
		}
		
	}

}
