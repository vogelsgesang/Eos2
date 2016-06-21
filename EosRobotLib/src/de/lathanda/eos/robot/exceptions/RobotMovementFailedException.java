package de.lathanda.eos.robot.exceptions;
/**
 * Bewegung gescheitert.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */

public class RobotMovementFailedException extends RobotException {
	private static final long serialVersionUID = -1483780627814673329L;
	public RobotMovementFailedException() {
		super("robot.movement.failed");
	}
}
