package de.lathanda.eos.common;

public interface CodeColorHook {
	void init(SourceCode source);

	void setFontSize(int fontsize);

	void unmarkExecutionPoint();

	void doColoring();

	void markError(Marker code);

	void markExecutionPoint(Marker codeRange);

}
