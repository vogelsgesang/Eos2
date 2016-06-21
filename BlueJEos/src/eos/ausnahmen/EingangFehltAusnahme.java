package eos.ausnahmen;

import de.lathanda.eos.robot.exceptions.RobotException;
/**
 * @author Peter Schneider
 * 
 * Es wurde versucht einen Roboter in einer Welt zu platzieren die keinen Eingang mehr frei hat.
 * Je Eingang kann nur ein Roboter die Welt betreten.
 */
public class EingangFehltAusnahme extends RoboterAusnahme {
	private static final long serialVersionUID = -2032325174334754724L;
	public EingangFehltAusnahme(RobotException ausnahme) {
		super(ausnahme);
	}
}
