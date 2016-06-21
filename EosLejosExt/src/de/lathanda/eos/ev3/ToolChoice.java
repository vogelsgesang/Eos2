package de.lathanda.eos.ev3;

import java.awt.Container;
import java.awt.GridLayout;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;
import de.lathanda.eos.base.ResourceLoader;
import lejos.ev3.tools.EV3Control;
import lejos.ev3.tools.EV3SDCard;

public class ToolChoice extends JFrame {
	private static final long serialVersionUID = -3118282656846885601L;
	private static final ResourceBundle GUI = ResourceBundle.getBundle("lejos.gui");
	public ToolChoice() {
		setIconImage(ResourceLoader.loadImage("icons/eos.png"));
		Container panel = getContentPane();
		JButton control = new JButton(GUI.getString("Choice.Control"));
		JButton sdcard = new JButton(GUI.getString("Choice.SDCard"));

		panel.setLayout(new GridLayout(2,1));
		
		control.addActionListener((ae)-> showControl());
		panel.add(control);
		
		sdcard.addActionListener((ae)-> showSDCard());
		panel.add(sdcard);

		pack();
		setResizable(false);
	}
	public void showControl() {
		EV3Control.start(null);	
		setVisible(false);
	}
	public void showSDCard() {
		EV3SDCard.start(null);
		setVisible(false);
	}
}
