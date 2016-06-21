package eos.ausnahmen;

import de.lathanda.eos.robot.exceptions.RobotException;
/**
 * @author Peter Schneider
 * 
 * Der Roboter soll eine Welt betreten jedoch ist der Eingang nicht bzw. nicht
 * mehr frei.
 */
public class KeinPlatzAusnahme extends RoboterAusnahme {
	private static final long serialVersionUID = 8507459871242174812L;
	public KeinPlatzAusnahme(RobotException ausnahme) {
		super(ausnahme);
	}
}
