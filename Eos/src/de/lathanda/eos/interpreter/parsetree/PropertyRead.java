package de.lathanda.eos.interpreter.parsetree;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.Environment;
import de.lathanda.eos.interpreter.MethodType;
import de.lathanda.eos.interpreter.Type;
import de.lathanda.eos.interpreter.commands.LoadVariable;
import de.lathanda.eos.interpreter.commands.Method;
import de.lathanda.eos.spi.LanguageManager;

import java.util.ArrayList;

/**
 * Speichert und verwaltet einen lesenden Variablenzugriff.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.4
 */
public class PropertyRead extends Expression {

    private Expression target;
    private String member;
    private MethodType methodType;
    private boolean isVariable;

    public PropertyRead(Expression target, String member) {
        this.target = target;
        this.member = member.toLowerCase();
        prio = 7;
    }

    public String getMember() {
        return member;
    }

    @Override
    public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
        if (isVariable) {
            ops.add(new LoadVariable(member));
        } else if (target != null) {
            target.compile(ops, autoWindow);
            ops.add(new Method(methodType));
        } 
    }

    @Override
    public void resolveNamesAndTypes(Expression with, Environment env) {
        if (target != null) {
            //access member
            accessMember(env);
        } else {
            //try direct variable
            type = env.getVariableType(member);
            if (!type.isUnknown()) {
                //variable exists
                isVariable = true;
            } else if (with != null) {
                //try with
                target = with;
                target.resolveNamesAndTypes(null, env);
                //access member
                accessMember(env);
            } else if (member.equals(LanguageManager.getInstance().getDefaultWindowName())) {
            	env.setFigureExists();
            	if (env.getAutoWindow()) {
            		member = ReservedVariables.WINDOW;
            		isVariable = true;
            		type = Type.getWindow();
            	}
            } else {
                env.addError(marker, "UnknownVariable", member);
                type = Type.getUnknown();
            }
        }

    }

    private void accessMember(Environment env) {
        //access member
        target.resolveNamesAndTypes(null, env);
        type = target.getType();
        methodType = type.getReadProperty(member);
        isVariable = false;
        if (methodType == null) {
            env.addError(marker, "UnknownMember", type + "." + member);
            type = Type.getUnknown();
        } else {
        	if (LanguageManager.getInstance().getLockProperties()) {
                env.addError(marker, "LockedMember", type + "." + member);
                type = Type.getUnknown();
        	} else {
        		type = methodType.getReturnType();
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
            return createText("ReadA.Label", getLabelRight(target), member);
        } else {
        	if (member.equals(ReservedVariables.RESULT)) {
        		return createText("Result");
        	} else {
        		return createText("ReadB.Label", member);
        	}        	
        }
    }    
   
}
