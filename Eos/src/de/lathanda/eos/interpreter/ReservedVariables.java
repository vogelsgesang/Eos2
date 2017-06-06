package de.lathanda.eos.interpreter;

/**
 * Reservierte Variablennamen.
 * Alle Reservierten Variablennamen starten mit einer Nummer um Konflikte
 * mit benutzerdefinierten Variablen zu vermeiden.
 * 
 * @author Peter (Lathanda) Schneider
 */
public interface ReservedVariables {
    /**
     * Lokale Variable für das Ergebnis einer Methode
     */
    String RESULT = "1x";
    /**
     * Lokale Variable für das eigene Objekt
     */
    String SELF= "1s";
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
    /**
     * Name der Konstruktor Prozedur.
     */
    String INIT_PROCEDURE = "1new";
    /**
     * user property set prefix
     */
    String SET_PREFIX = "1set";
    /**
     * user property get prefix
     */
    String GET_PREFIX = "1get";
}
