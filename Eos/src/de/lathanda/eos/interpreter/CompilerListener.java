package de.lathanda.eos.interpreter;

import java.util.LinkedList;


/**
 * Kommunikationsschnittstelle vom Kompiler zur Anzeige.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.5
 */
public interface CompilerListener {
    void compileComplete(Machine machine, LinkedList<CompilerError> errors, Program program);    
}
