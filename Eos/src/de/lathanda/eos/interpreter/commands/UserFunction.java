package de.lathanda.eos.interpreter.commands;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.Machine;
import de.lathanda.eos.interpreter.MethodType;

/**
 * 
 * Assembler Befehl: Benutzerdefinierte Funktion aufrufen.
 *
 * @author Peter (Lathanda) Schneider
 */
public class UserFunction extends Command {
    private final MethodType methodType;
    public UserFunction(MethodType methodType) {
        this.methodType = methodType;
    }

    @Override
    public boolean execute(Machine m) throws Exception {
        Object[] args = methodType.popArguments(m);
        m.call(methodType, args);
        return true;
    }

    @Override
    public String toString() {
        return "UserFunction{" + methodType + '}';
    }
    
}
