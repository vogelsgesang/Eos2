package de.lathanda.eos.common;
/**
 * Fabrik für Programme.
 * @author Peter (Lathanda) Schneider
 *
 */
public class Factory {
	private static Class<? extends AbstractProgram> programClass;
	
	public static AbstractProgram createProgram(String src) {
		try {
			return programClass.getConstructor(String.class).newInstance(src);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
			return null;
		}
	}	
	public static void setProgram(Class<? extends AbstractProgram> cls) {
		programClass = cls;
	}

}
