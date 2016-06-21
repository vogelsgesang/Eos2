package de.lathanda.eos.interpreter;

/**
 * Basisklasse aller Kommandos der Virtuellen Maschine.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.2
 */
public abstract class Command {
    public abstract boolean execute(Machine m) throws Exception;  
    public void prepare(Machine m) {}
}
