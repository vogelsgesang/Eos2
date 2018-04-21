package de.lathanda.eos.extension;

public interface Config {
	void setValue(String key, String value);
	String getValue(String key, String def);
	void addChangeListener(ChangeListener cl, String key);
	public static interface ChangeListener {
		void valueChanged(String key, String value);
	}
}
