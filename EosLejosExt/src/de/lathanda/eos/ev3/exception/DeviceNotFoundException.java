package de.lathanda.eos.ev3.exception;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class DeviceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 8367740623206215320L;
	private static final ResourceBundle gui = ResourceBundle.getBundle("lejos.gui");
    private final String addr;
	public DeviceNotFoundException(String addr) {		
		this.addr = addr;
	}	

    @Override
    public String getLocalizedMessage() {
    	String msg = gui.getString("DeviceNotFound");
        return MessageFormat.format(msg, addr);
    }   
}
