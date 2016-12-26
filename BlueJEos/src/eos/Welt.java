package eos;

import java.awt.Color;

import de.lathanda.eos.robot.World;
import de.lathanda.eos.robot.exceptions.CubeImmutableException;
import de.lathanda.eos.robot.exceptions.CubeMissingException;
import de.lathanda.eos.robot.exceptions.RobotEntranceMissingException;
import de.lathanda.eos.robot.exceptions.RobotNoSpaceException;
import de.lathanda.eos.robot.exceptions.WorldLoadFailedException;
import de.lathanda.eos.robot.exceptions.WorldNotFoundException;
import eos.ausnahmen.EingangFehltAusnahme;
import eos.ausnahmen.KeinPlatzAusnahme;
import eos.ausnahmen.KeinSteinVorhandenAusnahme;
import eos.ausnahmen.SteinFeststehendAusnahme;
import eos.ausnahmen.WeltKorruptAusnahme;
import eos.ausnahmen.WeltNichtGefundenAusnahme;

/**
 * @author Peter Schneider
 * 
 * Eine Welt für Roboter.
 */
public class Welt {
    World world;
    public Welt() {
        world = new World();
    }
    public Welt(String filename) {
    	this();
    	try {
    		world.load(filename);
    	} catch (WorldLoadFailedException cwe) {
    		throw new WeltKorruptAusnahme(cwe);
    	} catch (WorldNotFoundException wnfe) {
    		throw new WeltNichtGefundenAusnahme(wnfe);
		}
    }
    public void betreten(Roboter roboter) {
        try {
			world.enter(roboter.robot);
		} catch (RobotEntranceMissingException nle) {
			throw new EingangFehltAusnahme(nle);
		} catch (RobotNoSpaceException rnse) {
			throw new KeinPlatzAusnahme(rnse);
		}
    }
    public void steinSetzen(int x, int y, int z) {
    	world.setStone(x,y,z);
    }
    public void steinFarbeSetzen(Color farbe) {
    	world.setStoneColor(farbe);
    }
    public void steinHinlegen(int x, int y) {
    	world.dropStone(x, y);
    }
    public void steinEntfernen(int x, int y, int z) {
    	try {
			world.removeStone(x,y,z);
		} catch (CubeImmutableException e) {
			throw new SteinFeststehendAusnahme(e); 
		}
    }
    public void steinAufheben(int x, int y) {
    	try {
			world.pickupStone(x,y);
		} catch (CubeMissingException cme) {
    		throw new KeinSteinVorhandenAusnahme(cme); 
		} catch (CubeImmutableException cie) {
    		throw new SteinFeststehendAusnahme(cie); 
		}
    }
    public void laden(String name) {
    	try {
    		world.load(name);
    	} catch (WorldLoadFailedException cwe) {
    		throw new KeinSteinVorhandenAusnahme(cwe);    		
    	} catch (WorldNotFoundException wnfe) {
    		throw new SteinFeststehendAusnahme(wnfe);    		
		}
    }
    public void ziegelVerstreuen(int links, int oben, int rechts, int unten, int dichte) {
    	world.fillRandom(links, oben, rechts, unten, dichte);
    }
}
