package de.lathanda.legoev3.spi;

import java.util.LinkedList;
import java.util.List;

import de.lathanda.eos.extension.ClassDefinition;
import de.lathanda.eos.extension.Command;
import de.lathanda.eos.extension.Extension;
import de.lathanda.legoev3.SeekEv3Dialog;
import de.lathanda.legoev3.brick.Connection;

public class LegoEv3RemoteExtension implements Extension {
	private SeekEv3Dialog seekev3;
	
	public LegoEv3RemoteExtension() {
		seekev3 = new SeekEv3Dialog();
	}

	@Override
	public int getID() {
		return 10;
	}

	@Override
	public List<ClassDefinition> getClassDefinitions() {
		List<ClassDefinition> def = new LinkedList<>();
		def.add(new ClassDefinition("ev3remote", () -> {return new Connection();}, null, Connection.class));
		return def;
	}

	@Override
	public List<Command> getCommands() {
		List<Command> cmd = new LinkedList<>();
		cmd.add(seekev3.new Show());
		return cmd;
	}

}
