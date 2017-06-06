package de.lathanda.eos.interpreter.commands;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.MObject;
import de.lathanda.eos.interpreter.Machine;

/**
 * 
 * Assembler Befehl: Wert aus Attribut eines benutzerdefinierten Objektes laden.
 *
 * @author Peter (Lathanda) Schneider
 */
public class LoadProperty extends Command {
	private String variable;

	public LoadProperty(String variable) {
	        this.variable = variable;
	    }

	@Override
	public boolean execute(Machine m) throws Exception {
		MObject obj = (MObject)m.pop();
		m.push(obj.getProperty(variable));
		return true;
	}

	@Override
	public String toString() {
		return "LoadProperty{" + variable + '}';
	}

}
