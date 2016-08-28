package de.lathanda.eos.interpreter.exceptions;

import de.lathanda.eos.common.gui.Messages;

/**
 * Dieser Fehler wird ausgelöst, wenn auf eine Variable zugegriffen wird,
 * bevor sie einen Wert hat.
 * Dies ist nur bei abstrakten Typen möglich.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.9.5
 */
public class NullAccessException extends RuntimeException {
	private static final long serialVersionUID = -5428419995880814279L;
    public NullAccessException() {
        super();
    }
    @Override
    public String getLocalizedMessage() {
        return Messages.getError("AccessNull");
    }    
}
