package eos.ausnahmen;

import de.lathanda.eos.robot.exceptions.RobotException;
/**
 * @author Peter Schneider
 * 
 * Es wurde versucht einen Roboter zu bewegen, der
 * sich in keiner Welt befindet.
 */
public class RoboterOhneWeltAusnahme extends RoboterAusnahme {
	private static final long serialVersionUID = 7250972308637388639L;
	public RoboterOhneWeltAusnahme(RobotException ausnahme) {
		super(ausnahme);
	}
}
