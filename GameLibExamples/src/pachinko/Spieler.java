package pachinko;

import java.awt.Color;
import java.awt.event.KeyEvent;

import de.lathanda.eos.base.Alignment;
import de.lathanda.eos.game.Game;
import de.lathanda.eos.game.Sprite;


public class Spieler extends Sprite {
	private int punkte;
	private int zustand;
	private int anzahlGelandet;
	private int anzahlErzeugt;
	private int pause; //Damit Bälle einzeln brauchen wir einen Pausenzähler (man kann im Entwurf nicht alles erahnen :-) )
	public Spieler() {
		super(-1);
		pause = 0;
		punkte = 0;
		zustand = 0;
		anzahlGelandet = 0;
		anzahlErzeugt = 0;
	}
	@Override
	public void render(de.lathanda.eos.base.Picture p) {	
		p.setLineColor(Color.BLACK);
		p.setTextAlignment(Alignment.TOP, Alignment.LEFT);
		p.drawText("Punkte: " + punkte, -100, 100);
		p.setTextAlignment(Alignment.TOP, Alignment.CENTER);
		p.drawText("Kugeln: " + (30 - anzahlErzeugt), 0, 100);
		p.setTextAlignment(Alignment.CENTER, Alignment.CENTER);
		switch(zustand) {
		case 0:
			p.setFillColor(Color.WHITE);
			p.setLineColor(Color.BLACK);
			p.drawRect(-25, -10, 50, 20);
			p.drawText("F1 drücken für start", 0, 0);
			break;
		case 1:
			break;
		case 2:
			p.setFillColor(Color.WHITE);
			p.setLineColor(Color.BLACK);
			p.drawRect(-35, -20, 70, 30);
			p.drawText("F1 drücken für neue Runde!", 0, 0);
			p.drawText("Erreichte Punktezahl:" + punkte, 0, -10);
			break;
		}

	}

	@Override
	public void update(Game game) {
		if (game.isKeyPressed(KeyEvent.VK_F1)) {
			F1(); //auslösende Aktion für Automaten
		}
		
		switch(zustand) { //zustandsabhängiges Verhalten (ohne Zustandsübergänge)
		case 0:
			break;
		case 1:
			pause--;
			if (game.isMouseDown(1) & pause < 1) {
				if (anzahlErzeugt < 30) {
					ballSetzen(game.getMouse().getX(), 90, game); //y fest auf 90 da man sonst betrügen kann, alternativ wäre Bereich
				}
			}
			break;
		case 2:
			break;
		}
	}
	public void ballSetzen(double x, double y, Game game) {
		anzahlErzeugt++;
		pause = 15;
		game.addSprite(new Ball(x, y, this));
	}
	public void ballRaus() {
		anzahlGelandet++;
		if ( anzahlGelandet >= 30) {
			zustand = 2;
		}
	}
	public void punkteGeben(int p) {		
		punkte += p;
	}
	public void F1() {
		switch (zustand) {
		case 0:
			zustand = 1;
			break;
		case 1:
			break;
		case 2:
			punkte = 0;
			anzahlErzeugt = 0;
			anzahlGelandet = 0;
			zustand = 1;
			break;
		}
	}
}
