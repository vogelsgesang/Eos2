package de.lathanda.eos.interpreter.commands;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.MProcedure;
import de.lathanda.eos.interpreter.Machine;

/**
 * 
 * Assembler Befehl: Methode aufrufen
 *
 * Stacklayout [..., para1, ... , paran, target]
 * @author Peter (Lathanda) Schneider
 */
public class UserMethod extends Command {
    private final MProcedure method;
    public UserMethod(MProcedure method) {
        this.method = method;
    }
    @Override
    public boolean execute(Machine m) throws Exception {
    	m.call(method);
        return true;
    }   

    @Override
    public String toString() {
        return "UserMethod{" + method.getSignature()+" }";
    }
    
}
