package de.lathanda.eos.common.interpreter;

import java.util.LinkedList;

import de.lathanda.eos.gui.diagram.MemoryEntry;
/**
 * Schnittstelle für Interpreter.
 * @author Peter (Lathanda) Schneider
 *
 */
public interface AbstractMachine {
	/**
	 * Programm laufen lassen.
	 */
	void run();
	/**
	 * Programm mit maximaler Geschwindigkeit ausführen.
	 */
	void skip();
	/**
	 * Einzelnen Programmschritt ausführen und danach warten.
	 */
	void singleStep();
	/**
	 * Programm unterbrechen.
	 */
	void pause();
	/**
	 * Programm abbrechen.
	 */
	void stop();
	/**
	 * Verzögerung setzen.
	 * @param delay
	 */
	void setDelay(long delay);
	/**
	 * Neu initialisieren.
	 */
	void reinit();
	/**
	 * Debuglistener hinzufügen.
	 * @param debugListener
	 */
	void addDebugListener(DebugListener debugListener);
	/**
	 * Debuglistener entfernen.
	 * @param debugListener
	 */
	void removeDebugListener(DebugListener debugListener);
	/**
	 * Debugging Informationen abfragen.
	 * @return
	 */
	DebugInfo getDebugInfo();
	/**
	 * Breakpoint setzen.
	 * @param linenumber
	 * @param b
	 */
	void setBreakpoint(int linenumber, boolean b);
	/**
	 * Liefert die Position des Breakpoints für diese Zeile innerhalb des Soucecodes.
	 * @param linenumber
	 * @return
	 */
	int getBreakpointPosition(int linenumber);
	/**
	 * Speicher für Visualisierung auslesen.
	 * @return
	 */
	LinkedList<MemoryEntry> getMemory();
}