package de.lathanda.eos.interpreter.commands;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.Machine;

/**
 * 
 * Assembler Befehl: Double Hoch rechnen.
 *
 * @author Peter (Lathanda) Schneider
 */public class PowD extends Command {

    @Override
    public boolean execute(Machine m) throws Exception {
        Object b = m.pop();
        Object a = m.pop();
        m.push(Math.pow(((Number)a).doubleValue(), ((Number)b).doubleValue()));            
        return true;
    }

    @Override
    public String toString() {
        return "Multiply{" + '}';
    }
}
