package de.lathanda.eos.interpreter;

import java.util.TreeMap;

import de.lathanda.eos.interpreter.exceptions.TypeMissMatchException;
import de.lathanda.eos.interpreter.parsetree.Property.Signature;
import de.lathanda.eos.interpreter.parsetree.SystemType.ObjectSource;

/**
 * Informationen über eine Java Klasse
 * @author Peter (Lathanda) Schneider
 *
 */
public class MJavaClass implements MType {
	public final static MType BASE = new MJavaClass("base", Object.class, () -> {return null;});
	private final Class<?> cls;
	private final ObjectSource objSrc;
	private final String name;
	public MJavaClass(String name, Class<?> cls, ObjectSource objSrc) {
		this.cls = cls;
		this.objSrc = objSrc;
		this.name = name;
	}
	@Override
	public Object checkAndCast(Object obj) {
		return checkAndCast(cls, obj);
	}

	private Object checkAndCast(Class<?> c, Object obj) {
		if (obj instanceof Integer && c == double.class) {
			return ((Integer) obj).doubleValue();
		} else if (obj instanceof Double && c == int.class) {
			return ((Double) obj).intValue();
		} else if (obj instanceof Integer && c == int.class) {
			return (Integer) obj;
		} else if (obj instanceof Boolean && c == boolean.class) {
			return (Boolean) obj;
		} else if (obj instanceof Double && c == double.class) {
			return (Double) obj;
		} else if (obj instanceof MObject) {
			return checkAndCast(c, ((MObject)obj).getJavaObject());
		} else if (c.isInstance(obj)) {
			return obj;
		} else if (c == String.class) {
			return obj.toString();
		}
		// wrong type that cannot be converted to fit
		throw new TypeMissMatchException(c.toString(), obj.getClass().toString());
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isAbstract() {
		return objSrc == null;
	}

	@Override
	public Object newInstance(Machine m) throws InstantiationException, IllegalAccessException {
		if (objSrc != null) {
			return objSrc.createObject();
		} else {
			return null;
		}
	}
	public Object createJavaObject(Machine m) throws Exception {
		return newInstance(m);
	}
	public TreeMap<Signature, Variable> createProperties(Machine m) {
		return new TreeMap<>();
	}

}
