package de.lathanda.eos.interpreter.commands;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.Machine;

/**
 * 
 * Assembler Befehl: Logisches Nicht berechnen.
 *
 * @author Peter (Lathanda) Schneider
 */public class Not extends Command {

    public Not() {
    }

    @Override
    public boolean execute(Machine m) throws Exception {
        Boolean a = (Boolean)m.pop();
        m.push(!a);
        return true;    
    }

    @Override
    public String toString() {
        return "Not{" + '}';
    }
    
}
