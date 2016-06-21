package eos.ausnahmen;
/**
 * @author Peter Schneider
 * 
 * Der Roboter hat eine Bewegung versucht die nicht erfolgreich war.
 */
import de.lathanda.eos.robot.exceptions.RobotException;

public class BewegungFehlgeschlagenAusnahme extends RoboterAusnahme {
	private static final long serialVersionUID = -6920652670564070015L;
	public BewegungFehlgeschlagenAusnahme(RobotException ausnahme) {
		super(ausnahme);
	}
}
