package de.lathanda.eos.ev3;

import javax.swing.SwingUtilities;

public class Start {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new ToolChoice().setVisible(true));
	}

}
