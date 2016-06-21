package de.lathanda.eos.interpreter.javacc;

import java.util.Locale;

import de.lathanda.eos.interpreter.Program;

public interface EosParser {
	void Parse(Program program, String path) throws ParseException;
	public static EosParser create(String code) {
		switch (Locale.getDefault().getLanguage()) {
		case "de":
			ParserTokenManager tokenmanager = new ParserTokenManager(new StringCharStream(code));
			return new Parser(tokenmanager);
		default:
			EnParserTokenManager entokenmanager = new EnParserTokenManager(new StringCharStream(code));
			return new EnParser(entokenmanager);
		}
	}
}
