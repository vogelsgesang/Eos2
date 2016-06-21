package de.lathanda.eos.interpreter.commands;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.Machine;

/**
 * 
 * Assembler Befehl: Integer Hoch rechnen.
 *
 * @author Peter (Lathanda) Schneider
 */public class PowI extends Command {

    @Override
    public boolean execute(Machine m) throws Exception {
        Object b = m.pop();
        Object a = m.pop();
      	int power = 1;
       	int ai = ((Number)a).intValue();
       	for (int i = ((Number)b).intValue(); i --> 0; ) {
       		power *= ai;
       	}
        m.push(power);
        return true;
    }

    @Override
    public String toString() {
        return "Multiply{" + '}';
    }
}
