package de.lathanda.eos.common.interpreter;

public interface AutoCompleteInformation {
	String getScantext();
	String getLabel();
	int getType();
	String getTooltip();
	AbstractType getCls();
	String getSort();
	String getTemplate();
}
