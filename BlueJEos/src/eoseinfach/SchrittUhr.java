package eoseinfach;

/**
 * @author Peter Schneider
 * 
 * Diese Klasse dient als Helfer für die Methode der kleinen Schritte (Physikunterricht).
 * 
 * Man legt im Konstruktor
 * das &Delta;t für einen Schritt fest.
 * Immer wenn man nun die Methode weiter() aufruft, wird die Ausführung des Programms
 * unterbrochen bis die Zeit, die für einen Schritt festgelegt wurde, vergangen ist.
 * Hierbei wird die Zeit, die für die Berechnung des Schrittes notwendig war, herausgerechnet.
 * Die Methode fertig() hält die Uhr an.
 */
public class SchrittUhr {
    private static final long UNDEFINIERT = 0;
    long naechsterSchritt = UNDEFINIERT;
    long dt;
    /**
     * Erzeugt eine Schrittuhr, welc
     * @param dt Zeitintervall eines Schrittes in ms.
     */
    public SchrittUhr(long dt) {
        this.dt = dt;
    }
    public void weiter() {
        if (naechsterSchritt == UNDEFINIERT) {
            naechsterSchritt = System.currentTimeMillis() + dt;
        }
        try {
            long wartezeit = naechsterSchritt - System.currentTimeMillis();
            if (wartezeit > 0) {
                Thread.sleep(wartezeit);
            }
        } catch (InterruptedException ie) {
            System.err.println(ie.getLocalizedMessage());
        }
        //Wir gehen von der aktuellen Zeit aus, um einen Warp nach einer unterbrechung
        //zu vermeiden.
        naechsterSchritt = System.currentTimeMillis() + dt;
    }
}
