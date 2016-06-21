package examples;

import eos.Fenster;
import eos.Kreis;
import eos.SchrittUhr;
import static eos.Funktionen.*;

/**
 * @author Peter Schneider
 *
 * Ein Springender Ball wird simuliert
 */
public class SpringenderBall {

    public static void main(String[] args) {
        SpringenderBall ball = new SpringenderBall();
        for (int i = 0; i < 1000; i++) {
            ball.schritt();
        }
    }

    SchrittUhr uhr;
    Kreis k;
    Fenster f;
    /* Ausgrund des Simulationsintervalls ist
     * v in der Einheit [0,1 m/s]
     * g in der Einheit [0,1 m/s^2]
     */
    double g;
    double vx;
    double vy;

    public SpringenderBall() {
        this(0, 80, -0.1);
    }

    public SpringenderBall(double x, double y, double g) {
        f = new Fenster();
        k = new Kreis();
        f.zeichne(k);
        uhr = new SchrittUhr(10);
        k.verschiebenNach(x, y);
        this.g = g;
        vx = 1;
        vy = 0;
    }

    public void schritt() {
        k.verschieben(vx, vy);
        if (k.mittexLesen() > 90) {
            vx = -abs(vx);
        }
        if (k.mittexLesen() < -90) {
            vx = abs(vx);
        }
        if (k.mitteyLesen() < -90) {
            vy = abs(vy);
        } else {
            //Eigentlich müsse man die folgende Zeile immer machen
            //jedoch hält der Ball sich dann nicht an den Energieerhaltungssatz,
            //da er bei der Reflektion Energie gewinnt oder verliert.
            //Je nachdem, ob man vor der Reflektion beschleunigt oder danach.
        	//Er müsste ja vor den Reflektion beschleunigen und danach bremsen.
            vy = vy + g;
        }
        uhr.weiter();
    }
}
