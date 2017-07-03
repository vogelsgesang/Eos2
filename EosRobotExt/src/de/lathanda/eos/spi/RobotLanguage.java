package de.lathanda.eos.spi;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.jogamp.opengl.GLProfile;

import de.lathanda.eos.common.gui.Messages;
import de.lathanda.eos.gui.objectchart.DirectionValue;
import de.lathanda.eos.interpreter.parsetree.SystemType.ObjectSource;
import de.lathanda.eos.robot.Direction;
import de.lathanda.eos.robot.Robot;
import de.lathanda.eos.robot.World;
import de.lathanda.eos.robot.gui.WorldEditor;
import de.lathanda.eos.util.GuiToolkit;

public class RobotLanguage extends Language implements LanguageProvider {
	public static final ResourceBundle ROBOT = ResourceBundle.getBundle("robot.robot"); 
	@Override
	public void registerLanguage(LanguageManager lm) throws IOException {
		try {
    	//init JOGL to avoid wait time before first call
			GLProfile.initSingleton();
		} catch (Throwable t) {
            JOptionPane.showMessageDialog(null, Messages.getString("Export.Error.Title"),
                    t.getLocalizedMessage(),
                    JOptionPane.ERROR_MESSAGE
            );
            return;
		}
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
	    JMenuItem mitWorldEditor = GuiToolkit.createMenuItem(ROBOT.getString("Menu.WorldEditor"), null, evt -> WorldEditorActionPerformed(evt));
	    JMenu robotMenu = GuiToolkit.createMenue(ROBOT.getString("Menu.Editor"));	  
        
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

	public String getSuper(String id) {
		switch (id) {
		case "robot":
			return "string";
		case "world":
			return "string";
		default:
			return null;
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
