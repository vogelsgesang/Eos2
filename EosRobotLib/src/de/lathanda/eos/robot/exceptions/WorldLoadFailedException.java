package de.lathanda.eos.robot.exceptions;
/**
 * Die Weltdatei ist korrupt.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */

public class WorldLoadFailedException extends RobotException {
	private static final long serialVersionUID = -4220917696590143524L;
	public WorldLoadFailedException() {
		super("world.load.failed");
	}
}
