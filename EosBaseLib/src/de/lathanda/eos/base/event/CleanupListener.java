package de.lathanda.eos.base.event;
/**
 * Klassen die diese Schnittstelle implementieren
 * müssen beim Ende des Programms noch Dinge erledigen.
 * z.B. nicht GC Speicher freigeben, Fenster schliessen etc. 
 *
 * @author Peter (Lathanda) Schneider
 */

public interface CleanupListener {
	/**
	 * Aufräumarbeiten am Ende der Ausführung.
	 */
	void terminate();
}
