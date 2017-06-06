package de.lathanda.eos.robot.gui;

import static de.lathanda.eos.base.ResourceLoader.loadImage;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ResourceBundle;

import javax.swing.JFrame;

import de.lathanda.eos.robot.World;
/**
 * Fenster für die Welt.
 * 
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class WorldFrame extends JFrame {
	private static final long serialVersionUID = 5001020279686211090L;
	private static final ResourceBundle VIEW   = ResourceBundle.getBundle("text.robot");
    private static final Image     LOGO        = loadImage(VIEW.getString("Robot.Icon.Logo"));
    Component view;
	public WorldFrame(World world) {
		super(VIEW.getString("Robot.Title"));
        this.setIconImage(LOGO);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        //TODO use graphic card dependent factory and/or
        //resolve gl errors in order to use alternative renderer
        view = new WorldPanelOpenGLNoShader(world);
        
        setLocation(0, 0);
    	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    	setSize(new Dimension(screen.width/2, screen.height/2));
    	getContentPane().add(view, BorderLayout.CENTER);
	}
}
