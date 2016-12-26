package examples;

import java.awt.Color;
import static eos.Funktionen.zufall;
import eos.Gruppe;
import eos.Kreis;
import eos.SchrittUhr;

public class Sterne {
	public static void main(String[] args) {
		Gruppe sterne = new Gruppe();
		Kreis stern = new Kreis();
		SchrittUhr uhr = new SchrittUhr(10);
		stern.fuellfarbeSetzen(Color.YELLOW);
		stern.radiusSetzen(1);
		sterne.zentrumSetzen(0, 0);
		while (true) {
			for(int i = 20; i --> 0; ) {
				stern.mittexSetzen(zufall(-200, 200));
				stern.mitteySetzen(zufall(-200, 200));
				sterne.kopiere(stern);
			}
			sterne.strecken(1.2);
			uhr.weiter();
		}	
	}
}
