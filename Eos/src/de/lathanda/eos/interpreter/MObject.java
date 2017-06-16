package de.lathanda.eos.interpreter;

import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.TreeMap;

import de.lathanda.eos.base.Readout;
import de.lathanda.eos.interpreter.parsetree.Property.Signature;

/**
 * Benutzerdefiniertes Objekt.
 * @author Peter (Lathanda) Schneider
 *
 */
public class MObject implements Readout {
	private final TreeMap<Signature, Variable> properties;
	private final MClass cls;
	private final Object javaObject;
	public MObject(MClass mClass, Machine m) throws Exception {
		properties = mClass.createProperties(m);
		cls = mClass;
		javaObject = mClass.createJavaObject(m);
	}

	public MClass getType() {
		return cls;
	}
	public void setProperty(Signature s, Object value) {
		properties.get(s).set(value);
	}
	public Object getProperty(Signature s) {
		return properties.get(s);
	}
	public Object getJavaObject() {
		return javaObject;
	}

	@Override
	public void getAttributes(LinkedList<Attribut> attributes) {
		for(Entry<Signature, Variable> e:properties.entrySet()) {
			attributes.add(new Attribut(e.getKey().name, e.getValue().get()));
		}		
	}

	@Override
	public boolean translate() {
		return false;
	}
	
}
