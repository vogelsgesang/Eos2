package eos.ausnahmen;

import de.lathanda.eos.robot.exceptions.RobotException;
/**
 * @author Peter Schneider
 * 
 * Beim laden der Welt ist etwas schief gegangen.
 * In der Regel enthält die Datei keine WElt oder diese wurde beschädigt.
 */
public class WeltKorruptAusnahme extends RoboterAusnahme {
	private static final long serialVersionUID = 6440831084423840544L;
	public WeltKorruptAusnahme(RobotException ausnahme) {
		super(ausnahme);
	}
}
