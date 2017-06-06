package de.lathanda.eos.interpreter.parsetree;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.MProcedure;

import java.util.ArrayList;

import de.lathanda.eos.interpreter.commands.DebugPoint;
import de.lathanda.eos.interpreter.commands.Function;
import de.lathanda.eos.interpreter.commands.Method;
import de.lathanda.eos.interpreter.commands.UserFunction;
import de.lathanda.eos.interpreter.commands.UserMethod;

/**
 * Speichert und behandelt einen Aufruf einer Methode, Funktion oder
 * benutzerdefinierten Funktion.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Invoke extends Expression {

    private enum FunctionType {
        USER,
        SYSTEM_FUNCTION,
        METHOD,
        USER_METHOD
    }

    private Expression target;
    private final String methodname;
    private final Arguments arguments;
    private MethodType methodType;
    private FunctionType functionType;

    public Invoke(Expression target, String methodname, Arguments arguments) {
        this.target = target;
        this.methodname = methodname;
        this.arguments = arguments;
        prio = 7;
    }

    public String getMethodname() {
        return methodname;
    }

    public Arguments getArguments() {
        return arguments;
    }

    @Override
    public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
        if (functionType == null || methodType == null) {
            return;
        }
        arguments.compile(ops, autoWindow);
        ops.add(new DebugPoint(marker));
        switch (functionType) {
            case USER:
                ops.add(new UserFunction(methodType.getSignature()));
                break;
            case SYSTEM_FUNCTION:
                ops.add(new Function(methodType.getParameters(), methodType.getMethod()));
                break;
            case METHOD:
                target.compile(ops, autoWindow);
                ops.add(new Method(methodType.getParameters(), methodType.getMethod()));
                break;
            case USER_METHOD:          
            	target.compile(ops, autoWindow);
            	UserType ut = (UserType)target.getType();
            	MProcedure mp = ut.getMClass().getMethod(methodType.getSignature());
            	ops.add(new UserMethod(mp));
        }
    }

    @Override
    public void resolveNamesAndTypes(Expression with, Environment env) {
        arguments.resolveNamesAndTypes(with, env);
        if (target == null) {
        	//Systemfunktion
            methodType = MethodType.getSystemFunction(methodname, arguments.getTypes().length);
            if (methodType != null) {
                functionType = FunctionType.SYSTEM_FUNCTION;
                if (!methodType.checkType(arguments.getTypes())) {
                    env.addError(marker, "FunctionType",
                            methodType,
                            arguments
                    );
                }
                type = methodType.getReturnType();
                return;
            } else {
                methodType = env.getFunctionSignature(methodname, arguments.getTypes().length);
                if (methodType != null) {
                    // usermethod
                    functionType = FunctionType.USER;
                    if (!methodType.checkType(arguments.getTypes())) {
                        env.addError(marker, "FunctionType",
                                methodType,
                                arguments
                        );
                    }
                    type = methodType.getReturnType();
                    return;
                } else {
                    if (with == null) {
                        env.addError(marker, "ProcedureNotFound", methodname);
                        return;
                    } else {
                        //apply with
                        target = with;
                        target.resolveNamesAndTypes(with, env);
                    }
                }
            }
        } else {
        	//method
            target.resolveNamesAndTypes(with, env);
        }

        Type tartype = target.getType();
        if (tartype == null) {
            env.addError(marker, "UnknownVariable",
                    target
            );
        } else {
            methodType = tartype.getMethod(methodname, arguments.getTypes().length);
        }
        if (methodType != null) {
        	if (methodType.isNative()) {
        		functionType = FunctionType.METHOD;
        	} else {
        		functionType = FunctionType.USER_METHOD;
        	}
        }

        if (methodType == null) {
            env.addError(marker, "UnknownInvoke",
                    methodname,
                    arguments.getTypes().length,
                    tartype
            );
        } else if (!methodType.checkType(arguments.getTypes())) {
            env.addError(marker, "ParameterType",
                    methodType,
                    arguments
            );
        } else if (methodType != null) {
        	type = methodType.getReturnType();
        	
        } else {
        	env.addError(marker, "MethodNotFound", methodname);
        }
    }

    @Override
    public String toString() {
        if (target != null) {
            return target + "." + methodname + arguments;
        } else {
            return methodname + arguments;
        }
    }
    @Override
    public String getLabel() {
        if (target != null) {
            return createText("InvokeA.Label", getLabelLeft(target), methodname, arguments.getLabel());
        } else {
            return createText("InvokeB.Label", methodname, arguments.getLabel());
        }
    }    
}
