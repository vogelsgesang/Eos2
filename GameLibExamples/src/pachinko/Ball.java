package pachinko;

import java.awt.Color;

import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.math.Vector;
import de.lathanda.eos.game.Game;
import de.lathanda.eos.game.Sprite;
import de.lathanda.eos.game.geom.Circle;


public class Ball extends Sprite {
	private double dx;
	private double dy;
	private Spieler spieler;
	public Ball(double x, double y, Spieler spieler) {
		this.spieler = spieler;
		shape = new Circle(2, x, y);
		dx = 0;
		dy = 0;
	}

	@Override
	public void render(Picture p) {
		p.setFillColor(Color.BLACK);
		p.drawShape(shape);
	}

	@Override
	public void update(Game game) {
		dy = dy - 0.04;
		shape.move(dx, dy);
		if ( shape.getY() < -110) {
			spieler.ballRaus();
			game.removeSprite(this);
		}
	}
	@Override
	public boolean processCollision(Sprite b, Game game) {
		if (b instanceof Ball) {
			Ball ball = (Ball)b;
			Vector abstand = new Vector(shape.getCenter(), ball.shape.getCenter());
			Vector diffbewegung = new Vector(ball.dx - this.dx, ball.dy - this.dy);
			//Testen ob die Kollision real ist, oder nur ein Simulationsfehler
			if (diffbewegung.getProjectionLength(abstand) > 0) {
				// Ball a bewegt sich nicht auf die Kollisionsstelle zu =>
				// Fehler durch Methode der kleinen Schritte
				return true;
			}
			// Impulszerlegung
			// L Kollisionsrichtung H Senkrecht zur Kollisionsrichtung
			Vector v1 = new Vector(this.dx, this.dy);
			Vector v2 = new Vector(ball.dx, ball.dy);
			Vector aH = v1.getPerpendicular(abstand);
			Vector bH = v2.getPerpendicular(abstand);
			Vector aL = v1.getProjection(abstand);
			Vector bL = v2.getProjection(abstand);
			// Impulsaddition
			v1 = aH.add(bL);
			v2 = bH.add(aL);
			dx = v1.getdx();
			dy = v1.getdy();
			ball.dx = v2.getdx(); //dx ist zwar privat, aber wir befinden und ja auch in der Klasse Ball, dass es ein anderes Objekt ist spielt für den Zugriff keine Rolle
			ball.dy = v2.getdy();
			return true; //beide Bälle sind versorgt daher darf der andere auf keinen Fall mehr aufgerufen werden
		}
		if (b instanceof Rand) {
			Rand rand = (Rand)b;
			Vector v = new Vector(this.dx, this.dy);
			Vector abstandsRichtung = rand.getLot();
			if(v.getProjectionLength(abstandsRichtung) < 0) {
				//falsche Bewegungsrichtung
				return false;
			}
			Vector aH = v.getPerpendicular(abstandsRichtung);
			Vector aL = v.getProjection(abstandsRichtung);
			v = aH.substract(aL.multiply(0.7)); // 0.7 sorgt für Dämpfung, nur 70% des Impulses werden reflektiert
			dx = v.getdx();
			dy = v.getdy();
			return false;
		}
		if (b instanceof Pin) {
			Pin pin = (Pin)b;
			Vector v = new Vector(this.dx, this.dy);
			Vector abstandsRichtung = new Vector(pin.getX()-shape.getX(), pin.getY()- shape.getY());
			if(v.getProjectionLength(abstandsRichtung) < 0) {
				//falsche Bewegungsrichtung
				return false;
			}
			Vector aH = v.getPerpendicular(abstandsRichtung);
			Vector aL = v.getProjection(abstandsRichtung);
			v = aH.substract(aL.multiply(0.7)); // 0.7 sorgt für Dämpfung, nur 70% des Impulses werden reflektiert
			dx = v.getdx();
			dy = v.getdy();
			return false;
		}
		
		return false;
	}
}
