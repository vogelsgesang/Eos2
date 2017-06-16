package de.lathanda.eos.interpreter.commands;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.MClass;
import de.lathanda.eos.interpreter.MObject;
import de.lathanda.eos.interpreter.Machine;
import de.lathanda.eos.interpreter.parsetree.Property.Signature;

/**
 * 
 * Assembler Befehl: Wert in Attribut eines benutzerdefinierten Objektes speichern.
 *
 * @author Peter (Lathanda) Schneider
 */
public class StoreProperty extends Command {
	private final Signature variable;

	public StoreProperty(MClass cls, String variable) {
	        this.variable = new Signature(variable, cls.getName());
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
