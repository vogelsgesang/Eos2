package de.lathanda.eos.interpreter.commands;

import de.lathanda.eos.common.interpreter.Marker;
import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.Machine;

/**
 * 
 * Programmende.
 * Dieser Befehl beendet das Programm.
 * 
 *
 * @author Peter (Lathanda) Schneider
 */
public class Stoppoint extends Command {
    private final Marker marker;

    public Stoppoint(Marker marker) {
        this.marker = marker;
    }

    @Override
    public boolean execute(Machine m) throws Exception {
        m.debugStop(marker);
       	m.stop();
        return true;
    }

	@Override
    public String toString() {
        return "Stop{" + marker + '}';
    }
	public Marker getMarker() {
		return marker;
	}
}
