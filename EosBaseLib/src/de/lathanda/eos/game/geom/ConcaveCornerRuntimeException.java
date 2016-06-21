package de.lathanda.eos.game.geom;

import de.lathanda.eos.base.math.Point;
import java.text.MessageFormat;

/**
 * \brief Korruptes Polygon
 *
 * Diese Ausnahme wird erzeugt, wenn versucht wird ein Polygon zu erzeugen, dass
 * entweder nicht konvex ist oder die Eckenreihenfolge falsch herum ist.
 *
 * @author Lathanda
 */
public class ConcaveCornerRuntimeException extends RuntimeException {

    /**
     * \brief Serialisierungs UID
     */
    private static final long serialVersionUID = 26094915854422703L;
    /**
     * \brief Index der Kante die fehlerhaft ist
     */
    private int i;
    /**
     * \brief Erste Ecke des Fehlers
     */
    Point pA;
    /**
     * \brief Zweite Ecke des Fehlers
     */
    Point pB;
    /**
     * \brief Dritte Ecke des Fehlers
     */
    Point pC;

    /**
     * Erzeugt die Ausnahme
     *
     * @param i Index der Fehlerhaften Ecke
     * @param pA Erste Ecke
     * @param pB Zweite Ecke
     * @param pC Dritte Ecke
     */
    public ConcaveCornerRuntimeException(int i, Point pA, Point pB, Point pC) {
        this.i = i;
        this.pA = pA;
        this.pB = pB;
        this.pC = pC;
    }

    /**
     * Zeichenkette für Fehlersuche
     *
     * @return Lesbare Fehlerinformation
     */
    @Override
    public String toString() {
        return MessageFormat.format(
                "concave edge found at index {0}, with points {1}, {2}, {3}",
                new Object[]{i, pA, pB, pC});
    }
}
