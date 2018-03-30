package de.lathanda.eos;

import java.util.LinkedList;

import de.lathanda.eos.base.event.CleanupListener;
import de.lathanda.eos.common.gui.GuiConfiguration;
/**
 * \brief Aufräumklasse
 * 
 * Räumt hinter der Anwendung auf.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Stop extends Thread {
	private static final LinkedList<CleanupListener> toClean = new LinkedList<>();
	/**
	 * Speichert veränderte Daten bevor das Programm schließt. 
	 */
	@Override
	public void run() {
		synchronized (toClean) {
			for(CleanupListener cl : toClean) {
				cl.terminate();
			}
		}
		try {			
			GuiConfiguration.def.cleanup();
		} catch (Throwable t) {}
	}
	public static void addCleanupListener(CleanupListener cl) {
		synchronized (toClean) {
			toClean.add(cl);
		}
	}
	public static void removeCleanupListener(CleanupListener cl) {
		synchronized (toClean) {
			toClean.remove(cl);
		}
	}

}
