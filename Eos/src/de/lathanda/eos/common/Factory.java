package de.lathanda.eos.common;

import de.lathanda.eos.interpreter.Program;
/**
 * Fabrik für Programme.
 * @author Peter (Lathanda) Schneider
 *
 */
public class Factory {
	private static Class<?> programClass;
	
	public static AbstractProgram createProgram(String src) {
		try {
			return (AbstractProgram)programClass.getConstructor(String.class).newInstance(src);
		} catch (Exception e) {
			return new Program(src);
		}
	}	
	public static void setProgram(Class<?> cls) {
		programClass = cls;
	}

}
