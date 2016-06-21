package de.lathanda.eos.interpreter.parsetree;

import de.lathanda.eos.interpreter.Environment;
import de.lathanda.eos.interpreter.Type;

/**
 * Parameter.
 * 
 * @author Peter (Lathanda) Schneider
 * @since 0.4
 */
public class Parameter {
    private final String name;
    private final Type type;
    public Parameter(String name, Type parameterType) {
        this.name = name;
        this.type = parameterType;
    }

    public String getName() {
        return name;
    }

    public void registerParameter(Environment env) {
        env.setVariableType(name.toLowerCase(), type);
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return name + ":" + type;
    }
}
