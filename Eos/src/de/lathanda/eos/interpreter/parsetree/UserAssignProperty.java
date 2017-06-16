package de.lathanda.eos.interpreter.parsetree;

import java.util.ArrayList;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.MType;
import de.lathanda.eos.interpreter.commands.StoreProperty;

public class UserAssignProperty implements MethodType {
	private final UserType userType;
	private final Property property;
	private final String name;
	private final String signature;
	public UserAssignProperty(UserType userType, String name, Property property) {
		this.property  = property;
		this.userType  = userType;
		this.name      = name;
		this.signature = name + "(1)";
	}

	@Override
	public String getSignature() {
		return signature;
	}

	@Override
	public boolean checkType(Type[] types) {
		return types.length == 0;
	}

	@Override
	public Type getReturnType() {
		return Type.VOID;
	}

	@Override
	public Type getParameterType(int i) {
		if (i == 0) {
			return property.vartype;
		} else {
			return null;
		}
	}

	@Override
	public MType[] getParameters() {
		return new MType[] {};
	}

	@Override
	public void compile(ArrayList<Command> ops, Expression target, boolean autowindow) throws Exception {
		target.compile(ops, autowindow);
		ops.add(new StoreProperty(userType.getMClass(), name));
	}
}
