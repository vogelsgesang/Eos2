package de.lathanda.eos.interpreter;

import java.util.TreeMap;

/**
 * Benutzerdefiniertes Objekt.
 * @author Peter (Lathanda) Schneider
 *
 */
public class MObject {
	private final TreeMap<String, Variable> properties;
	private final MType cls;
	private final Object javaObject;
	public MObject(MType mClass) throws Exception {
		properties = mClass.createProperties();
		cls = mClass;
		javaObject = mClass.createJavaObject();
	}

	public MType getType() {
		return cls;
	}
	public void setProperty(String name, Object value) {
		properties.get(name).set(value);
	}
	public Object getProperty(String name) {
		return properties.get(name);
	}
	public Object getJavaObject() {
		return javaObject;
	}
}
