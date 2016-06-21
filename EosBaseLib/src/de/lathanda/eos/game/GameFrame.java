package de.lathanda.eos.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
/**
 * \brief Standard Fenster für Spiele
 * 
 * @author Lathanda
 */
/*package*/ class GameFrame extends JFrame implements WindowListener{
	private static final long serialVersionUID = -7729801117398028803L;
	
	/**
	 * Neues Fenster mit Standardaufbau
	 * @param width  Breite in mm
	 * @param height Höhe in mm
	 * @param back Hintergrundfarbe
	 * @param game  Spiel
	 * @param title Fenstertitel
	 */
	protected GameFrame(double width, double height, Color back, Game game, String title) {
		super(title);
		
		setLocation(0, 0);
		// layout
		setLayout(new BorderLayout());
		add(new GamePanel(game, 30, back, width, height), BorderLayout.CENTER);
		setResizable(false);
		pack();
		setVisible(true);
		addWindowListener(this);
	}

	public void windowActivated(WindowEvent we) {}
	public void windowClosed(WindowEvent we) {
		System.exit(0);
	}
	public void windowClosing(WindowEvent we) {
		dispose();
	}
	public void windowDeactivated(WindowEvent we) {}
	public void windowDeiconified(WindowEvent we) {}
	public void windowIconified(WindowEvent we) {}
	public void windowOpened(WindowEvent we) {}	
}
