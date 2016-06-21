package de.lathanda.eos.interpreter;

import java.lang.reflect.Method;
import java.util.TreeMap;

/**
 * Methodendefinition.
 * Speichert alle Daten die zum behandeln von Methodem,
 * Funktionen und benutzerdefinierten Funktionen notwendig sind.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.4
 */
public class MethodType {
	private static TreeMap<String, MethodType> systemFunctions = new TreeMap<>();
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
    public MethodType(Type target, Type[] parameters, Type ret, String name, String originalName) throws NoSuchMethodException {
		this.parameters = parameters;
		Class<?>[] parametersClass = new Class<?>[parameters.length];
		for(int i = 0; i < parameters.length; i++) {
			parametersClass[i] = this.parameters[i].convertToClass();
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
    public MethodType(String name, Type[] parameters, Type ret) {
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
    public MethodType(Class<?> target, Type[] parameters, Type ret, String name, String originalName) throws NoSuchMethodException {
		this.parameters = parameters;
		Class<?>[] parametersClass = new Class<?>[parameters.length];
		for(int i = 0; i < parameters.length; i++) {
			parametersClass[i] = this.parameters[i].convertToClass();
		}
		this.method = target.getMethod(name, parametersClass);
		this.ret = ret;
		this.name = name;
		this.originalName = originalName;
		this.signature = createSignature(originalName, parameters.length);
	}    
    public static MethodType getSystemFunction(String originalName, int args) {
    	return systemFunctions.get(createSignature(originalName, args));
    }
    public static void registerSystemFunction(Class<?> target, Type[] parameters, Type ret, String name, String originalName ) throws NoSuchMethodException {
    	systemFunctions.put(
    			createSignature(originalName, parameters.length), 
    			new MethodType(target, parameters, ret, name, originalName)
    	);
    }
    public static String createSignature(String originalName, int args) {
    	return originalName.toLowerCase() + "(" + args + ")";
    }
    public String getSignature() {
        return signature;
    }
    public Method getMethod() {
    	return method;
    }
    public Object[] popArguments(Machine m) {
        Object[] args = new Object[parameters.length];
        for (int i = args.length; i-- > 0;) {
            args[i] = m.pop();
            args[i] = parameters[i].checkAndCast(args[i]);
        }
        return args;
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
        for(Type t: parameters) {
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
    
}
