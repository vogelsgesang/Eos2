package de.lathanda.eos.common;

import java.util.List;

public interface AbstractType {

	boolean isUnknown();

	List<AutoCompleteInformation> getAutoCompletes();

}
