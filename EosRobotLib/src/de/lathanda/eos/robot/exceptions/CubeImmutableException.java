package de.lathanda.eos.robot.exceptions;
/**
 * Stein ist unveränderlich.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */

public class CubeImmutableException extends RobotException {
	private static final long serialVersionUID = 6033024898072385313L;
	public CubeImmutableException() {
		super("cube.immutable");
	}
}
