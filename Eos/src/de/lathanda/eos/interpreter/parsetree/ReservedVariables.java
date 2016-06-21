package de.lathanda.eos.interpreter.parsetree;

/**
 * Reservierte Variablennamen.
 * Alle Reservierten Variablennamen starten mit einer Nummer um Konflikte
 * mit benutzerdefinierten Variablen zu vermeiden.
 * 
 * @author Peter (Lathanda) Schneider
 * @since 0.6
 */
public interface ReservedVariables {
    /**
     * Lokale Variable für das Ergebnis einer Methode
     */
    String RESULT = "1x";
    /**
     * Globale Variable für das automatisch erzeugte Fenster.
     * Diese Variable kann von Methoden, aber nicht von Anweisungen, verwendet werden.
     */
    String WINDOW = "1f";
    /**
     * Präfix für alle Zählschleifen Indizes. Das Präfix wird durch eine
     * Programmweit eindeutige Nummer ergänzt.
     * 1rt1, 1rt2 ....
     */
    String REPEAT_TIMES_INDEX = "1rt";
}
