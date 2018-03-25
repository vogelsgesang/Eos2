package examples;

import eos.*;

public class MondLandung {
	public static void main(String[] args) {
		new MondLandung().spiel();
	}
    private Fenster fenster;
    private Rechteck mondfaehre;
    private Rechteck boden;
    private TextFeld text;
    private double hoehe;
    private double v;
    private double treibstoff;
    public MondLandung() {
        fenster = new Fenster();
        boden = new Rechteck();
        boden.breiteSetzen(200);
        boden.hoeheSetzen(200);
        boden.fuellfarbeSetzen(Farbe.braun);
        boden.verschiebenNach(0, -hoehe-100);
        fenster.zeichne(boden);
        mondfaehre = new Rechteck();
        mondfaehre.breiteSetzen(20);
        mondfaehre.hoeheSetzen(10);
        mondfaehre.verschieben(0,5);
        fenster.zeichne(mondfaehre);
        text = new TextFeld();
        text.verschiebenNach(-80, 80);
        fenster.zeichne(text);
        v = 0;
    }
    public void spiel() {
        v = 0;
        hoehe = 4000;
        treibstoff = 200;
        SchrittUhr uhr = new SchrittUhr(100);
        while (hoehe > 0) {
            uhr.weiter();
            if (fenster.mausGedruecktLesen() && treibstoff > 0) {
                v += 0.05;
                treibstoff--;
            } else {
                v -= 0.016;
            }
            hoehe += v;
            text.zeileSetzen(1, "Höhe:"+hoehe);
            text.zeileSetzen(2, "Treibstfoff:"+treibstoff);
            text.zeileSetzen(3, "Geschwindigkeit:"+v);
            boden.verschiebenNach(0, -hoehe-100);
        }
        if (v > -5) {
            text.zeileSetzen(1, "gelandet");
        } else {
            text.zeileSetzen(1, "C R A S H");
        }
    }    
}
