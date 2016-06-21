package pachinko;
import de.lathanda.eos.game.Game;


public class Pachinko {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Game game = new Game(200,200,"Pachinko");
		for(double x = -90; x < 100; x += 20) {
			for(double y = 80; y > -80; y -= 20) {
				game.addSprite(new Pin(x, y));
			}
		}
		for(double x = -80; x < 100; x += 20) {
			for(double y = 70; y > -80; y -= 20) {
				game.addSprite(new Pin(x, y));
			}
		}
		Spieler spieler = new Spieler();
		game.addSprite(spieler);
		for(int i = 0; i < 9; i++) {
			game.addSprite(new Korb(spieler, -85 + 20*i, -90, -75 + 20*i, -80, 50*(i-4)*(i-4)+100));
		}
		game.addSprite(new Rand(100,100,100,-100));
		game.addSprite(new Rand(100, -100, -100, -100));
		game.addSprite(new Rand(-100, -100, -100, 100));
	}

}
