package de.lathanda.eos.interpreter.parsetree;

import de.lathanda.eos.interpreter.Environment;
import de.lathanda.eos.interpreter.Type;
import java.util.LinkedList;

/**
 * Speichert und behandelt eine Parameterliste.
 * 
 * @author Peter (Lathanda) Schneider
 * @since 0.4
 */
public class Parameters {
    private final LinkedList<Parameter> parameters;
    private final LinkedList<String> nameBuffer;
    public Parameters() {
        parameters = new LinkedList<>();
        nameBuffer = new LinkedList<>();
    }
    public void append(Parameter parameter) {
        parameters.add(parameter);
    }
    public int size() {
        return parameters.size();
    }
    public String[] getParameters() {
        String[] para = new String[parameters.size()];
        for(int i = 0; i < para.length; i++) {
            para[i] = parameters.get(i).getName();
        }
        return para;
    }
    public Type[] getTypes() {
        Type[] para = new Type[parameters.size()];
        for(int i = 0; i < para.length; i++) {
            para[i] = parameters.get(i).getType();
        }        
        return para;
    }

    public void registerParameters(Environment env) {
        for (Parameter p : parameters) {
            p.registerParameter(env);
        }
    }

    public void addName(String name) {
        nameBuffer.add(name);
    }

    public void setType(Type type) {
        nameBuffer.stream().forEachOrdered(name -> append(new Parameter(name, type)));
        nameBuffer.clear();
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        boolean first = true;
        res.append("(");
        for (Parameter p : parameters) {
            if (first) {
                res.append(p);
            } else {
                res.append(",").append(p);
                first = false;
            }
        }
        res.append(")");
        return res.toString();
    }    
}
