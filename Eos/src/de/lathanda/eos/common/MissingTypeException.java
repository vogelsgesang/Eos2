package de.lathanda.eos.common;

public class MissingTypeException extends Exception {
	private static final long serialVersionUID = 3929985498165878438L;

	public MissingTypeException(String id) {
		super("missing type "+id);
	}
}
