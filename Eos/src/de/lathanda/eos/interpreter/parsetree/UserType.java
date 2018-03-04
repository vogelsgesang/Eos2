package de.lathanda.eos.interpreter.parsetree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;
import de.lathanda.eos.common.interpreter.AutoCompleteInformation;
import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.MClass;
import de.lathanda.eos.interpreter.MProcedure;
import de.lathanda.eos.interpreter.MType;
import de.lathanda.eos.interpreter.ReservedVariables;
import de.lathanda.eos.interpreter.commands.DeclareVariable;
import de.lathanda.eos.interpreter.commands.StoreVariable;
import de.lathanda.eos.interpreter.exceptions.CyclicStorageException;

public class UserType extends Type {
	private String supCls = null;
	private TreeMap<String, Method> usermethods = new TreeMap<>();
	private TreeMap<String, Property> userproperties = new TreeMap<>();
	private boolean isAbstract = false;
	private MClass mtype;
	/**
	 * marker for cyclic storage check
	 */
	private boolean checked = false;
	private boolean marked = false;
	private boolean isUndefined = true;
	public UserType(String id) {
		super(id, id);
		mtype = new MClass(id);
	}
	public void setSuperclass(String sup) {
		this.supCls = sup;
	}
	public void bind(Environment env) {
		if (superType == null && supCls != null) {
			superType = env.getProgram().getType(supCls);
			if (superType instanceof UserType) {
				((UserType)superType).bind(env);
			}
			mtype.setSuper(superType.getMType());
		}
		for (Method s : usermethods.values()) {
			s.createMethodType(env);
			methods.put(s.getSignature(), s.getMethodType(env));			
		}
	}
	public void checkCyclicStorage() throws CyclicStorageException {
		if (checked) return; 
		checkCyclicStorageInternal();
	}
	private void checkCyclicStorageInternal() throws CyclicStorageException {
		if (marked) {
			throw new CyclicStorageException(id);
		}
		marked = true;
		checkCyclicStorageInternal(superType);
		for( Property prop : userproperties.values()) {
			checkCyclicStorageInternal(prop.getPropertyType());
		}
		checked = true;
		
	}
	private void checkCyclicStorageInternal(Type t) throws CyclicStorageException {
		if (t != null && t instanceof UserType) {
			((UserType)t).checkCyclicStorage();
		}
	}	
	@Override
	public LinkedList<AutoCompleteInformation> getAutoCompletes() {
		LinkedList<AutoCompleteInformation> aci = (supCls == null)?new LinkedList<>():superType.getAutoCompletes();
		for(Property p:userproperties.values()) {
			aci.addAll(p.getAutoCompletes());
		}
		for(Method m:usermethods.values()) {
			aci.add(m.getAutoComplete());
		}
		return aci;
	}

	@Override
	public MType getMType() {
		return mtype;
	}

	@Override
	public boolean isAbstract() {
		return isAbstract;
	}

	@Override
	public boolean isUnknown() {
		return isUndefined;
	}
	public void addProperty(Property prop) {
		for(String name:prop.getNames()) {
			userproperties.put(name, prop);
		}		
	}

	public void addMethod(Method meth) {
		usermethods.put(meth.getName(), meth);
	}

	public MClass getMClass() {
		return mtype;
	}
	public void compile() throws Exception {		
		//generate properties
		for(Property p: userproperties.values()) {
			for(String name:p.getNames()) {
				mtype.addProperty(name, p.getPropertyType().getMType());
			}
		}
		//generate methods
		for(Method m: usermethods.values() ) {
			ArrayList<Command> methCmd = new ArrayList<>();
			//store self
			methCmd.add(new DeclareVariable(ReservedVariables.SELF, mtype));			
			methCmd.add(new StoreVariable(ReservedVariables.SELF));
			//compile method
			m.compile(methCmd,  false);
			MProcedure meth = new MProcedure(m.getSignature(), methCmd, false);
			mtype.addMethod(meth);
		}	
	}

	@Override
	public MethodType getReadProperty(String name) {
		if (userproperties.containsKey(name)) {
			return new UserReadProperty(this, name, userproperties.get(name));
		} else if (superType != null){
			return superType.getReadProperty(name);
		} else {
			return null;
		}
	}
	@Override
	public MethodType getAssignProperty(String name) {
		if (userproperties.containsKey(name)) {
			return new UserAssignProperty(this, name, userproperties.get(name));
		} else if (superType != null){
			return superType.getAssignProperty(name);
		} else {
			return null;
		}
	}
	public void define() {
		isUndefined = false;		
	}
}
