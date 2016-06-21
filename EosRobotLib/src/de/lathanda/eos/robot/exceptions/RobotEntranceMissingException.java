package de.lathanda.eos.robot.exceptions;
/**
 * Kein Eingang mehr für den Roboter vorhanden.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */

public class RobotEntranceMissingException extends RobotException {
	private static final long serialVersionUID = 462041726767207120L;
	public RobotEntranceMissingException() {
		super("robot.entrance.missing");
	}
}
