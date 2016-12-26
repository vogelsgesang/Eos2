package examples;

import eos.Roboter;
import eos.SchrittUhr;
import eos.Welt;

public class RoboterSammler {
	public static void main(String[] args) {
		Welt welt = new Welt();
		welt.ziegelVerstreuen(1, 50, 50, 1, 0.2);
		Roboter marry = new Roboter();
		SchrittUhr uhr = new SchrittUhr(200);
		welt.betreten(marry);
		for(int y = 0; y < 51; y++) {
			for(int x = 0; x < 51; x++) {
				if (marry.istStein()) {
					marry.aufheben();
				}
				marry.schritt();
				uhr.weiter();
			}
			if (marry.istOsten()) {
				marry.linksdrehen();
				marry.schritt();
				marry.linksdrehen();
			} else {
				marry.rechtsdrehen();
				marry.schritt();
				marry.rechtsdrehen();				
			}
			uhr.weiter();
		}				
	}
}
