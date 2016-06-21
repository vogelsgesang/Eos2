package de.lathanda.eos.interpreter.commands;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.Machine;
import de.lathanda.eos.interpreter.MethodType;
import java.lang.reflect.Method;

/**
 * 
 * Assembler Befehl: Globale Funktion aufrufen.
 * Eine Funktion der Klasse SystemFunctions wird aufgerufen.
 *
 * @author Peter (Lathanda) Schneider
 */public class Function extends Command {
    private final MethodType methodType;
    public Function(MethodType methodType) {
        this.methodType = methodType;
    }
    @Override
    public boolean execute(Machine m) throws Exception {
        Object[] args = methodType.popArguments(m);
        Method method = methodType.getMethod();
        m.push(method.invoke(null, args));
        return true;
    }

    @Override
    public String toString() {
        return "Function{" + methodType + '}';
    }
    
}
