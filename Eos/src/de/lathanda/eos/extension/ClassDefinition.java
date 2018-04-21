package de.lathanda.eos.extension;

public class ClassDefinition {
	private final ObjectSource objSrc;
	private final String superClass;
	private final Class<?> javaClass;	
	private final String id;
	public ClassDefinition(String id, ObjectSource objSrc, String superClass, Class<?> javaClass) {
		this.id = id;
		this.objSrc = objSrc;
		this.superClass = superClass;
		this.javaClass = javaClass;
	}
	public ObjectSource getObjSrc() {
		return objSrc;
	}
	public String getSuperClass() {
		return superClass;
	}
	public Class<?> getJavaClass() {
		return javaClass;
	}
	public String getID() {
		return id;
	}
}
