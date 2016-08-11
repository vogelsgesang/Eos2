package de.lathanda.eos.common;

/**
 * Schnittstelle für Klasse die informiert werden wollen, wenn Debugpunkt erreicht wurde.
 * Dies entspricht dem Abschluss eines Befehls.
 * Wird immer aufgerufen auch wenn das Programm nicht anhält.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.5
 */
public interface DebugListener {
    void debugPointReached(DebugInfo debugInfo);
}
