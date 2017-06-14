package de.lathanda.eos.interpreter.parsetree;

import java.util.ArrayList;
import java.util.LinkedList;
import de.lathanda.eos.common.interpreter.AutoCompleteInformation;
import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.MClass;
import de.lathanda.eos.interpreter.MProcedure;
import de.lathanda.eos.interpreter.MType;
import de.lathanda.eos.interpreter.ReservedVariables;
import de.lathanda.eos.interpreter.commands.DeclareVariable;
import de.lathanda.eos.interpreter.commands.LoadProperty;
import de.lathanda.eos.interpreter.commands.LoadVariable;
import de.lathanda.eos.interpreter.commands.StoreProperty;
import de.lathanda.eos.interpreter.commands.StoreVariable;
import de.lathanda.eos.interpreter.exceptions.CyclicStorageException;

/**
 * Speichert und behandelt Benutzerdefinierte Klassen
 *
 * @author Peter (Lathanda) Schneider
 */
public class UserClass extends MarkedNode {
	private final LinkedList<Method> meths  = new LinkedList<>();
	private final LinkedList<Property> props = new LinkedList<>();
	private final String name;
	private String sup;
	private Self self;

	/** 
	 * @param name Name der Klasse
	 */
	public UserClass(String name) {
		this.name = name;
		sup = null;
		self = new Self();		
		type = self.getType();
	}
	public void setSuperClass(String name) {
		sup = name;
	}
	public void addProperty(Property prop) {
		props.add(prop);		
	}

	public void addMethod(Method meth) {
		meths.add(meth);
	}
	public String getName() {
		return name;
	}
	@Override
	public String getLabel() {
		return name;
	}
	public void bind(Environment env) {
		self.bind(env);
	}
	public void checkCyclicStorage() throws CyclicStorageException {
		self.ut.checkCyclicStorage();
	}

	public void compile() throws Exception {
		//create class
		MClass cls = self.ut.getMClass();
		//generate constructor
		ArrayList<Command> initCmd = new ArrayList<>();
		for(Property p: props) {
			for(String name:p.getNames()) {
				cls.addProperty(name, p.getType().getMType());
			}
			p.compile(initCmd, false);
			//create set get methods
			compileProperty(cls, name, p.getType().getMType());
		}
		MProcedure init = new MProcedure(ReservedVariables.INIT_PROCEDURE, initCmd, false);		
		cls.addMethod(init);
		//generate methods
		for(Method m: meths ) {
			ArrayList<Command> methCmd = new ArrayList<>();
			//store self
			methCmd.add(new DeclareVariable(ReservedVariables.SELF, cls));			
			methCmd.add(new StoreVariable(ReservedVariables.SELF));
			//compile method
			m.compile(methCmd,  false);
			MProcedure meth = new MProcedure(m.getSignature(), methCmd, false);
			cls.addMethod(meth);
		}
	}
	/**
	 * Create the setter and getter method for that property
	 * @param c Class containing the setter and getter
	 * @param name name of the property
	 * @param t type of the property
	 */
	private void compileProperty(MClass c, String name, MType t) {
		ArrayList<Command> getCmd = new ArrayList<>();
		ArrayList<Command> setCmd = new ArrayList<>();

		setCmd.add(new StoreProperty(name));
		getCmd.add(new LoadProperty(name));		
		
		MProcedure setter = new MProcedure(ReservedVariables.GET_PREFIX+name, getCmd, false);
		MProcedure getter = new MProcedure(ReservedVariables.SET_PREFIX+name, setCmd, false);
		c.addMethod(setter);	
		c.addMethod(getter);
	}
	
	public void resolveNamesAndTypes(Environment env) {
		env.storeVariables();
		env.setVariableType(ReservedVariables.SELF, self.ut);
		for(Property p:props) {
			p.resolveNamesAndTypes(self, env);
		}
		for(Method m:meths) {
			m.resolveNamesAndTypes(self, env);
		}
		env.restoreVariables();
	}

	private class Self extends Expression {
		private UserType ut;
		public Self() {
			ut = new UserType(UserClass.this.name);
			type = ut;
		}
		public void bind(Environment env) {
			ut.setSuperclass(sup);		
			for (Method m : meths) {
				ut.addMethod(m);
			}
			for (Property p : props) {
				ut.addProperty(p);
			}
			ut.bind(env);
		}
		@Override
		public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
			ops.add(new LoadVariable(ReservedVariables.SELF));			
		}

		@Override
		public void resolveNamesAndTypes(Expression with, Environment env) {
			//nothing to do
		}
		
		@Override
		public String getLabel() {
			return  createText("Class.Self");
		}		
	}

	public AutoCompleteInformation getAutoComplete() {
		return new AutoCompleteInfo(name, self.ut, AutoCompleteInformation.CLASS);
	}
}
