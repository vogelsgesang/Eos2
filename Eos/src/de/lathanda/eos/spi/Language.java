package de.lathanda.eos.spi;

/**
 * \brief Spracherweiterung
 * 
 * Oberklasse für alle zusätzlichen Spracherweiterungen
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.9.7
 */
public abstract class Language {
	public abstract void registerLanguage(LanguageManager lm) throws Exception;
}
