package de.lathanda.eos.spi;

import de.lathanda.eos.extension.ObjectSource;

/**
 * Die Schnittstelle dient zum ergänzen 
 * hart kodierter Information einer Spracherweiterung.
 * 
 * @author Peter (Lathanda) Schneider
 *
 */
public interface LanguageProvider {
	ObjectSource getObjectSource(String id);
	default String getSuper(String id) {
		return null;
	}
	Class<?> getClassById(String id);
	Class<?> getFunctionTarget();
}
