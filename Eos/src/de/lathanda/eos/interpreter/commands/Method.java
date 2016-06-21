package de.lathanda.eos.interpreter.commands;

import java.lang.reflect.InvocationTargetException;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.Machine;
import de.lathanda.eos.interpreter.MethodType;
import de.lathanda.eos.interpreter.exceptions.NullAccessException;

/**
 * 
 * Assembler Befehl: Methode aufrufen
 *
 * Stacklayout [..., para1, ... , paran, target]
 * @author Peter (Lathanda) Schneider
 */
public class Method extends Command {
    private final MethodType methodType;
    public Method(MethodType methodType) {
        this.methodType = methodType;
    }
    @Override
    public boolean execute(Machine m) throws Exception {
        Object target = m.pop();        
        Object[] args = methodType.popArguments(m);
        if (target == null) {
        	throw new NullAccessException();
        }
        java.lang.reflect.Method method = methodType.getMethod();
        try {
        	Object result = method.invoke(target, args);
        	if (result != null) {
        		m.push(result);
        	}
        } catch (InvocationTargetException ite) {
        	throw (Exception)ite.getTargetException();
        }
        return true;
    }   

    @Override
    public String toString() {
        return "Method{" + methodType + '}';
    }
    
}
