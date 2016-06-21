package de.lathanda.eos.interpreter.commands;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.Machine;

/**
 * 
 * Assembler Befehl: negativen Wert berechnen.
 *
 * @author Peter (Lathanda) Schneider
 */public class Negate extends Command {

    @Override
    public boolean execute(Machine m) throws Exception {
        Object a = m.pop();
        if (a instanceof Integer) {
            m.push(-((Number)a).intValue());
        } else {
            m.push(-((Number)a).doubleValue());            
        }
        return true;
    }

    @Override
    public String toString() {
        return "Negate{" + '}';
    }
}
