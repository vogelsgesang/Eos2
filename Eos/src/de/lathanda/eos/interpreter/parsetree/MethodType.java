package de.lathanda.eos.interpreter.parsetree;

import java.util.ArrayList;
import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.MType;

/**
 * Methodendefinition.
 *
 * @author Peter (Lathanda) Schneider
 */
public interface MethodType {

    public String getSignature();
    public boolean checkType(Type[] args);
    public Type getReturnType();
    public Type getParameterType(int i);

	public MType[] getParameters();
	public void compile(ArrayList<Command> ops, Expression target, boolean autowindow) throws Exception;
}
