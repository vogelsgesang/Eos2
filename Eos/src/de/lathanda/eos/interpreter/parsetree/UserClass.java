package de.lathanda.eos.interpreter.parsetree;

import java.util.ArrayList;

import de.lathanda.eos.common.interpreter.ProgramSequence;
import de.lathanda.eos.common.interpreter.ProgramUnit;
import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.Environment;
import de.lathanda.eos.interpreter.Node;

/**
 * Speichert und behandelt Benutzerdefinierte Klassen
 *
 * @author Peter (Lathanda) Schneider
 */
public class UserClass extends Node {

	public UserClass(String name, String sup) {
		// TODO Auto-generated constructor stub
	}

	public void addDeclaration(Declaration prop) {
		// TODO Auto-generated method stub
		
	}

	public void addMethod(SubRoutine meth) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resolveNamesAndTypes(Expression with, Environment env) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

}
