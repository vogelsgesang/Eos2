package de.lathanda.eos.spi;

import de.lathanda.eos.common.interpreter.AutoCompleteType;
import de.lathanda.eos.common.interpreter.AutoCompleteInformation;
import de.lathanda.eos.common.interpreter.MissingTypeException;
import de.lathanda.eos.interpreter.parsetree.Type;

public class AutoCompleteEntry  implements AutoCompleteInformation {
	/**
	 * Basistyp
	 */
	public final Type cls;
	/**
	 * Text der mit der Eingabe abgeglichen wird.
	 */
	public final String scantext;
	/**
	 * Text der in der Liste angezeigt wird.
	 */
	public final String label;
	/**
	 * Signatur
	 */
	public final String labelLong;
	/**
	 * Sortierkriterium.
	 */
	public final String sort;
	/**
	 * Hilfe die bei Mausover angezeigt wird (Tooltip).
	 */
	public final String tooltip;
	/**
	 * Text der beim Anwenden eingefügt wird.
	 */
	public final String template;
	/**
	 * Art der Auswahl. Methode, Attribut, Klasse, Template.
	 * Beeinflusst verwendetes Icon und Sortierung.
	 */
	public final int type;
	/**
	 * Erzeugt eine typbasierte Auswahl.
	 * @param key Schlüssel Format: &lt;Scantext&gt; = ...
	 * @param description Wert Format: ... &lt;Typ&gt;&lt;Anzeige&gt;#&lt;Tooltip als Html&gt;
	 * @throws MissingTypeException 
	 */
	public AutoCompleteEntry(String key, String description) throws MissingTypeException {
		//class.Grafik              = !Grafik#Ein Bild in einem Rechteck.
		int dot = key.indexOf('.');

		this.cls = Type.getInstanceByID(key.substring(0, dot));
		this.scantext = key.substring(dot + 1);
		if (cls == null) {
			throw new MissingTypeException(key.substring(0, dot));
		}
		
		int separator = description.indexOf("#");
		this.sort = scantext;
		this.tooltip = description.substring(separator+1);
		this.template = description.substring(1, separator);
		this.label = template;
		if (tooltip.indexOf("<br>") >= 0) {
			this.labelLong = tooltip.substring(0, tooltip.indexOf("<br>"));
		} else {
			this.labelLong = label;
		}
		switch (description.charAt(0)) {
		case '@':
			type = AutoCompleteInformation.METHOD;
			break;
		case '%':
			type = AutoCompleteInformation.PROPERTY;
			break;
		case '!':
			type = AutoCompleteInformation.CLASS;
			break;
		case '$':
			type = AutoCompleteInformation.PRIVATE;
			break;
		default:
			type = AutoCompleteInformation.METHOD;
		}
	}	
	/**
	 * Erzeugt eine templatebasierte Auswahl.
	 * @param description Wert Format: &lt;Anzeige&gt;#&lt;Template&gt;
	 * @param sort Das Sortierungskriterium ist der erste Buchstabe des Schüssels.
	 */
	public AutoCompleteEntry(String description, char sort) {
		int separator = description.indexOf("#");
		this.scantext = "";
		this.tooltip = "";
		this.sort = String.valueOf(sort);
		this.template = description.substring(separator+1);
		this.label = description.substring(0, separator);
		this.labelLong = label;
		type = 3;
		cls = Type.getVoid();
	}

	@Override
	public String getScantext() {
		return scantext;
	}
	public AutoCompleteType getCls() {
		return cls;
	}
	public String getLabel() {
		return label;
	}
	public String getSort() {
		return sort;
	}
	public String getTooltip() {
		return tooltip;
	}
	public String getTemplate() {
		return template;
	}
	public int getType() {
		return type;
	}
	@Override
	public String getLabelLong() {
		return labelLong;
	}
	
}
