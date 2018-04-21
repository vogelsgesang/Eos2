package de.lathanda.eos.interpreter.exceptions;

import de.lathanda.eos.common.gui.Messages;

/**
 * Es wurde versucht eine Klasse zu definieren die sich selbst enthält.
 *
 * @author Peter (Lathanda) Schneider

 */
public class MissingParserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5685441006262921286L;
    public MissingParserException() {

    }
    @Override
    public String getLocalizedMessage() {
        return Messages.formatError("MissingParser");
    }    
}
