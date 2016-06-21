package de.lathanda.eos.interpreter.exceptions;

import de.lathanda.eos.gui.Messages;

/**
 * Die Ausführung musste abgebrochen werden, da sich der Interpreter in einem
 * nicht behebbarem Zustand befindet.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.9.5
 */
public class ProgramTerminationException extends RuntimeException {
	private static final long serialVersionUID = 693918237097929645L;
	public ProgramTerminationException() {
        super();
    }
    @Override
    public String getLocalizedMessage() {
        return Messages.formatError("ProgramTermination");
    }    
}
