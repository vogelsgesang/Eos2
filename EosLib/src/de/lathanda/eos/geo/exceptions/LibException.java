package de.lathanda.eos.geo.exceptions;

import java.util.ResourceBundle;

public class LibException extends RuntimeException {
	private static final long serialVersionUID = 5681484621769034552L;
	private static final ResourceBundle errorMessage = ResourceBundle.getBundle("text.liberror");
	private final String localMessage;

	public LibException(String errorID) {
		localMessage = errorMessage.getString(errorID);
	}

	@Override
	public String getLocalizedMessage() {
		return localMessage;
	}
}
