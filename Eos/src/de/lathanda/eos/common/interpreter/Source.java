package de.lathanda.eos.common.interpreter;

/**
 * Kommunikationsschnittstelle vom Compiler zum Quellcode.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.5
 */
public interface Source {
    String getSourceCode();
	String getPath();
}
