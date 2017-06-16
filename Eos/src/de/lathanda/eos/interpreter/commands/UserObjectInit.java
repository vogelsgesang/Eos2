package de.lathanda.eos.interpreter.commands;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.MClass;
import de.lathanda.eos.interpreter.MObject;
import de.lathanda.eos.interpreter.Machine;

/**
 * 
 * Assembler Befehl: User Objekt erzeugen.
 *
 * @author Peter (Lathanda) Schneider
 */
public class UserObjectInit extends Command {
	private final MClass cls;

	public UserObjectInit(MClass cls) {
	    this.cls = cls;
	}

	@Override
	public boolean execute(Machine m) throws Exception {
		MObject obj = new MObject(cls, m);
		m.push(obj);
		return true;
	}

	@Override
	public String toString() {
		return "Create MObject{" + cls + '}';
	}

}
