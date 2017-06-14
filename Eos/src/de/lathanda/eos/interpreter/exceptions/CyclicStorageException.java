package de.lathanda.eos.interpreter.exceptions;

import de.lathanda.eos.common.gui.Messages;

/**
 * Es wurde versucht eine Klasse zu definieren die sich selbst enthält.
 *
 * @author Peter (Lathanda) Schneider

 */
public class CyclicStorageException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5295897311459322613L;
	private final String cls;
    public CyclicStorageException(String cls) {
        super(cls);
        this.cls = cls;
    }
    @Override
    public String getLocalizedMessage() {
        return Messages.formatError("CyclicStorage", cls);
    }    
}
