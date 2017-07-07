package de.lathanda.eos.common.interpreter;

import javax.swing.ImageIcon;

import de.lathanda.eos.util.GuiToolkit;

public interface AutoCompleteInformation extends Comparable<AutoCompleteInformation> {
	final static ImageIcon[] ICON = {
   		 GuiToolkit.createSmallIcon("icons/gearwheels.png"),
   		 GuiToolkit.createSmallIcon("icons/memory.png"),
   		 GuiToolkit.createSmallIcon("icons/book.png"),
   		 GuiToolkit.createSmallIcon("icons/memory.png"),
   		 GuiToolkit.createSmallIcon("icons/element.png"),
    };
	final static char[] PREFIX = {	'@','%','!','$'	};
	final int METHOD = 0;
	final int PROPERTY = 1;
	final int CLASS = 2;
	final int PRIVATE = 3;
	final int NEUTRAL = 4;
	String getScantext();
	String getLabel();
	int getType();
	String getTooltip();
	String getLabelLong();
	AutoCompleteType getCls();
	String getSort();
	String getTemplate();
	@Override
	default int compareTo(AutoCompleteInformation b) {
		if (b.getType() == getType()) {
			return getSort().compareTo(b.getSort());
		} else {
			return getType() - b.getType();
		}
	}	
}
