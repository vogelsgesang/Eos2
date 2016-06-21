package de.lathanda.eos.interpreter.commands;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.Machine;
import de.lathanda.eos.interpreter.Type;

/**
 * 
 * Assembler Befehl: Variable anlegen.
 *
 * @author Peter (Lathanda) Schneider
 */
public class CreateVariable extends Command {
    private final String name;
    private final Type type;

    public CreateVariable(String name, Type type) {
        this.name = name.toLowerCase();
        this.type  = type;
    }
    
    @Override
    public boolean execute(Machine m) throws Exception {
        m.create(name, type);
        return true;
    }

    @Override
    public String toString() {
        return "ReserveMemory{" + name + ":" + type + '}';
    }
    
}
