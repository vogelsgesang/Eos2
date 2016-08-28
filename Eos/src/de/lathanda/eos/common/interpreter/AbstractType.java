package de.lathanda.eos.common.interpreter;

import java.util.List;

public interface AbstractType {

	boolean isUnknown();

	List<AutoCompleteInformation> getAutoCompletes();

}
