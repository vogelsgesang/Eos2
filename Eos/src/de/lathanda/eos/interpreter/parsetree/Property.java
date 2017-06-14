package de.lathanda.eos.interpreter.parsetree;

import de.lathanda.eos.common.interpreter.AutoCompleteInformation;
import java.util.LinkedList;

/**
 * Speichert und behandelt eine Attributdeklaration.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Property extends Declaration {
	private UserClass uc;
	
    public Property(UserClass uc) {
		super();
		this.uc = uc;
	}

	@Override
    public void resolveNamesAndTypes(Expression with, Environment env) {
        if (vartype.isUnknown()) {
            env.addError(marker, "UnknownType", vartype);
        } else if (vartype == SystemType.getWindow()) {
            //a window variable was found, this information is used to determine if automatic window has to be generated
            env.setWindowExists();
        } else if (vartype.inherits(SystemType.getFigure())) {
            //a figure variable was found, this information is used to determine if automatic window has to be generated
        	env.setFigureExists();
        }
    }
    
	public LinkedList<AutoCompleteInformation> getAutoCompletes() {
		LinkedList<AutoCompleteInformation> result = new LinkedList<>();
		for (String name: names) {
			result.add(new AutoCompleteInfo(name, uc.getType(), AutoCompleteInformation.PROPERTY));
		}
		return result;
	}    
	public Type getPropertyType() {
		return vartype;
	}
}
