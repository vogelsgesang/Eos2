/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lathanda.eos.interpreter.commands;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.Machine;

/**
 *
 * @author schneidp
 */
public class Jump extends Command {
    int relativ;

    public Jump(int relativ) {
        this.relativ = relativ;
    }

    @Override
    public boolean execute(Machine m) throws Exception {
        m.jump(relativ);
        return false;
    }

    @Override
    public String toString() {
        return "Jump{" + relativ + '}';
    }
    
}
