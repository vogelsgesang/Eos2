package de.lathanda.eos.base;

import java.util.LinkedList;

/**
 * Diese Schnittstelle erlaubt den lesenden Zugriff auf Attribute zu Debuggingzwecken
 * und für Objektdiagramme  
 *
 * @author Peter (Lathanda) Schneider
 */
public interface Readout { 
	/**
	 * Der Aufruf befüllt die Map mit Attributen.
	 * Welche dies sind entscheidet jede Klasse selbst,
	 * es gibt keinerlei garantie dass die Liste vollständig ist.
	 * Etwa werden nicht technische und interne Attribute in der Regel weggelassen.
	 * 
	 * @param attributes Datenspeicher für die Attibute Name -> Wert
	 */
	void getAttributes(LinkedList<Attribut> attributes);
	public class Attribut {
		public final String name;
		public final Object value;
		public Attribut(String name, Object value) {
			super();
			this.name = name;
			this.value = value;
		}
	}
	default boolean translate() {
		return true;
	}
}
