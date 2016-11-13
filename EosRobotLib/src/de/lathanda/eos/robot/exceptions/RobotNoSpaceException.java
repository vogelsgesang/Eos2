package de.lathanda.eos.robot.exceptions;
/**
 * Nicht genügend Platz für den Roboter.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */

public class RobotNoSpaceException extends RobotException {
	private static final long serialVersionUID = -850424017512233499L;
	public RobotNoSpaceException() {
		super("robot.no.space");
	}
}
