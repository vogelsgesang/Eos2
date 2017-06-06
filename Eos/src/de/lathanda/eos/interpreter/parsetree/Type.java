package de.lathanda.eos.interpreter.parsetree;

import de.lathanda.eos.common.interpreter.AutoCompleteInformation;
import de.lathanda.eos.common.interpreter.AutoCompleteType;
import de.lathanda.eos.interpreter.MType;
import de.lathanda.eos.spi.AutoCompleteEntry;


import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Basis Typdefinition.
 * Diese Klasse enthält alle Informationen zu den Eos Typen. 
 *
 */
public abstract class Type implements Comparable<Type>, AutoCompleteType {
	protected final LinkedList<Type> inherits = new LinkedList<>();
	// properties
	protected final String id;
	protected final String description;
	
	protected final TreeMap<String, MethodType> methods = new TreeMap<>();
	protected final TreeMap<String, MethodType> getProperty = new TreeMap<>();
	protected final TreeMap<String, MethodType> setProperty = new TreeMap<>();	
	protected final LinkedList<AutoCompleteEntry> autocompletes = new LinkedList<>();
	
	protected static SystemType VOID;
	protected static SystemType UNKNOWN;	
	protected static SystemType INTEGER;
	protected static SystemType REAL;
	protected static SystemType BOOLEAN;
	protected static SystemType STRING;	
	protected static SystemType FILLSTYLE;
	protected static SystemType LINESTYLE;
	protected static SystemType COLOR;
	protected static SystemType ALIGNMENT;	
	
	protected Type(String id, String description) {
		this.id = id;
		this.description = description;
	}
	
	public boolean checkType(Type right) {
		return !mergeTypes(right).isUnknown();
	}

	public boolean isNumber() {
		return this == INTEGER || this == REAL;
	}

	public boolean isBoolean() {
		return this == BOOLEAN;
	}

	public boolean isVoid() {
		return this == VOID;
	}

	public boolean isInteger() {
		return this == INTEGER;
	}	
	public boolean isUnknown() {
		return this == UNKNOWN;
	}
	public abstract Type mergeTypes(Type right);

	public static SystemType getVoid() {
		return VOID;
	}
	public static SystemType getUnknown() {
		return UNKNOWN;
	}	
	public static SystemType getInteger() {
		return INTEGER;
	}

	public static SystemType getBoolean() {
		return BOOLEAN;
	}

	public static SystemType getDouble() {
		return REAL;
	}

	public static SystemType getString() {
		return STRING;
	}	
	
	public static SystemType getColor() {
		return COLOR;
	}

	public static SystemType getLineStyle() {
		return LINESTYLE;
	}

	public static SystemType getFillStyle() {
		return FILLSTYLE;
	}

	public static SystemType getAlignment() {
		return ALIGNMENT;
	}	
	public void registerAutoCompleteEntry(AutoCompleteEntry ace) {
		autocompletes.add(ace);
	}	
	public void registerMethod(MethodType mt) {
		methods.put(mt.getSignature(), mt);
	}

	public void registerReadProperty(MethodType mt) {
		getProperty.put(mt.originalName, mt);
	}

	public void registerAssignProperty(MethodType mt) {
		setProperty.put(mt.originalName, mt);
	}	
	/**
	 * Liefert die Methode zur Signature.
	 * Die Signature ist der Name gefolgt von der Parameterzahl in Klammern
	 * ZB verschieben(2)
	 * @param originalName Name in lokaler Sprache
	 * @param args Anzahl Parameter
	 * @return
	 */
	public MethodType getMethod(String originalName, int args) {
		String key = MethodType.createSignature(originalName, args);

		for (Type t : inherits) {
			if (t.methods.containsKey(key)) {
				return t.methods.get(key);
			}
		}
		return null;
	}

	/**
	 * Liefert die Methode, um ein Attribut zu setzen.
	 * @param name Attributname
	 * @return
	 */
	public MethodType getAssignProperty(String name) {
		String key = name.toLowerCase();
		for (Type t : inherits) {
			if (t.setProperty.containsKey(key)) {
				return t.setProperty.get(key);
			}
		}
		return null;
	}

	/**
	 * Liefert die Methode, um ein Attribut zu lesen.
	 * @param name Attributname
	 * @return
	 */
	public MethodType getReadProperty(String name) {
		String key = name.toLowerCase();
		for (Type t : inherits) {
			if (t.getProperty.containsKey(key)) {
				return t.getProperty.get(key);
			}
		}
		return null;
	}
	
	public boolean inherits(Type b) {
		return inherits.contains(b);
	}	

	public LinkedList<Type> getTypeList() {
		return inherits;
	}	

	public LinkedList<AutoCompleteInformation> getAutoCompletes() {
		LinkedList<AutoCompleteInformation> completes = new LinkedList<>();
		for (Type t : inherits) {
			completes.addAll(t.autocompletes);
		}
		return completes;
	}
	
	@Override
	public int compareTo(Type b) {
		return this.id.compareTo(b.id);
	}
	@Override
	public String toString() {
		return description;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return description;
	}
	public abstract MType getMType();
	public abstract boolean isAbstract();
}
