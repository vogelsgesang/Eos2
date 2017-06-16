package de.lathanda.eos.interpreter;

import java.util.TreeMap;

import de.lathanda.eos.interpreter.parsetree.Property.Signature;

public interface MType {
	Object checkAndCast(Object obj);

	String getName();

	boolean isAbstract();

	Object newInstance(Machine m) throws Exception;

	Object createJavaObject(Machine m) throws Exception;

	TreeMap<Signature, Variable> createProperties(Machine m) throws Exception;

}
