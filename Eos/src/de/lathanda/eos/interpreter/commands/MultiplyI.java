package de.lathanda.eos.interpreter.commands;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.Machine;

/**
 * 
 * Assembler Befehl: Integer Multiplikation berechnen.
 *
 * @author Peter (Lathanda) Schneider
 */public class MultiplyI extends Command {

    @Override
    public boolean execute(Machine m) throws Exception {
        Object b = m.pop();
        Object a = m.pop();
        m.push(((Number)a).intValue() * ((Number)b).intValue());
        return true;
    }

    @Override
    public String toString() {
        return "Multiply{" + '}';
    }
}
