package eos.ausnahmen;

import de.lathanda.eos.robot.exceptions.RobotException;
/**
 * @author Peter Schneider
 * 
 * Es wurde versucht einen Stein aufzuheben den es nicht gibt.
 */
public class KeinSteinVorhandenAusnahme extends RoboterAusnahme {
	private static final long serialVersionUID = -282952263267492095L;
	public KeinSteinVorhandenAusnahme(RobotException ausnahme) {
		super(ausnahme);
	}
}
