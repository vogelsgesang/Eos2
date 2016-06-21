package de.lathanda.eos.robot.exceptions;
/**
 * Weltdatei wurde nicht gefunden.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */

public class WorldNotFoundException extends RobotException {
	private static final long serialVersionUID = 6063309540599083912L;
	public WorldNotFoundException() {
		super("world.not.found");
	}
}
