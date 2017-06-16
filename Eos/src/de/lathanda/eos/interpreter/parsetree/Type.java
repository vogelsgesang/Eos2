package de.lathanda.eos.interpreter.parsetree;

import de.lathanda.eos.common.interpreter.AutoCompleteInformation;
import de.lathanda.eos.common.interpreter.AutoCompleteType;
import de.lathanda.eos.interpreter.MType;
import de.lathanda.eos.interpreter.ReservedVariables;
import de.lathanda.eos.spi.AutoCompleteEntry;


import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Basis Typdefinition.
 * Diese Klasse enthält alle Informationen zu den Eos Typen. 
 *
 */
public abstract class Type implements Comparable<Type>, AutoCompleteType {
	protected Type superType;
	// properties
	protected final String id;
	protected final String description;
	
	protected final TreeMap<String, MethodType> methods = new TreeMap<>();
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
	public Type mergeTypes(Type right) {
		if (this == right) {
			return this;
		} else if (right != null) {
			if (this == INTEGER && right == REAL || this == REAL && right == INTEGER) {
				return REAL;
			} else {
				return mergeTypes(right.superType);
			}
		} else {
			return UNKNOWN;
		}
	}

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
	
	/**
	 * Liefert die Methode zur Signature.
	 * Die Signature ist der Name gefolgt von der Parameterzahl in Klammern
	 * ZB verschieben(2)
	 * @param originalName Name in lokaler Sprache
	 * @param args Anzahl Parameter
	 * @return
	 */
	public MethodType getMethod(String originalName, int args) {
		String key = ReservedVariables.createSignature(originalName, args);

		if (methods.containsKey(key)) {
			return methods.get(key);
		} else  if (superType != null) {
			return superType.getMethod(originalName, args);
		} else {
			return null;
		}
	}


	
	public boolean inherits(Type b) {
		if (b == this) {
			return true;
		} else if (superType != null) {
			return superType.inherits(b);
		} else {
			return false;
		}
	}	

	public LinkedList<Type> getTypeList() {
		LinkedList<Type> typelist = new LinkedList<Type>();
		Type act = this;
		while (act != null) {
			typelist.add(act);
			act = act.superType;
		}
		return typelist;
	}	

	public LinkedList<AutoCompleteInformation> getAutoCompletes() {
		LinkedList<AutoCompleteInformation> completes = new LinkedList<>();
		fillAutoCompletes(completes);
		return completes;
	}
	protected void fillAutoCompletes(LinkedList<AutoCompleteInformation> list) {
		list.addAll(autocompletes);
		if (superType != null) {
			superType.fillAutoCompletes(list);
		}
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
	public abstract MethodType getReadProperty(String name);
	public abstract MethodType getAssignProperty(String name);
}
