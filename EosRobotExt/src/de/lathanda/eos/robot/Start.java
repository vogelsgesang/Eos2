package de.lathanda.eos.robot;

import javax.swing.SwingUtilities;

import de.lathanda.eos.robot.gui.WorldEditor;

public class Start {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new WorldEditor().setVisible(true));
	}
}
