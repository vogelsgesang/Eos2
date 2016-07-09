package de.lathanda.eos.spi;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.jogamp.opengl.GLProfile;

import de.lathanda.eos.gui.objectchart.DirectionValue;
import de.lathanda.eos.interpreter.Type.ObjectSource;
import de.lathanda.eos.robot.Direction;
import de.lathanda.eos.robot.Robot;
import de.lathanda.eos.robot.World;
import de.lathanda.eos.robot.gui.WorldEditor;

public class RobotLanguage extends Language implements LanguageProvider {
	public static final ResourceBundle ROBOT = ResourceBundle.getBundle("robot.robot"); 
	@Override
	public void registerLanguage(LanguageManager lm) throws IOException {
    	//init JOGL to avoid wait time before first call
   		GLProfile.initSingleton();
		//init diagrams
		lm.registerNames(ResourceBundle.getBundle("robot.map_names"));
		//init help
		try {
			lm.addClasses(ROBOT.getString("Help.Classes"));
		} catch (IOException e) {
			//hmm no help, we probably will survive that			
		}
		try {
			lm.addInfo(ROBOT.getString("Help.Info"));
		} catch (IOException e) {
			//hmm no help, we probably will survive that			
		}
		
		//init classes
		lm.registerLanguageByConfig(ResourceBundle.getBundle("robot.lang"), this);   
		
		//add gui elements
	    JMenuItem mitWorldEditor = new JMenuItem();;
	    JMenu robotMenu = new JMenu();;	  
	    robotMenu.setText(ROBOT.getString("Menu.Editor"));
        
        mitWorldEditor.setText(ROBOT.getString("Menu.WorldEditor"));
        mitWorldEditor.addActionListener(evt -> WorldEditorActionPerformed(evt));
        
        robotMenu.add(mitWorldEditor);	   
        lm.registerMenu(robotMenu);
        
        //register object attribut renderer
        lm.registerUnit((value)->{return new DirectionValue((Direction)value);}, Direction.class.getName());
        
	}

	public ObjectSource getObjectSource(String id) {
		switch (id) {
		case "robot":
			return () -> {
				return new Robot();
			};
		case "world":
			return () -> {
				return new World();
			};
		default:
			return null;
		}
	}

	public String[] getInherits(String id) {
		switch (id) {
		case "robot":
			return new String[]{"robot", "string"};
		case "world":
			return new String[]{"world", "string"};
		default:
			return new String[]{id};
		}
	}
	public Class<?> getClassById(String id) {
		switch (id) {
        case "robot":
            return Robot.class;
        case "world":
            return World.class;
        default:
            return null;
		}		
	}


	@Override
	public Class<?> getFunctionTarget() {
		return null;
	}		
    /**
     * Welteditor anzeigen.
     * @param evt
     */
    private void WorldEditorActionPerformed(java.awt.event.ActionEvent evt) {
    	new WorldEditor().setVisible(true);
    }

}