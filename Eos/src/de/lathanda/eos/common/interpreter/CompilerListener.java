package de.lathanda.eos.common.interpreter;

import java.util.LinkedList;


/**
 * Kommunikationsschnittstelle vom Kompiler zur Anzeige.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.5
 */
public interface CompilerListener {
    void compileComplete(LinkedList<ErrorInformation> errors, AbstractProgram program);    
}
