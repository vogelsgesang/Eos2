package de.lathanda.eos.interpreter.commands;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.Machine;

/**
 * 
 * Assembler Befehl: Benutzerdefinierte Funktion aufrufen.
 *
 * @author Peter (Lathanda) Schneider
 */
public class UserFunction extends Command {
    private final String signature;
    public UserFunction(String signature) {
        this.signature = signature;
    }

    @Override
    public boolean execute(Machine m) throws Exception {
        m.call(signature);
        return true;
    }

    @Override
    public String toString() {
        return "UserFunction{" + signature + '}';
    }
    
}
