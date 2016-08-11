package de.lathanda.eos.common;

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
