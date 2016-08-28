package de.lathanda.eos.common.gui;

import de.lathanda.eos.common.interpreter.Marker;

public interface CodeColorHook {
	void init(SourceCode source);

	void setFontSize(int fontsize);

	void unmarkExecutionPoint();

	void doColoring();

	void markError(Marker code);

	void markExecutionPoint(Marker codeRange);

}
