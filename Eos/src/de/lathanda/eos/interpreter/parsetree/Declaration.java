package de.lathanda.eos.interpreter.parsetree;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.ReservedVariables;
import de.lathanda.eos.interpreter.commands.CreateVariable;
import de.lathanda.eos.interpreter.commands.DebugPoint;
import de.lathanda.eos.interpreter.commands.LoadVariable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Speichert und behandelt eine Variablendeklaration.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Declaration extends Node {
	static {
		try {
			ADD_FIGURE = new SystemMethodType(SystemType.getWindow(), new SystemType[]{SystemType.getFigure()}, Type.getVoid(), "addFigure", "");
		}catch(NoSuchMethodException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	protected static /*final*/ MethodType ADD_FIGURE; //java doesn't allow final here, but it should be final
    protected final List<String> names;
    protected Type vartype;

    public Declaration() {
        this.names = new LinkedList<>();
        this.type = Type.getVoid();
        this.vartype = Type.getUnknown();
    }

    public void addName(String name) {
        names.add(name.toLowerCase());
    }

    public List<String> getNames() {
        return names;
    }

    public void setType(Type vartype) {
        this.vartype = vartype;
        names.stream().forEach((name) -> Program.addGuess(name, vartype));
    }

    @Override
    public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
        ops.add(new DebugPoint(marker));
        for(String name : names) {
            if (autoWindow && vartype.inherits(SystemType.getFigure()) && !vartype.isAbstract()) {
                ops.add(new CreateVariable(name, vartype.getMType()));
                ops.add(new LoadVariable(name));
                ops.add(new LoadVariable(ReservedVariables.WINDOW));
                ADD_FIGURE.compile(ops, null, autoWindow);
            } else {
                ops.add(new CreateVariable(name, vartype.getMType()));
            }
        };
    }

    @Override
    public void resolveNamesAndTypes(Expression with, Environment env) {
        names.stream().forEach((name) -> {
        	if (env.isVariableDefined(name)) {
        		env.addError(marker, "DoubleVariableDefinition", name);
        	} else {
        		env.setVariableType(name.toLowerCase(), vartype);
        	}
        });

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
    
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        boolean first = true;
        for (String name : names) {
            if (first) {
                res.append(name);
            } else {
                res.append(",").append(name);
                first = false;
            }
        }
        res.append(":").append(vartype);
        return res.toString();
    }

    @Override
    public String getLabel() {
        StringBuilder text = new StringBuilder();
        boolean first = true;
        for (String name : names) {
            if (first) {
                text.append(name);
            } else {
                text.append(", ").append(name);
            }
        }
        text.append(":").append(vartype);
        return text.toString();        
    }
}
