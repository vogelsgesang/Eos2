package de.lathanda.eos.ev3.exception;

import java.util.ResourceBundle;

public class ConnectionLostException extends RuntimeException {
	private static final long serialVersionUID = -3244348805460902833L;
	private static final ResourceBundle gui = ResourceBundle.getBundle("lejos.gui");	
	public ConnectionLostException() {
		super(gui.getString("ConnectionLost"));
	}
}
