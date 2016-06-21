package de.lathanda.eos.gui.objectchart;

import java.text.MessageFormat;

import de.lathanda.eos.util.ConcurrentLinkedList;
/**
 * Zeichnet eine Liste.
 * 
 * Im Moment wird nur die Anzahl der Elemente angezeigt.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.9.4
 */
public class ListValue extends TextValue {
	public ListValue(ConcurrentLinkedList<?> data) {
		super(buildValue(data));
	}
	private static String buildValue(ConcurrentLinkedList<?> data) {
		return MessageFormat.format(lm.getName("1count"), data.getLength());		
	}
}
