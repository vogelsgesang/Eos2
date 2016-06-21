package de.lathanda.eos.interpreter.commands;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.Machine;

/**
 * 
 * Assembler Befehl: Auf Ungleichheit prüfen.
 *
 * @author Peter (Lathanda) Schneider
 */
public class NEQ extends Command {

    public NEQ() {
    }
    
    @Override
    public boolean execute(Machine m) throws Exception {
        Object b = m.pop();
        Object a = m.pop();
        m.push(!a.equals(b));
        return true;    }

    @Override
    public String toString() {
        return "NEQ{" + '}';
    }
    
}
