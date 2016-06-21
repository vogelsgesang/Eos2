package de.lathanda.eos.interpreter;

import de.lathanda.eos.interpreter.exceptions.TypeMissMatchException;
import de.lathanda.eos.spi.AutoCompleteEntry;
import de.lathanda.eos.spi.MissingTypeException;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

/**
 * Typdefinition.
 * Diese Klasse enthält alle Informationen zu den Eos Typen. 
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.4
 */
public class Type implements Comparable<Type> {
	// type maps
	private static final TreeMap<String, Type> nameType = new TreeMap<>();
	private static final TreeMap<String, Type> idType = new TreeMap<>();
	// special types
	private static Type UNKNOWN;
	private static Type VOID;
	private static Type CLASS;
	private static Type INTEGER;
	private static Type REAL;
	private static Type BOOLEAN;
	private static Type STRING;
	private static Type FILLSTYLE;
	private static Type LINESTYLE;
	private static Type COLOR;
	private static Type ALIGNMENT;
	private static Type WINDOW;
	private static Type FIGURE;

	// properties
	private final String id;
	private final String description;
	private final LinkedList<Type> inherits = new LinkedList<>();
	private final LinkedList<AutoCompleteEntry> autocompletes = new LinkedList<>();
	private final ObjectSource objSrc;
	private final Class<?> cls;
	private final TreeMap<String, MethodType> methods = new TreeMap<>();
	private final TreeMap<String, MethodType> getProperty = new TreeMap<>();
	private final TreeMap<String, MethodType> setProperty = new TreeMap<>();

	private Type(String id, String description, ObjectSource objSrc, Class<?> cls) {
		this.id = id;
		this.description = description;
		this.objSrc = objSrc;
		this.cls = cls;
	}

	public static void registerType(String id, String[] names, String description, ObjectSource objSrc, Class<?> cls) {
		Type type = new Type(id, description, objSrc, cls);
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

	public static void registerInherits(String id, String[] inherits) throws MissingTypeException {
		Type t = idType.get(id);
		if (t == null) {
			throw new MissingTypeException(id);
		}
		for (String sup : inherits) {
			Type st = idType.get(sup);
			if (st == null) {
				throw new MissingTypeException(sup);
			}
			t.inherits.add(st);
		}
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

	// basic types
	public static Type getInteger() {
		return INTEGER;
	}

	public static Type getBoolean() {
		return BOOLEAN;
	}

	public static Type getDouble() {
		return REAL;
	}

	public static Type getString() {
		return STRING;
	}

	public static Type getColor() {
		return COLOR;
	}

	public static Type getLineStyle() {
		return LINESTYLE;
	}

	public static Type getFillStyle() {
		return FILLSTYLE;
	}

	public static Type getAlignment() {
		return ALIGNMENT;
	}

	public static Type getUnknown() {
		return UNKNOWN;
	}

	public static Type getVoid() {
		return VOID;
	}

	public static Type getFigure() {
		return FIGURE;
	}

	public static Type getWindow() {
		return WINDOW;
	}

	public static Type getClassType() {
		return CLASS;
	}

	public static Type getInstanceByID(String id) {
		if (idType.containsKey(id)) {
			return idType.get(id);
		} else {
			return UNKNOWN;
		}
	}

	public static Type getInstanceByName(String eosname) {
		if (nameType.containsKey(eosname.toLowerCase())) {
			return nameType.get(eosname.toLowerCase());
		} else {
			return UNKNOWN;
		}
	}

	public Object newInstance() throws InstantiationException, IllegalAccessException {
		if (objSrc != null) {
			return objSrc.createObject();
		} else {
			return null;
		}
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

	public Type mergeTypes(Type right) {
		if (this == right) {
			return this;
		} else {
			if (right.inherits.contains(this)) {
				if (this == INTEGER && right == REAL) {
					return REAL;
				} else {
					return this;
				}
			} else {
				return UNKNOWN;
			}
		}
	}

	public LinkedList<Type> getTypeList() {
		return inherits;
	}

	public boolean checkType(Type right) {
		return !mergeTypes(right).isUnknown();
	}

	public Object checkAndCast(Object obj) {
		return checkAndCast(convertToClass(), obj);
	}

	public boolean inherits(Type b) {
		return inherits.contains(b);
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
		} else if (c.isInstance(obj)) {
			return obj;
		} else if (c == String.class) {
			return obj.toString();
		}
		// wrong type that cannot be converted to fit
		throw new TypeMissMatchException(c, obj.getClass());
	}

	public Class<?> convertToClass() {
		return cls;
	}

	public boolean isNumber() {
		return this == INTEGER || this == REAL;
	}

	public boolean isBoolean() {
		return this == BOOLEAN;
	}

	public boolean isUnknown() {
		return this == UNKNOWN;
	}

	public boolean isVoid() {
		return this == VOID;
	}
	
	public boolean isInteger() {
		return this == INTEGER;
	}
	
	public boolean isFigure() {
		return inherits.contains(FIGURE);
	}

	public boolean isAbstract() {
		return objSrc == null;
	}

	public boolean isWindow() {
		return this == WINDOW;
	}

	@Override
	public String toString() {
		return description;
	}

	public String getName() {
		return description;
	}

	public String getId() {
		return id;
	}

	public interface ObjectSource {
		public Object createObject();
	}

	@Override
	public int compareTo(Type b) {
		return this.id.compareTo(b.id);
	}

	public List<AutoCompleteEntry> getAutoCompletes() {
		LinkedList<AutoCompleteEntry> completes = new LinkedList<>();
		for (Type t : inherits) {
			completes.addAll(t.autocompletes);
		}
		return completes;
	}

	public void registerAutoCompleteEntry(AutoCompleteEntry ace) {
		autocompletes.add(ace);

	}

}
