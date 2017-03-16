package de.lathanda.eos.interpreter.parsetree;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import de.lathanda.eos.common.interpreter.ErrorInformation;
import de.lathanda.eos.common.interpreter.Marker;

/**
 * Übersetzungsfehler.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.5
 */
public class CompilerError implements ErrorInformation {
    private static final ResourceBundle error = ResourceBundle.getBundle("text.error");
    private Marker code;
    private String message;
    public CompilerError(String errorId, Object ... data) {    
        this.code = null;
        this.message = MessageFormat.format(error.getString(errorId), data);
    }
    public CompilerError(Marker code, String message) {
        this.code = code;
        this.message = message;
    }
    public CompilerError(Marker code, Exception e) {
        this.code = code;
        this.message = MessageFormat.format(error.getString("Exception"), e.getLocalizedMessage());
    }
    public CompilerError(Marker code, String errorId, Object ... data) {    
        this.code = code;
        this.message = MessageFormat.format(error.getString(errorId), data);
    }

    public Marker getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Error{" + "code=" + code + ", message=" + message + '}';
    }
}
