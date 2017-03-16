package de.lathanda.eos.interpreter;

import java.util.Map.Entry;
import java.util.TreeMap;

import de.lathanda.eos.interpreter.exceptions.TypeMissMatchException;

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
	public void addMethod(String name, MProcedure m) {
		methods.put(name, m);
	}
	public MProcedure getMethod(String signature) {
		return methods.get(signature);
	}	
	public void addProperty(String name, MType t) {
		properties.put(name,  t);
	}
	@Override
	public Object checkAndCast(Object obj) {
		if (obj instanceof MObject && ((MObject)obj).getType() == this) {
			return obj;
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
	public Object newInstance() throws Exception {
		return new MObject(this);
	}
	@Override
	public TreeMap<String, Variable> createProperties() {
		TreeMap<String, Variable> prop = sup.createProperties();
		for(Entry<String, MType> p:properties.entrySet()) {
			prop.put(p.getKey(), new Variable(p.getValue(), p.getKey()));
		}
		return prop;
	}
	@Override
	public Object createJavaObject() throws Exception {
		return sup.createJavaObject();
	}	
}
