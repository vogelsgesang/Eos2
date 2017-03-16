package de.lathanda.eos.interpreter.commands;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.MClass;
import de.lathanda.eos.interpreter.MProcedure;
import de.lathanda.eos.interpreter.MType;
import de.lathanda.eos.interpreter.Machine;
import de.lathanda.eos.interpreter.ReservedVariables;

/**
 * 
 * Assembler Befehl: Object erzeugen.
 *
 * @author Peter (Lathanda) Schneider
 */
public class CreateObject extends Command {
    private final MProcedure init;
    private final MType classType;
    public CreateObject(MClass classType, int parameters) {
        this.init = classType.getMethod(ReservedVariables.INIT_PROCEDURE + "("+parameters+")");
        this.classType = classType;
    }
    @Override
    public boolean execute(Machine m) throws Exception {
    	m.push(classType.newInstance());
        m.call(init);
        return true;
    }

    @Override
    public String toString() {
        return "Function{" + init + '}';
    }
    
}
