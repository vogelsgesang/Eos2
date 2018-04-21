package de.lathanda.eos.extension;

import de.lathanda.eos.common.interpreter.TranslationException;
import de.lathanda.eos.interpreter.parsetree.Program;

public interface BasicParser {
	void parse(Program program, String path) throws TranslationException;
	int getLine(int pos);
}