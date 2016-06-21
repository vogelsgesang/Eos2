package de.lathanda.eos.game.geom;

/**
 * \brief Kollision
 *
 * Objekte dieser Klasse dienen als temporärer Speicher für potenielle
 * Kollisionen innerhalb der Kollisionsberechnung.
 *
 * @author Lathanda
 * @version 2.0.3
 *
 */
class Collision implements Comparable<Collision> {

    /**
     * \brief Erster Umriss (kleinere ID)
     */
    public final Shape a;
    /**
     * \brief Zweiter Umriss (größere ID)
     */
    public final Shape b;

    /**
     * Neue Kollision zwischen Umriss a und Umriss b. Die Attribute werden nach
     * ihrer ID sortiert.
     *
     * @param a Erster Umriss
     *
     * @param b Zweiter Umriss
     */
    public Collision(Shape a, Shape b) {
        if (a.id < b.id) {
            this.a = a;
            this.b = b;
        } else {
            this.a = b;
            this.b = a;
        }
    }

    /**
     * Definiert eine Ordnung für die Speicherung in Bäumen.
     *
     * @param o Zweite Collision
     */
    @Override
    public int compareTo(Collision o) {
        int result = a.id - o.a.id;
        if (result == 0) {
            result = b.id - o.b.id;
        }
        return result;
    }

    /**
     * Ausgabe für Fehlersuche
     *
     * @return (a,b)
     */
    @Override
    public String toString() {
        return "(" + a + "," + b + ")";
    }

    /**
     * Die Methode überprüft ob zwei Umrisse wirklich überlappen. Dies ist die
     * zweite Stufe der Kollisionsberechnung. In der Ersten Stufe wird nur
     * geschätzt.
     *
     * @return Wahr, wenn die beiden Umrisse gemeinsame Punkte haben
     */
    public boolean verifyCollision() {
        return Intersection.intersects(a, b);
    }
}
