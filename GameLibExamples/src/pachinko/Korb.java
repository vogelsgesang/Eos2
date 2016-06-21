package pachinko;

import java.awt.Color;

import de.lathanda.eos.base.Alignment;
import de.lathanda.eos.game.Game;
import de.lathanda.eos.game.Sprite;
import de.lathanda.eos.game.geom.Rectangle;


public class Korb extends Sprite {
	Spieler spieler;
	int punkte;
	public Korb(Spieler spieler, double x1, double y1, double x2, double y2, int punkte) {
		this.spieler = spieler;
		shape = new Rectangle(x1, y1, x2-x1, y2-y1);
		this.punkte = punkte;
	}

	@Override
	public void render(de.lathanda.eos.base.Picture p) {
		p.setFillColor(Color.GREEN);
		p.drawShape(shape);
		p.setLineColor(Color.BLUE);
		p.setTextAlignment(Alignment.CENTER, Alignment.CENTER);
		p.drawString(""+punkte, shape);
	}

	@Override
	public boolean processCollision(Sprite b, Game game) {
		if (b instanceof Ball) {
			Ball ball = (Ball)b;
			game.removeSprite(ball);
			spieler.ballRaus();
			spieler.punkteGeben(punkte);
		} 
		return false;
	}

}
