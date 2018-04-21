package de.lathanda.eos.interpreter.parsetree;

import de.lathanda.eos.common.interpreter.AutoCompleteType;
import de.lathanda.eos.common.interpreter.MissingTypeException;
import de.lathanda.eos.extension.ObjectSource;
import de.lathanda.eos.interpreter.MJavaClass;
import de.lathanda.eos.interpreter.MType;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Typdefinition.
 * Diese Klasse enthält alle Informationen zu den Eos Typen. 
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.4
 */
public class SystemType extends Type implements Comparable<Type>, AutoCompleteType {
	protected final TreeMap<String, MethodType> getProperty = new TreeMap<>();
	protected final TreeMap<String, MethodType> setProperty = new TreeMap<>();	
	// type maps
	private static final TreeMap<String, SystemType> nameType = new TreeMap<>();
	private static final TreeMap<String, SystemType> idType = new TreeMap<>();
	protected static AutoCompleteType CLASS;
	// special types
	private static SystemType WINDOW;
	private static SystemType FIGURE;


	private final ObjectSource objSrc;
	private final Class<?> cls;


	private SystemType(String id, String description, ObjectSource objSrc, Class<?> cls) {
		super(id, description);
		this.objSrc = objSrc;
		this.cls = cls;
	}

	public static void registerType(String id, String[] names, String description, ObjectSource objSrc, Class<?> cls) {
		SystemType type = new SystemType(id, description, objSrc, cls);
		if (names != null) {
			for (String name : names) {
				nameType.put(name.trim().toLowerCase(), type);
			}
		}
		idType.put(id, type);
		switch (id) {
		case "integer":
			INTEGER = type;
			break;
		case "?":
			UNKNOWN = type;
			break;
		case "class":
			CLASS = type;
			break;
		case "#":
			VOID = type;
			break;
		case "boolean":
			BOOLEAN = type;
			break;
		case "real":
			REAL = type;
			break;
		case "string":
			STRING = type;
			break;
		case "color":
			COLOR = type;
			break;
		case "linestyle":
			LINESTYLE = type;
			break;
		case "fillstyle":
			FILLSTYLE = type;
			break;
		case "alignment":
			ALIGNMENT = type;
			break;
		case "window":
			WINDOW = type;
			break;
		case "figure":
			FIGURE = type;
			break;
		}
	}

	public static void registerSuper(String id, String superType) throws MissingTypeException {
		Type t = idType.get(id);
		if (t == null) {
			throw new MissingTypeException(id);
		}
		if (superType != null) {
			Type st = idType.get(superType);
			if (st == null) {
				throw new MissingTypeException(superType);
			}
			t.superType = st;
		} else {
			t.superType = null;
		}
	}

	// basic types
	public static SystemType getFigure() {
		return FIGURE;
	}

	public static SystemType getWindow() {
		return WINDOW;
	}

	public static AutoCompleteType getClassType() {
		return CLASS;
	}

	public static SystemType getInstanceByID(String id) {
		if (idType.containsKey(id)) {
			return idType.get(id);
		} else {
			return UNKNOWN;
		}
	}

	public static SystemType getInstanceByName(String eosname) {
		if (nameType.containsKey(eosname.toLowerCase())) {
			return nameType.get(eosname.toLowerCase());
		} else {
			return UNKNOWN;
		}
	}

	public static LinkedList<Type> getAll() {
		LinkedList<Type> allTypes = new LinkedList<>();
		for (Type t : nameType.values()) {
			allTypes.add(t);
		}
		return allTypes;
	}

	/**
	 * Liefert die Methode, um ein Attribut zu setzen.
	 * @param name Attributname
	 * @return
	 */
	public MethodType getAssignProperty(String name) {
		String key = name.toLowerCase();
		if (setProperty.containsKey(key)) {
			return setProperty.get(key);
		} else if (superType != null) {
			return superType.getAssignProperty(name);
		} else {
			return null;
		}
	}

	/**
	 * Liefert die Methode, um ein Attribut zu lesen.
	 * @param name Attributname
	 * @return
	 */
	public MethodType getReadProperty(String name) {
		String key = name.toLowerCase();
		if (getProperty.containsKey(key)) {
			return getProperty.get(key);
		} else if (superType != null) {
			return superType.getReadProperty(name);
		} else {
			return null;
		}
	}
	public void registerReadProperty(SystemMethodType mt) {
		getProperty.put(mt.originalName, mt);
	}

	public void registerAssignProperty(SystemMethodType mt) {
		setProperty.put(mt.originalName, mt);
	}	
	public Class<?> convertToClass() {
		return cls;
	}

	public boolean isAbstract() {
		return objSrc == null;
	}


	public MType getMType() {
		return new MJavaClass(description, cls, objSrc);
	}
}
