package de.lathanda.eos.extension;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public interface Extension {
	public int getID();
	public default String getMacros() {
		return "";
	}
	public default String getMenues() {
		return "";
	}
	public default List<Command> getCommands(){
		return new LinkedList<>();
	}
	public default String getHelp() {
		return "";
	}
	public default String getClassDescriptions() {
		return "";		
	}
	public default List<ClassDefinition> getClassDefinitions() {
		return new LinkedList<>();
	}
	public default Class<?> getFunctionTarget() {
		return null;
	}

	public default List<ParserSource> getParserSources() {
		return new LinkedList<>();
	}
	public default NameFilter getNameFilter() {
		return null;
	}
	public default void announceConfig(Config config) {
		
	}

	public static class ExtensionComparator implements Comparator<Extension> {
		@Override
		public int compare(Extension a, Extension b) {
			return b.getID() - a.getID();
		}		
	}
}
