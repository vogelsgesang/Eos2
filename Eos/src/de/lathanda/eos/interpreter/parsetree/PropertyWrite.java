package de.lathanda.eos.interpreter.parsetree;

import de.lathanda.eos.interpreter.Node;
import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.Environment;
import de.lathanda.eos.interpreter.MethodType;
import de.lathanda.eos.interpreter.Type;
import de.lathanda.eos.interpreter.commands.Method;
import de.lathanda.eos.interpreter.commands.StoreVariable;
import java.util.ArrayList;

/**
 * Speichert und behandelt einen schreibenden Variablenzugriff.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.4
 */
public class PropertyWrite extends Node {

    private Expression target;
    private final String member;
    private MethodType methodType;
    private boolean isVariable;

    public PropertyWrite(Expression target, String member) {
        this.target = target;
        this.member = member.toLowerCase();
    }


    public String getMember() {
        return member;
    }

    @Override
    public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
        if (isVariable) {
            ops.add(new StoreVariable(member));
        } else {
            if (target != null) {
                target.compile(ops, autoWindow);
                ops.add(new Method(methodType));
            }
        }
    }

    @Override
    public void resolveNamesAndTypes(Expression with, Environment env) {
        if (target != null) {
            //access member
            target.resolveNamesAndTypes(with, env);
            accessMember(env);
        } else {
            //try direct variable
            type = env.getVariableType(member);
            if (!type.isUnknown()) {
                //variable exists
                isVariable = true;
            } else if (with != null) {
                target = with;
                //access member
                target.resolveNamesAndTypes(null, env);
                accessMember(env);
            } else {
                env.addError(marker, "UnknownVariable", member);
            }
        }        
    }

    private void accessMember(Environment env) {
        //access member
        type = target.getType();
        target.resolveNamesAndTypes(null, env);
        methodType = type.getAssignProperty(member);
        isVariable = false;
        if (methodType == null) {
            env.addError(marker, "UnknownMember", type + "." + member);
            type = Type.getUnknown();
        } else {
        	if (env.getLockProperties()) {
                env.addError(marker, "LockedMember", type + "." + member);
                type = Type.getUnknown();
        	} else {        	
        		type = methodType.getParameterType(0);
        	}
        }
    }
    @Override
    public String toString() {
        if (target != null) {
            return target + "." + member;
        } else {
            return member;
        }
    }  
    @Override
    public String getLabel() {
        if (target != null) {
            return createText("WriteA.Label", target.getLabel(), member);
        } else {
        	if (member.equals(ReservedVariables.RESULT)) {
        		return createText("Result");
        	} else {
        		return createText("WriteB.Label", member);
        	}
        }
    }      
}
