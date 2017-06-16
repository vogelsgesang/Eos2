package de.lathanda.eos.interpreter.exceptions;

import de.lathanda.eos.common.gui.Messages;

/**
 * Das Attribut existiert bereits.
 *
 * @author Peter (Lathanda) Schneider
 */
public class DoublePropertyDeclarationException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8475409666820140095L;
	private final String cls;
	private final String property;
    public DoublePropertyDeclarationException(String property, String cls) {
        this.cls = cls;
        this.property = property;
    }
    @Override
    public String getLocalizedMessage() {
        return Messages.formatError("DoublePropertyDef", property, cls);
    }      
}
