package de.lathanda.eos.interpreter.parsetree;

import java.util.LinkedList;
import java.util.TreeMap;

import de.lathanda.eos.common.interpreter.AutoCompleteInformation;
import de.lathanda.eos.interpreter.MClass;
import de.lathanda.eos.interpreter.MType;

import static de.lathanda.eos.interpreter.ReservedVariables.*;

public class UserType extends Type {
	private String supCls = null;
	private Type supType = null;
	private TreeMap<String, SubRoutine> usermethods = new TreeMap<>();
	private TreeMap<String, Declaration> userproperties = new TreeMap<>();
	private boolean isAbstract = false;
	private MClass mtype;
	public UserType(String id) {
		super(id, id);
		inherits.add(this);
		mtype = new MClass(id);
	}
	public void setSuperclass(String sup) {
		this.supCls = sup;
	}
	public void bind(Environment env) {
		if (supType == null && supCls != null) {
			supType = env.getProgram().getType(supCls);
			if (supType instanceof UserType) {
				((UserType)supType).bind(env);
			}
			mtype.setSuper(supType.getMType());
			inherits.addAll(supType.inherits);
		}
		for (SubRoutine s : usermethods.values()) {
			s.createMethodType(env);
			methods.put(s.getSignature(), s.getMethodType(env));			
		}
		for (Declaration s : userproperties.values()) {
			for(String name:s.getNames()) {
				getProperty.put(name, new MethodType(GET_PREFIX, new Type[]{}, s.getType()));
				setProperty.put(name, new MethodType(SET_PREFIX, new Type[]{s.getType()}, SystemType.VOID));
			}					
		}
	}
	@Override
	public LinkedList<AutoCompleteInformation> getAutoCompletes() {
		LinkedList<AutoCompleteInformation> aci = (supCls == null)?new LinkedList<>():supType.getAutoCompletes();
		for(Declaration p:userproperties.values()) {
			aci.add(p.getAutoComplete());
		}
		for(SubRoutine m:usermethods.values()) {
			aci.add(m.getAutoComplete());
		}
		return aci;
	}

	@Override
	public Type mergeTypes(Type right) {
		if (right instanceof UserType) {
			UserType r = (UserType)right;
			while (r != null) {
				if (r == this) {
					return this;
				} else {
					r = (r.supType instanceof UserType)?(UserType)r.supType:null;
				}
			}
		}
		return Type.UNKNOWN;
	}

	@Override
	public MType getMType() {
		return mtype;
	}

	@Override
	public boolean isAbstract() {
		return isAbstract;
	}

	public void addProperty(Declaration prop) {
		for(String name:prop.getNames()) {
			userproperties.put(name, prop);
		}		
	}

	public void addMethod(SubRoutine meth) {
		usermethods.put(meth.getName(), meth);
	}

	public MClass getMClass() {
		return mtype;
	}
}
