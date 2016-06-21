
package eos;

import java.awt.Color;

import de.lathanda.eos.robot.Robot;
import de.lathanda.eos.robot.exceptions.CubeImmutableException;
import de.lathanda.eos.robot.exceptions.CubeMissingException;
import de.lathanda.eos.robot.exceptions.RobotMovementFailedException;
import de.lathanda.eos.robot.exceptions.RobotVoidException;
import eos.ausnahmen.BewegungFehlgeschlagenAusnahme;
import eos.ausnahmen.KeinSteinVorhandenAusnahme;
import eos.ausnahmen.RoboterOhneWeltAusnahme;
import eos.ausnahmen.SteinFeststehendAusnahme;

/**
 * @author Peter Schneider
 * 
 * Diese Klasse erzeugt einen Roboter. Dieser wird erst
 * sichtbar, wenn er eine Welt betritt. 
 * 
 * \code 
 *   Roboter karl = new Roboter(); 
 *   Welt welt = new Welt("spielfeld.eow");
 *   welt.betreten(karl); 
 * \endcode 
 * Nun können andere Befehle folgen. 
 * \code
 *   karl.schritt(); 
 *   karl.hinlegen(); 
 * \endcode
 */
public class Roboter {
	protected Robot robot;

	public Roboter() {
		robot = new Robot();
	}

	public void schritt() {
		try {
			robot.step();
		} catch (RobotMovementFailedException e) {
    		throw new BewegungFehlgeschlagenAusnahme(e); 
		} catch (RobotVoidException e) {
    		throw new RoboterOhneWeltAusnahme(e); 
    	}
	}

	public void schritt(int anzahl) {
		try {
			robot.step(anzahl);
		} catch (RobotMovementFailedException e) {
    		throw new BewegungFehlgeschlagenAusnahme(e); 
		} catch (RobotVoidException e) {
    		throw new RoboterOhneWeltAusnahme(e); 
    	}
	}
	public void schrittZurueck() {
		try {
			robot.stepBack();
		} catch (RobotMovementFailedException e) {
    		throw new BewegungFehlgeschlagenAusnahme(e); 
		} catch (RobotVoidException e) {
    		throw new RoboterOhneWeltAusnahme(e); 
    	}
	}
	public void schrittLinks() {
		try {
			robot.stepLeft();
		} catch (RobotMovementFailedException e) {
    		throw new BewegungFehlgeschlagenAusnahme(e); 
		} catch (RobotVoidException e) {
    		throw new RoboterOhneWeltAusnahme(e); 
    	}
	}
	public void schrittRechts() {
		try {
			robot.stepRight();
		} catch (RobotMovementFailedException e) {
    		throw new BewegungFehlgeschlagenAusnahme(e); 
		} catch (RobotVoidException e) {
    		throw new RoboterOhneWeltAusnahme(e); 
    	}
	}
	public void runterFliegen() {
		try {
			robot.flyDown();
		} catch (RobotMovementFailedException e) {
    		throw new BewegungFehlgeschlagenAusnahme(e); 
		} catch (RobotVoidException e) {
    		throw new RoboterOhneWeltAusnahme(e); 
    	}
	}
	public void linksFliegen() {
		try {
			robot.flyLeft();
		} catch (RobotMovementFailedException e) {
    		throw new BewegungFehlgeschlagenAusnahme(e); 
		} catch (RobotVoidException e) {
    		throw new RoboterOhneWeltAusnahme(e); 
    	}
	}
	public void rechtsFliegen() {
		try {
			robot.flyRight();
		} catch (RobotMovementFailedException e) {
    		throw new BewegungFehlgeschlagenAusnahme(e); 
		} catch (RobotVoidException e) {
    		throw new RoboterOhneWeltAusnahme(e); 
    	}
	}
	public void fliegen() {
		try {
			robot.fly();
		} catch (RobotMovementFailedException e) {
    		throw new BewegungFehlgeschlagenAusnahme(e); 
		} catch (RobotVoidException e) {
    		throw new RoboterOhneWeltAusnahme(e); 
    	}
	}	
	public void zurueckFliegen() {
		try {
			robot.flyBack();
		} catch (RobotMovementFailedException e) {
    		throw new BewegungFehlgeschlagenAusnahme(e); 
		} catch (RobotVoidException e) {
    		throw new RoboterOhneWeltAusnahme(e); 
    	}
	}
	public void linksdrehen() {
		robot.turnLeft();
	}

	public void rechtsdrehen() {
		robot.turnRight();
	}
	public void umdrehen() {
		robot.turnAround();
	}

	public void hinlegen() {
		try {
			robot.dropStone();
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		}
	}

	public void aufheben() {
		try {
			robot.pickup();
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		} catch (CubeMissingException e) {
			throw new KeinSteinVorhandenAusnahme(e);
		} catch (CubeImmutableException e) {
			throw new SteinFeststehendAusnahme(e);
		}
	}
	public void aufheben(int n) {
		try {
			robot.pickup(n);
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		} catch (CubeMissingException e) {
			throw new KeinSteinVorhandenAusnahme(e);
		} catch (CubeImmutableException e) {
			throw new SteinFeststehendAusnahme(e);
		}
	}
	public void steinSetzen(int n) {
		try {
			robot.placeStone(n);
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		} catch (CubeImmutableException e) {
			throw new SteinFeststehendAusnahme(e);
		}
	}
	public void steinEntfernen(int n) {
		try {
			robot.removeStone(n);
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		} catch (CubeImmutableException e) {
			throw new SteinFeststehendAusnahme(e);
		}
	}
	public void steinFarbeSetzen(Color farbe) {
		robot.setStoneColor(farbe);
	}

	public void markeSetzen() {
		try {
			robot.setMark();
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		}
	}

	public void markeLoeschen() {
		try {
			robot.removeMark();
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		}
	}
	public boolean istMarke() {
		try {
			return robot.isMarked();
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		}
	}
	public boolean istFrei() {
		try {
			return robot.isFree();
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		}
	}
	public boolean istLinksFrei() {
		try {
			return robot.isLeftFree();
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		}
	}
	public boolean istRechtsFrei() {
		try {
			return robot.isRightFree();
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		}
	}
	public boolean istHintenFrei() {
		try {
			return robot.isBackFree();
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		}
	}
	public boolean istObenFrei() {
		try {
			return robot.isTopFree();
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		}
	}
	public boolean istUntenFrei() {
		try {
			return robot.isBottomFree();
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		}
	}
	public boolean istHindernis() {
		try {
			return robot.isObstacle();
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		}		
	}
	public boolean istHintenHindernis() {
		try {
			return robot.isBackObstacle();
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		}		
	}
	public boolean istLinksHindernis() {
		try {
			return robot.isLeftObstacle();
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		}		
	}
	public boolean istRightHindernis() {
		try {
			return robot.isRightObstacle();
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		}		
	}
	public boolean istStein() {
		try {
			return robot.hasStone();
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		}
	}
	public boolean istStein(int n) {
		try {
			return robot.hasStone(n);
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		}
	}
	public boolean istSueden() {
		return robot.isFacingSouth();
	}
	public boolean istWesten() {
		return robot.isFacingWest();
	}
	public boolean istNorden() {
		return robot.isFacingNorth();
	}
	public boolean istOsten() {
		return robot.isFacingEast();
	}	
}
