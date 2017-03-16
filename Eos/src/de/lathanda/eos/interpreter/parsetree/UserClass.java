package de.lathanda.eos.interpreter.parsetree;

import java.util.ArrayList;
import java.util.LinkedList;

import de.lathanda.eos.common.interpreter.Marker;
import de.lathanda.eos.common.interpreter.ProgramSequence;
import de.lathanda.eos.common.interpreter.ProgramUnit;
import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.MType;

/**
 * Speichert und behandelt Benutzerdefinierte Klassen
 *
 * @author Peter (Lathanda) Schneider
 */
public class UserClass implements ProgramUnit {
	private final LinkedList<SubRoutine> meths  = new LinkedList<>();
	private final LinkedList<Declaration> props = new LinkedList<>();
	private Sequence constructor;
	private final String name;
	private final String sup;

	public UserClass(String name, String sup) {
		this.name = name;
		this.sup = sup;
	}

	public void addDeclaration(Declaration prop) {
		props.add(prop);		
	}

	public void addMethod(SubRoutine meth) {
		meths.add(meth);
	}

	public MType compile() throws Exception {
		return null;
		// TODO Auto-generated method stub
		
	}

	public void resolveNamesAndTypes(Environment env) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	public void registerUserClass(Environment env) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Marker getMarker() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProgramSequence getSequence() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addConstructor(Sequence sequence) {
		// TODO Auto-generated method stub
		
	}

}
