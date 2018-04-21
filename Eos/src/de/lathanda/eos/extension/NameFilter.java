package de.lathanda.eos.extension;

public interface NameFilter {
	public default String filterClass(String name) {
		return name;
	}
	public default String filterProperty(String name) {
		return name;
	}
	public default String filterMethod(String name) {
		return name;
	}
}
