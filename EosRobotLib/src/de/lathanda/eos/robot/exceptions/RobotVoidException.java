package de.lathanda.eos.robot.exceptions;
/**
 * Der Roboter wurde ohne Welt bewegt.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */

public class RobotVoidException extends RobotException {
	private static final long serialVersionUID = -125099304631393167L;
	public RobotVoidException() {
		super("robot.void");
	}
}
