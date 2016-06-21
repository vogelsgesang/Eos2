package de.lathanda.eos.interpreter.commands;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.Machine;

/**
 * 
 * Assembler Befehl: Nichts tun.
 * Kann verwendet werden um Lücken zu füllen, wenn eine 
 * korrekte Anordnung zu aufwendig wäre.
 *
 * @author Peter (Lathanda) Schneider
 */
public class NOP extends Command {

    @Override
    public boolean execute(Machine m) throws Exception {
        return true;
    }

    @Override
    public String toString() {
        return "NOP{" + '}';
    }
    
}
