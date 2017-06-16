package de.lathanda.eos.interpreter;

import java.util.Map.Entry;
import java.util.TreeMap;

import de.lathanda.eos.interpreter.exceptions.TypeMissMatchException;
import de.lathanda.eos.interpreter.parsetree.Property.Signature;

/**
 * Informationen über eine Benutzerdefinierte Klasse
 * @author Peter (Lathanda) Schneider
 *
 */
public class MClass implements MType {
	private String name;
	private TreeMap<String, MProcedure> methods;
	private TreeMap<String, MType> properties;
	private MType sup;
	private boolean isAbstract;
	public MClass(String name) {
		this(name, MJavaClass.BASE);
	}
	public MClass(String name, MType sup) {
		methods = new TreeMap<>();
		properties = new TreeMap<>();
		this.sup = sup;
		this.name = name;
		isAbstract = false;
	}
	public void addMethod(MProcedure m) {	
		methods.put(m.getSignature(), m);
	}
	public MProcedure getMethod(String signature) {
		return methods.get(signature);
	}	
	public void addProperty(String name, MType t) {
		properties.put(name,  t);
	}
	@Override
	public Object checkAndCast(Object obj) {
		if (obj instanceof MObject) {
			MType objType = ((MObject)obj).getType();
			while (objType instanceof MClass) {
				if (objType == this) {
					return obj;
				}
				objType = ((MClass)objType).sup;
			}
			throw new TypeMissMatchException(name, ((MObject)obj).getType().toString());
		} else if (sup != null) {
			return sup.checkAndCast(obj);
		} else {
			if (obj instanceof MObject) {
				throw new TypeMissMatchException(name, ((MObject)obj).getType().getName());
			} else {
				throw new TypeMissMatchException(name, obj.getClass().toString());
			}
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isAbstract() {
		return isAbstract;
	}

	@Override
	public Object newInstance(Machine m) throws Exception {
		return new MObject(this, m);
	}

	public TreeMap<Signature, Variable> createProperties(Machine m) throws Exception {
		TreeMap<Signature, Variable> prop = sup.createProperties(m);
		for(Entry<String, MType> p:properties.entrySet()) {
			Signature s = new Signature(p.getKey(), this.name);
			prop.put(s, m.createInitVariable(p.getKey(), p.getValue()));
		}
		return prop;		
	}

	public Object createJavaObject(Machine m) throws Exception {
		return sup.createJavaObject(m);
	}
	public void setSuper(MType sup) {
		this.sup = sup;
	}	
}
