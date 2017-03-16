package de.lathanda.eos.common.interpreter;

import javax.swing.ImageIcon;

import de.lathanda.eos.base.ResourceLoader;

public interface AutoCompleteInformation extends Comparable<AutoCompleteInformation> {
	final static ImageIcon[] ICON = new ImageIcon[]{
   		 ResourceLoader.loadIcon("icons/method.png"),
   		 ResourceLoader.loadIcon("icons/attribut.png"),
   		 ResourceLoader.loadIcon("icons/class.png"),
   		 ResourceLoader.loadIcon("icons/attribut.png"),
    };

	final int METHOD = 0;
	final int PROPERTY = 1;
	final int CLASS = 2;
	final int PRIVATE = 3;
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
