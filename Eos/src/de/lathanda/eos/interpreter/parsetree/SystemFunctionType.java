package de.lathanda.eos.interpreter.parsetree;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.TreeMap;

import de.lathanda.eos.common.interpreter.AutoCompleteType;
import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.MType;
import de.lathanda.eos.interpreter.commands.Function;

/**
 * Methodendefinition.
 * Speichert alle Daten die zum behandeln von Methoden notwendig sind.
 *
 * @author Peter (Lathanda) Schneider
 */
public class SystemFunctionType implements MethodType {
	private static TreeMap<String, SystemFunctionType> systemFunctions = new TreeMap<>();
    protected final Type[] parameters;
    protected final Type ret;
    protected final String name;
    protected final String originalName;
    protected final String signature;
    protected final Method method;
    /**
     * Erzeugt eine Java Methode
     * @param target
     * @param parameters
     * @param ret
     * @param name
     * @param originalName
     * @throws NoSuchMethodException
     */
    public SystemFunctionType(SystemType target, SystemType[] parameters, Type ret, String name, String originalName) throws NoSuchMethodException {
		this.parameters = parameters;
		Class<?>[] parametersClass = new Class<?>[parameters.length];
		for(int i = 0; i < parameters.length; i++) {
			parametersClass[i] = parameters[i].convertToClass();
		}
		this.method = target.convertToClass().getMethod(name, parametersClass);
		this.ret = ret;
		this.name = name;
		this.originalName = originalName;
		this.signature = createSignature(originalName, parameters.length);
	}
    /**
     * Erzeugt eine Userdefinierte Methode
     * @param name
     * @param parameters
     * @param ret
     */
    public SystemFunctionType(String name, Type[] parameters, Type ret) {
		this.parameters = parameters;		
		this.method = null;
		this.ret = ret;
		this.name = name;
		this.originalName = name;
		this.signature = createSignature(originalName, parameters.length);
    }
    /**
     * Erzeugt eine statische Java Methode
     * @param target
     * @param parameters
     * @param ret
     * @param name
     * @param originalName
     * @throws NoSuchMethodException
     */
    public SystemFunctionType(Class<?> target, SystemType[] parameters, Type ret, String name, String originalName) throws NoSuchMethodException {
		this.parameters = parameters;
		Class<?>[] parametersClass = new Class<?>[parameters.length];
		for(int i = 0; i < parameters.length; i++) {
			parametersClass[i] = parameters[i].convertToClass();
		}
		this.method = target.getMethod(name, parametersClass);
		this.ret = ret;
		this.name = name;
		this.originalName = originalName;
		this.signature = createSignature(originalName, parameters.length);
	}    
    public static SystemFunctionType getSystemFunction(String originalName, int args) {
    	return systemFunctions.get(createSignature(originalName, args));
    }
    public static void registerSystemFunction(Class<?> target, SystemType[] parameters, Type ret, String name, String originalName ) throws NoSuchMethodException {
    	systemFunctions.put(
    			createSignature(originalName, parameters.length), 
    			new SystemFunctionType(target, parameters, ret, name, originalName)
    	);
    }
    public static String createSignature(String originalName, int args) {
    	return originalName.toLowerCase() + "(" + args + ")";
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
        res.append(originalName).append("(");
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
		ops.add(new Function(getParameters(), method));
    }
}
