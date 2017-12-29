package de.lathanda.eos.interpreter.javacc;

import java.util.Locale;

import de.lathanda.eos.interpreter.parsetree.Program;

public abstract class EosParser {
	protected StringCharStream scs;
	public abstract void Parse(Program program, String path) throws ParseException;
	public static EosParser create(String code) {
		scs = new StringCharStream(code);
		switch (Locale.getDefault().getLanguage()) {
		case "de":
			ParserTokenManager tokenmanager = new ParserTokenManager(scs);
			Parser parser = new Parser(tokenmanager);
			parser.scs = scs;
			return parser;
		default:
			EnParserTokenManager entokenmanager = new EnParserTokenManager(scs);
			EnParser enparser = new EnParser(entokenmanager);
			enparser.scs = scs;
			return enparser;
		}
	}
	public int getLine(int pos) {
		return scs.getLine(pos);
	}
}
