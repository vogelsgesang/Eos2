package de.lathanda.eos.common.interpreter;

import java.util.List;

public interface AutoCompleteType {

	boolean isUnknown();

	List<AutoCompleteInformation> getAutoCompletes();

}
