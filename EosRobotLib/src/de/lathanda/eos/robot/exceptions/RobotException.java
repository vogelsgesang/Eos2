package de.lathanda.eos.robot.exceptions;
/**
 * Etwas ist schief gegangen.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */

import java.util.ResourceBundle;

public abstract class RobotException extends Exception {
	private static final long serialVersionUID = 6166388499569040612L;
	private static final ResourceBundle errorMessage = ResourceBundle.getBundle("text.robot_error");
	private final String localMessage;
	public RobotException(String errorID) {
		localMessage = errorMessage.getString(errorID);
	}
	@Override
	public String getLocalizedMessage() {
		return localMessage;
	}
}
