package tron;
import de.lathanda.eos.game.Game;

public class Tron {
	public static void main(String[] args) {
		Game game = new Game(300, 200, "Tron");
		game.addSprite(new Intro());
	}
}
