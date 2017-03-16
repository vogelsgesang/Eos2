package de.lathanda.eos.interpreter.parsetree;

import java.util.ArrayList;

import de.lathanda.eos.interpreter.Command;

/**
 * Unterbricht das Programm.
 * @author Peter (Lathanda) Schneider
 *
 */
public class Breakpoint  extends Node  {
	public Breakpoint() {
	}
	@Override
	public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
		ops.add(new de.lathanda.eos.interpreter.commands.Breakpoint(marker));		
	}

	@Override
	public void resolveNamesAndTypes(Expression with, Environment env) {		
		//not much to resolve it's a breakpoint
	}

	@Override
	public String getLabel() {
		return createText("Breakpoint.Label");
	}
}
