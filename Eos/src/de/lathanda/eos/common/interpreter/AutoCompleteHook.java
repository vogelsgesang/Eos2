package de.lathanda.eos.common.interpreter;

public interface AutoCompleteHook {

	void insertString(int pos, String text, AbstractProgram program);

}
