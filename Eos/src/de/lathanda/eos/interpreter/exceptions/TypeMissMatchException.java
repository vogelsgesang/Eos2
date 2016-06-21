package de.lathanda.eos.interpreter.exceptions;

import de.lathanda.eos.gui.Messages;

/**
 * Der übergebene Typ ist mit dem erwarteten Typ nicht kompatibel.
 * Dieser Fehler kann eigentlich nur auftreten, wenn der Compiler bei 
 * der Typprüfung einen Fehler macht oder die Konfiguration falsch ist.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.2
 */
public class TypeMissMatchException extends RuntimeException {
    private static final long serialVersionUID = -1331782960608862330L;
    Class<?> expected;
    Class<?> found;
    public TypeMissMatchException(Class<?> expected, Class<?> found) {
        this.expected = expected;
        this.found = found;
    }
    @Override
    public String getLocalizedMessage() {
        return Messages.formatError("InvalidTypeCast", found, expected);
    }      
}
