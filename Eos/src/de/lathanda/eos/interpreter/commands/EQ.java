package de.lathanda.eos.interpreter.commands;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.Machine;

/**
 * 
 * Assembler Befehl: Test auf Gleichheit.
 *
 * @author Peter (Lathanda) Schneider
 */
public class EQ extends Command {

    public EQ() {
    }
    
    @Override
    public boolean execute(Machine m) throws Exception {
        Object b = m.pop();
        Object a = m.pop();
        if (a instanceof Integer && b instanceof Integer) {
        	m.push(((Integer)b).intValue() == ((Integer)a).intValue());
        } else if (a instanceof Number && b instanceof Number) {
        	m.push(((Number)b).doubleValue() == ((Number)a).doubleValue());
        } else {
        	m.push(a.equals(b));
        }
        return true;    
    }

    @Override
    public String toString() {
        return "EQ{" + '}';
    }
    
}
