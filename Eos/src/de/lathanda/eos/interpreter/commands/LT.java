package de.lathanda.eos.interpreter.commands;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.Machine;

/**
 * 
 * Assembler Befehl: Kleiner als Vergleich.
 *
 * @author Peter (Lathanda) Schneider
 */
public class LT extends Command {

    @Override
    public boolean execute(Machine m) throws Exception {
        Number b = (Number)m.pop();
        Number a = (Number)m.pop();
        m.push(a.doubleValue() < b.doubleValue());            
        return true;
    }

    @Override
    public String toString() {
        return "LT{" + '}';
    }
}
