package de.lathanda.eos.interpreter.parsetree;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.Environment;
import de.lathanda.eos.interpreter.MethodType;
import de.lathanda.eos.interpreter.Node;
import de.lathanda.eos.interpreter.Program;
import de.lathanda.eos.interpreter.Type;
import de.lathanda.eos.interpreter.commands.CreateVariable;
import de.lathanda.eos.interpreter.commands.DebugPoint;
import de.lathanda.eos.interpreter.commands.LoadVariable;
import de.lathanda.eos.interpreter.commands.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Speichert und behandelt eine Variablendeklaration.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.4
 */
public class Declaration extends Node {
	static {
		try {
			ADD_FIGURE = new MethodType(Type.getWindow(), new Type[]{Type.getFigure()}, Type.getVoid(), "addFigure", "");
		}catch(NoSuchMethodException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
    private static /*final*/ MethodType ADD_FIGURE; //java doesn't allow final here, but it should be final
    private final List<String> names;
    private Type vartype;

    public Declaration() {
        this.names = new LinkedList<>();
        this.type = Type.getVoid();
        this.vartype = Type.getUnknown();
    }

    public void addName(String name) {
        names.add(name);
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
        names.stream().forEach((name) -> {
            if (autoWindow && vartype.isFigure() && !vartype.isAbstract()) {
                ops.add(new CreateVariable(name, vartype));
                ops.add(new LoadVariable(name));
                ops.add(new LoadVariable(ReservedVariables.WINDOW));
                ops.add(new Method(ADD_FIGURE));
            } else {
                ops.add(new CreateVariable(name, vartype));
            }
        });
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
        } else if (vartype.isWindow()) {
            //a window variable was found, this information is used to determine if automatic window has to be generated
            env.setWindowExists();
        } else if (vartype.isFigure()) {
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
