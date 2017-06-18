package de.lathanda.eos.interpreter.parsetree;

import java.util.ArrayList;
import de.lathanda.eos.common.interpreter.AutoCompleteType;
import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.MType;
import de.lathanda.eos.interpreter.ReservedVariables;
import de.lathanda.eos.interpreter.commands.UserFunction;

/**
 * Methodendefinition.
 * Speichert alle Daten die zum behandeln von Methoden notwendig sind.
 *
 * @author Peter (Lathanda) Schneider
 */
public class UserFunctionType implements MethodType {
    protected final Type[] parameters;
    protected final Type ret;
    protected final String name;
    protected final String signature;
    /**
     * Erzeugt eine statische Java Methode
     * @param name
     * @param parameters
     * @param ret
     */
    public UserFunctionType(String name, Type[] parameters, Type ret) {
		this.parameters = parameters;
		this.ret = ret;
		this.name = name;
		this.signature = ReservedVariables.createSignature(name, parameters.length);
	}    

    public String getSignature() {
        return signature;
    }
     
    public boolean checkType(Type[] args) {
        if (parameters.length != args.length) {
            return false;
        }
        for(int i = 0; i < args.length; i++) {
            if(!parameters[i].checkType(args[i])) {
                return false;
            }
        }
        return true;
    }
    public Type getReturnType() {
        return ret;
    }
    public Type getParameterType(int i) {
        return parameters[i];
    }
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        boolean first = true;
        res.append(name).append("(");
        for(AutoCompleteType t: parameters) {
            if (first) {
                res.append(t);
            } else {
                res.append(",").append(t);
                first = false;
            }
        }
        res.append(")");
        if (ret != null && !ret.isVoid()) {
            res.append(":").append(ret);
        }
        return res.toString();
    }

	public MType[] getParameters() {
    	MType[] para = new MType[parameters.length];
    	for(int i = 0; i < para.length; i++) {
    		para[i] = parameters[i].getMType();
    	}
    	return para;
	}

	public void compile(ArrayList<Command> ops, Expression target, boolean autowindow) throws Exception {
		ops.add(new UserFunction(getSignature()));
    }
}
