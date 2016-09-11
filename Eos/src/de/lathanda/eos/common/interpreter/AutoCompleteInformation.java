package de.lathanda.eos.common.interpreter;

import javax.swing.ImageIcon;

import de.lathanda.eos.base.ResourceLoader;

public interface AutoCompleteInformation {
	final static ImageIcon[] ICON = new ImageIcon[]{
   		 ResourceLoader.loadIcon("icons/method.png"),
   		 ResourceLoader.loadIcon("icons/attribut.png"),
   		 ResourceLoader.loadIcon("icons/class.png")
    };

	final int METHOD = 0;
	final int PROPERTY = 1;
	final int CLASS = 2;
	String getScantext();
	String getLabel();
	int getType();
	String getTooltip();
	String getLabelLong();
	AbstractType getCls();
	String getSort();
	String getTemplate();
}
