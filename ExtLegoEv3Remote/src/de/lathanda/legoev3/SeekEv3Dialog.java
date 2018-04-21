package de.lathanda.legoev3;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import de.lathanda.eos.extension.Command;

public class SeekEv3Dialog extends JFrame {
	private static final long serialVersionUID = 4665725904712634527L;
	private JTextArea txtFound;
	private Ev3SeekerThread seeker;
	public SeekEv3Dialog() {
		txtFound = new JTextArea();
		this.getContentPane().add(txtFound);
		seeker = Ev3SeekerThread.getInstance();
		setSize(300,300);
	}
	public class Show implements Command {

		@Override
		public String getID() {
			return "seekev3dialog.show";
		}

		@Override
		public void invoke() {
			seeker.start();
			setVisible(true);
		}
		
	}
}
