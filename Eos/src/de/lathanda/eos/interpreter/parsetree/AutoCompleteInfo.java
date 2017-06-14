package de.lathanda.eos.interpreter.parsetree;

import de.lathanda.eos.common.interpreter.AutoCompleteInformation;
import de.lathanda.eos.common.interpreter.AutoCompleteType;

class AutoCompleteInfo implements AutoCompleteInformation {
	private String name;
	private AutoCompleteType t;
	private int type;
	
	public AutoCompleteInfo(String name, AutoCompleteType t, int type) {
		this.name = name;
		this.t = t;
		this.type = type;
	}

	@Override
	public String getScantext() {
		return name;
	}

	@Override
	public String getLabel() {
		return name;
	}

	@Override
	public int getType() {
		return type;
	}

	@Override
	public String getTooltip() {
		return "";
	}

	@Override
	public String getLabelLong() {
		return name;
	}

	@Override
	public AutoCompleteType getCls() {
		return t;
	}

	@Override
	public String getSort() {
		return PREFIX[type] + name;
	}

	@Override
	public String getTemplate() {
		return name;
	}	
}
