package de.lathanda.eos.interpreter.commands;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.MObject;
import de.lathanda.eos.interpreter.Machine;

/**
 * 
 * Assembler Befehl: Wert in Attribut eines benutzerdefinierten Objektes speichern.
 *
 * @author Peter (Lathanda) Schneider
 */
public class StoreProperty extends Command {
	private String variable;

	public StoreProperty(String variable) {
	        this.variable = variable;
	    }

	@Override
	public boolean execute(Machine m) throws Exception {
		MObject obj = (MObject)m.pop();
		Object data = m.pop();
		obj.setProperty(variable, data);
		return true;
	}

	@Override
	public String toString() {
		return "StoreProperty{" + variable + '}';
	}

}
