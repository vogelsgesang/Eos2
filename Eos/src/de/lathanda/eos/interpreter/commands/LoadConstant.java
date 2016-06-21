package de.lathanda.eos.interpreter.commands;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.Machine;

/**
 * 
 * Assembler Befehl: Konstante laden.
 *
 * @author Peter (Lathanda) Schneider
 */
public class LoadConstant extends Command {
    Object data;

    public LoadConstant(Object data) {
        this.data = data;
    }

    @Override
    public boolean execute(Machine m) throws Exception {
        m.push(data);
        return true;
    }

    @Override
    public String toString() {
        return "LoadConstant{" + data + '}';
    }   
}
