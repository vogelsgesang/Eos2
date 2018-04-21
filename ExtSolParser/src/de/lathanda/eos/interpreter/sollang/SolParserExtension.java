package de.lathanda.eos.interpreter.sollang;

import java.util.LinkedList;
import java.util.List;

import de.lathanda.eos.extension.Extension;
import de.lathanda.eos.extension.ParserSource;

public class SolParserExtension implements Extension {

	@Override
	public int getID() {
		return 1;
	}
	
	@Override
	public List<ParserSource> getParserSources() {
		LinkedList<ParserSource> list = new LinkedList<>();
		list.add(new SolParserSource());
		return list;
	}
}
