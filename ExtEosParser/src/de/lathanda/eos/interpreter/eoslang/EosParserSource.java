package de.lathanda.eos.interpreter.eoslang;

import java.util.ResourceBundle;

import de.lathanda.eos.extension.BasicParser;
import de.lathanda.eos.extension.ParserSource;

public class EosParserSource implements ParserSource {
	public static final ResourceBundle LANG = ResourceBundle.getBundle("eoslang.eoslang"); 
	@Override
	public String getName() {
		return LANG.getString("Title");
	}

	@Override
	public String getDescription() {
		return LANG.getString("Description");
	}

	@Override
	public String getHelp() {
		return LANG.getString("Help");
	}

	@Override
	public BasicParser createParser(String source) {
		return new EosParser(source);
	}

	@Override
	public String getID() {
		return "eos2.german";
	}

}
