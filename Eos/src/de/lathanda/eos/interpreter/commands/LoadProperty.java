package de.lathanda.eos.interpreter.commands;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.MClass;
import de.lathanda.eos.interpreter.MObject;
import de.lathanda.eos.interpreter.Machine;
import de.lathanda.eos.interpreter.parsetree.Property.Signature;;

/**
 * 
 * Assembler Befehl: Wert aus Attribut eines benutzerdefinierten Objektes laden.
 *
 * @author Peter (Lathanda) Schneider
 */
public class LoadProperty extends Command {
	private final Signature variable;

	public LoadProperty(MClass cls, String variable) {
	    this.variable = new Signature(variable, cls.getName());
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
