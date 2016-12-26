package examples;

import static eos.Farbe.*;
import static eos.Funktionen.*;
import static eos.Linienart.*;
import eos.Kreis;
import eos.Linie;
import eos.SchrittUhr;

/**
 * @author Peter Schneider
 *
 * Ein Pendel wird simuliert.
 */
public class Pendel {

    public static void main(String[] args) {
        Pendel pendel = new Pendel();
        for (int i = 0; i < 1000; i++) {
            pendel.schritt();
        }
    }
    /* Ausgrund des Simulationsintervalls ist
     * v in der Einheit [0,1 m/s]
     * g in der Einheit [0,1 m/s^2]
     */
    double x;   /// Aufhängung x
    double y;   /// Aufhängung y
    double g;   /// Gravitation
    double D_m; /// D/m
    double vx;  /// Geschwindigkeit des Gewichts x
    double vy;  /// Geschwindigkeit des Gewichts y
    double n;   /// Federlänge neutral
    Kreis gewicht;
    Linie feder;
    SchrittUhr uhr;

    public Pendel() {
        this(20, -70, 0.007, -0.1);
    }

    public Pendel(double x, double y, double D_m, double g) {
        gewicht = new Kreis();
        feder = new Linie();
        feder.linienartSetzen(gestrichelt);
        feder.linienStaerkeSetzen(2f);
        gewicht.fuellfarbeSetzen(grau);
        gewicht.verschiebenNach(x, y);
        this.x = 0;
        this.y = 30;
        this.D_m = D_m;
        this.g = g;
        vx = 0;
        vy = 0;
        n = 30;
        uhr = new SchrittUhr(10);
        federKorrigieren();
    }

    private void federKorrigieren() {
        feder.endpunkteSetzen(gewicht.mittexLesen(), gewicht.mitteyLesen(), x, y);
    }

    private void schritt() {
        double dx = gewicht.mittexLesen() - x;
        double dy = gewicht.mitteyLesen() - y;
        double l = r(dx, dy) - n;
        double alpha = phi(dx, dy);
        double F = l * D_m;
        vx = vx - cos(alpha) * F;
        vy = vy - sin(alpha) * F + g;
        gewicht.verschieben(vx, vy);
        federKorrigieren();
        uhr.weiter();
    }
}
