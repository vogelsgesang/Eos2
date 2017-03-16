package de.lathanda.eos.interpreter;

import java.util.TreeMap;

public interface MType {
	Object checkAndCast(Object obj);

	String getName();

	boolean isAbstract();

	Object newInstance() throws Exception;

	Object createJavaObject() throws Exception;

	TreeMap<String, Variable> createProperties();

}
