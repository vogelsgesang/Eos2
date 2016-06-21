package ewire;

import de.lathanda.eos.game.Game;

public class EWire {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Game game = new Game(200, 200, "E Wire");
		game.addSprite(new Paddle(0,0,0));
		game.addSprite(new Wire(-50,-50, 90, 90));
	}

}
