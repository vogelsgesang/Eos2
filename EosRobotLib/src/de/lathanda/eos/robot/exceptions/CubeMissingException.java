package de.lathanda.eos.robot.exceptions;
/**
 * Nicht genügend Steine vorhanden.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */

public class CubeMissingException extends RobotException {
	private static final long serialVersionUID = -2926971075942518545L;
	public CubeMissingException() {
		super("cube.missing");
	}
}
