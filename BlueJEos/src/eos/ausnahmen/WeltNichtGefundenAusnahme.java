package eos.ausnahmen;

import de.lathanda.eos.robot.exceptions.RobotException;
/**
 * @author Peter Schneider
 * 
 * Die Datei, welche versucht wurde zu laden konnte nicht gefunden werden.
 * Entweder gibt es die Datei nicht oder sie liegt im falschen Verzeichnis.
 */
public class WeltNichtGefundenAusnahme extends RoboterAusnahme {
	private static final long serialVersionUID = -11203248164195147L;
	public WeltNichtGefundenAusnahme(RobotException ausnahme) {
		super(ausnahme);
	}
}
