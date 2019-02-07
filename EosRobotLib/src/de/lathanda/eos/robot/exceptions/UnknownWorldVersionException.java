package de.lathanda.eos.robot.exceptions;
/**
 * Die Weltdatei ist korrupt.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */

public class UnknownWorldVersionException extends RobotException {
	private static final long serialVersionUID = -4809276041967567419L;

	public UnknownWorldVersionException() {
		super("world.load.versionunknown");
	}
}
