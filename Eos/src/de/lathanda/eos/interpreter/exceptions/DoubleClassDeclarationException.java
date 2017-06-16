package de.lathanda.eos.interpreter.exceptions;

import de.lathanda.eos.common.gui.Messages;

/**
 * Die Klasse existiert bereits
 *
 * @author Peter (Lathanda) Schneider
 */
public class DoubleClassDeclarationException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8821078166258701578L;
	private final String cls;
    public DoubleClassDeclarationException(String cls) {
        this.cls = cls;
    }
    @Override
    public String getLocalizedMessage() {
        return Messages.formatError("DoubleClassDef", cls);
    }      
}
