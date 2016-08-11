package de.lathanda.eos.common;

import java.util.List;

import de.lathanda.eos.spi.AutoCompleteEntry;

public interface AbstractType {

	boolean isUnknown();

	List<AutoCompleteEntry> getAutoCompletes();

}
