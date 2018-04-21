package de.lathanda.eos.extension;

public interface ParserSource {
	String getID();
	String getName();
	String getDescription();
	String getHelp();
	BasicParser createParser(String source);
}
