package de.lathanda.eos.interpreter.commands;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.Machine;

/**
 * 
 * Assembler Befehl: Double Division berechnen.
 *
 * @author Peter (Lathanda) Schneider
 */public class DivideD extends Command {

    @Override
    public boolean execute(Machine m) throws Exception {
        Object b = m.pop();
        Object a = m.pop();
        m.push(((Number)a).doubleValue() / ((Number)b).doubleValue());            
        return true;
    }

    @Override
    public String toString() {
        return "Divide{" + '}';
    }
}
