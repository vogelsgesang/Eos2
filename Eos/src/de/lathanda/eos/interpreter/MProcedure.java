package de.lathanda.eos.interpreter;

import java.util.ArrayList;
import java.util.Arrays;

public class MProcedure {
    private final String signature;
    private final Command[] ops;
    private final boolean globalAccess;
    public MProcedure(String signature, ArrayList<Command> ops, boolean globalAccess) {
    	this.signature = signature;
    	this.ops = new Command[ops.size()];
    	ops.toArray(this.ops);
    	this.globalAccess = globalAccess;
    }

    public MProcedure(String signature, Command[] ops, boolean globalAccess) {
    	this.signature = signature;
    	this.ops = ops;
    	this.globalAccess = globalAccess;
    }
    public String getSignature() {
        return signature;
    }
	public Command[] getOps() {
		return ops;
	}
	public boolean getGlobalAccess() {
		return globalAccess;
	}    
	public void prepare(Machine m) {
    	for(Command command : ops) {
    		command.prepare(m);
    	}		
	}
	@Override
	public String toString() {
		return "MProcedure{" + signature + ", " + Arrays.toString(ops) + ", " + globalAccess + "}";
	}
}
