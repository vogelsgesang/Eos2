package de.lathanda.eos.interpreter.parsetree;

import java.util.ArrayList;
import java.util.TreeMap;
import de.lathanda.eos.common.interpreter.AutoCompleteInformation;
import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.ReservedVariables;
import de.lathanda.eos.interpreter.commands.LoadVariable;
import de.lathanda.eos.interpreter.exceptions.CyclicStorageException;
import de.lathanda.eos.interpreter.exceptions.DoubleMethodDeclarationException;
import de.lathanda.eos.interpreter.exceptions.DoublePropertyDeclarationException;

/**
 * Speichert und behandelt Benutzerdefinierte Klassen
 *
 * @author Peter (Lathanda) Schneider
 */
public class UserClass extends MarkedNode {
	private final TreeMap<String, Method> meths  = new TreeMap<>();
	private final TreeMap<String, Property> props = new TreeMap<>();
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
	public void addProperty(Property prop) throws DoublePropertyDeclarationException {
		for (String name:prop.getNames()) {
			if (props.containsKey(name)) {
				throw new DoublePropertyDeclarationException(name, this.name);
			}					
			props.put(name, prop);
		}
	}

	public void addMethod(Method meth) throws DoubleMethodDeclarationException {
		if (meths.containsKey(meth.getSignature())) {
			throw new DoubleMethodDeclarationException(meth.getSignature(), this.name);
		}		
		meths.put(meth.getSignature(), meth);
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
		self.ut.compile();
	}
	
	public void resolveNamesAndTypes(Environment env) {
		env.storeVariables();
		for(Property p:props.values()) {
			p.resolveNamesAndTypes(self, env);
		}
		for(Method m:meths.values()) {
			env.setVariableType(ReservedVariables.SELF, self.ut);
			env.restoreVariables();
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
			for (Method m : meths.values()) {
				ut.addMethod(m);
			}
			for (Property p : props.values()) {
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
