package de.lathanda.eos.interpreter;

import java.util.LinkedList;
/**
 * Ein Algorithmus zum umformatieren des Quellcodes.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.9
 */
public class PrettyPrinter {
	private static final String TAB = "    ";
	private LinkedList<Newline> newlines;
	private String source;
	public PrettyPrinter(String source) {
		this.source = source;
		newlines = new LinkedList<>();
	}

	public String prettyPrint() {
		int index = 0;
		int level = 0;
		String linetext;
		StringBuilder pretty = new StringBuilder();
		for (Newline line: newlines) {
			linetext = source.substring(index, line.position);
			int crs = linetext.replaceAll("\n", "xx").length() - linetext.length();
			linetext = linetext.trim();

			index = line.position;
			if (!linetext.isEmpty()) {
				for(int i = level; i --> 0; ) {
					pretty.append(TAB);
				}
				pretty.append(linetext);
				pretty.append("\n");
				for(int i = 1; i < crs; i++) {
					pretty.append("\n");
				}
			}
			level = line.level;
		}
		linetext = source.substring(index, source.length()).trim();
		if (!linetext.isEmpty()) {
			for(int i = level; i --> 0; ) {
				pretty.append(TAB);
			}
			pretty.append(linetext);
			pretty.append("\n");
		}
		return pretty.toString();
	}
	public void newline(int position, int level) {
		newlines.add(new Newline(position, level));		
	}
	private class Newline {
		int position;
		int level;
		public Newline(int position, int level) {
			this.position = position;
			this.level = level;
		}
		
	}
}
