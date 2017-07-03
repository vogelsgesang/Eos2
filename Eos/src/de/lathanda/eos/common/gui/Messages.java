package de.lathanda.eos.common.gui;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * \brief Texte
 * 
 * Zentrale Quelle für alle übersetzungsabhängigen Texte.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.9.5
 */
public abstract class Messages {
	private static ResourceBundle ERROR = ResourceBundle.getBundle("text.error");	
    private static ResourceBundle GUI   = ResourceBundle.getBundle("text.gui");	
	private Messages(){}
	public static String getError(String id) {
		return ERROR.getString(id);
	}
	public static String formatError(String id, Object ... info)  {
		return MessageFormat.format(ERROR.getString(id), info);
	}
	public static String getString(String id) {
		return GUI.getString(id);
	}
}
