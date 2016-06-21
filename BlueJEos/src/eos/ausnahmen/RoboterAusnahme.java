package eos.ausnahmen;

import de.lathanda.eos.robot.exceptions.RobotException;
/**
 * @author Peter Schneider
 * 
 * <p>Irgendetwas ist bei einer Roboter Aktion schief gegangen.
 * Da alle Roboterfehler Laufzeitfehler sind kann man so tun also ob sie
 * nicht passiert.</p>
 * <p>Will man einen Absturz bei einem Fehler vermeiden, muss man die Ausnahme fangen</p>
 * \code
 *   try {
 *     ... Roboter aktion ...
 *   } catch (RoboterAusnahme roboterAusnahme) {
 *     ... Fehlerreaktion ...
 *   }
 * \endcode
 */
public class RoboterAusnahme extends RuntimeException {
	private static final long serialVersionUID = -9183783492499011105L;
	private RobotException ausnahme;
	public RoboterAusnahme(RobotException ausnahme) {
		this.ausnahme = ausnahme;
	}
	@Override
	public String getLocalizedMessage() {
		return ausnahme.getLocalizedMessage();
	}
}
