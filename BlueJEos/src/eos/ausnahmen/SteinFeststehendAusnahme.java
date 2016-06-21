package eos.ausnahmen;

import de.lathanda.eos.robot.exceptions.RobotException;
/**
 * @author Peter Schneider
 * 
 * Ein Roboter hat versucht einen feststehenden Stein aufzuheben.
 */
public class SteinFeststehendAusnahme extends RoboterAusnahme {
	private static final long serialVersionUID = 5763508469303606284L;
	public SteinFeststehendAusnahme(RobotException ausnahme) {
		super(ausnahme);
	}
}
