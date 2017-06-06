package de.lathanda.eos.interpreter.parsetree;

import de.lathanda.eos.common.interpreter.AutoCompleteInformation;
import de.lathanda.eos.common.interpreter.AutoCompleteType;

class AutoCompleteInfo implements AutoCompleteInformation {
	private String name;
	private AutoCompleteType t;
	
	public AutoCompleteInfo(String name, AutoCompleteType t) {
		this.name = name;
		this.t = t;
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
		return CLASS;
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
		return "@" + name;
	}

	@Override
	public String getTemplate() {
		return name;
	}
	
}
