package de.lathanda.eos.interpreter.parsetree;

import de.lathanda.eos.interpreter.Command;
import java.util.ArrayList;

import de.lathanda.eos.interpreter.commands.DebugPoint;

/**
 * Speichert und behandelt einen Aufruf einer Methode, Funktion oder
 * benutzerdefinierten Funktion.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Invoke extends Expression {
    private Expression target;
    private final String methodname;
    private final Arguments arguments;
    private MethodType methodType;

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
    public void compile(ArrayList<Command> ops, boolean autowindow) throws Exception {
        if (methodType == null) {
            return;
        }
        arguments.compile(ops, autowindow);
        ops.add(new DebugPoint(marker));
        methodType.compile(ops, target, autowindow);
    }

    @Override
    public void resolveNamesAndTypes(Expression with, Environment env) {
        arguments.resolveNamesAndTypes(with, env);
        if (target == null) {
        	//Systemfunktion
            methodType = SystemFunctionType.getSystemFunction(methodname, arguments.getTypes().length);
            if (methodType != null) {
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
                        target.resolveNamesAndTypes(null, env);
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
