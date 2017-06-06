package de.lathanda.eos.spi;

import de.lathanda.eos.interpreter.parsetree.SystemType.ObjectSource;
/**
 * Die Schnittstelle dient zum ergänzen 
 * hart kodierter Information einer Spracherweiterung.
 * 
 * @author Peter (Lathanda) Schneider
 *
 */
public interface LanguageProvider {
	ObjectSource getObjectSource(String id);
	default String[] getInherits(String id) {
		return new String[]{id};
	}
	Class<?> getClassById(String id);
	Class<?> getFunctionTarget();
}
