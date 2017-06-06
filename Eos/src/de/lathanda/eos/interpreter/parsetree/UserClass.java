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

/**
 * Speichert und behandelt Benutzerdefinierte Klassen
 *
 * @author Peter (Lathanda) Schneider
 */
public class UserClass extends MarkedNode {
	private final LinkedList<SubRoutine> meths  = new LinkedList<>();
	private final LinkedList<Declaration> props = new LinkedList<>();
	private final String name;
	private String sup;
	private Self self;

	public UserClass(String name) {
		this.name = name;
		sup = null;
		self = new Self();		
		type = self.getType();
	}
	public void setSuperClass(String name) {
		sup = name;
	}
	public void addDeclaration(Declaration prop) {
		props.add(prop);		
	}

	public void addMethod(SubRoutine meth) {
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

	public void compile() throws Exception {
		//create class
		MClass cls = self.ut.getMClass();
		//generate constructor
		ArrayList<Command> initCmd = new ArrayList<>();
		for(Declaration decl: props) {
			for(String name:decl.getNames()) {
				cls.addProperty(name, decl.getType().getMType());
			}
			decl.compile(initCmd, false);
			//create set get methods
			compileProperty(cls, name, decl.getType().getMType());
		}
		MProcedure init = new MProcedure(ReservedVariables.INIT_PROCEDURE, initCmd, false);		
		cls.addMethod(init);
		//generate methods
		for(SubRoutine sub: meths ) {
			ArrayList<Command> methCmd = new ArrayList<>();
			//store self
			methCmd.add(new DeclareVariable(ReservedVariables.SELF, cls));			
			methCmd.add(new StoreVariable(ReservedVariables.SELF));
			//compile method
			sub.compile(methCmd,  false);
			MProcedure meth = new MProcedure(sub.getSignature(), methCmd, false);
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
		env.setVariableType(ReservedVariables.SELF, self.ut);
		for(Declaration decl:props) {
			decl.resolveNamesAndTypes(self, env);
		}
		for(SubRoutine sub:meths) {
			sub.resolveNamesAndTypes(self, env);
		}	
	}

	public AutoCompleteInformation getAutoComplete() {
		return new AutoCompleteInfo(name, self.getType());
	}
	private class Self extends Expression {
		private UserType ut;
		public Self() {
			ut = new UserType(UserClass.this.name);
			type = ut;
		}
		public void bind(Environment env) {
			ut.setSuperclass(sup);		
			for (SubRoutine s : meths) {
				ut.addMethod(s);
			}
			for (Declaration d : props) {
				ut.addProperty(d);
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
}
