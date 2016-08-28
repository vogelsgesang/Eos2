package de.lathanda.eos.common.gui;

import static de.lathanda.eos.common.gui.GuiConstants.GUI;

import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import de.lathanda.eos.base.ResourceLoader;
import de.lathanda.eos.common.gui.GuiConfiguration.ErrorBehavior;

/**
 * Einstellungsfenster
 *
 * @author Peter (Lathanda) Schneider
 */
public class ConfigFrame extends javax.swing.JFrame {
	private static final long serialVersionUID = -7493228893699859641L;
	/**
	 * Oberflächenconfiguration
	 */
	private GuiConfiguration guiConf = GuiConfiguration.def;

	/**
	 * Erzeugt ein Konfigurationsfenster.
	 */
    public ConfigFrame() {
        initComponents();
    }
    /**
     * Initialisiert die Komponenten.
     */
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        setIconImage(ResourceLoader.loadImage("icons/eos.png"));
        generalPanel  = new JPanel();
        commandPanel  = new JPanel();
        lblFontSize   = new JLabel(GUI.getString("configuration.fontsize"));
        lblErrorMode  = new JLabel(GUI.getString("configuration.errormode"));
        btnOk         = new JButton();
        btnCancel     = new JButton();
        txtFontSize   = new JTextField();
        cmbErrorMode  = new JComboBox<>();
        
        cmbErrorMode.addItem(ErrorBehaviorEntry.IGNORE);
        cmbErrorMode.addItem(ErrorBehaviorEntry.WARN);
        cmbErrorMode.addItem(ErrorBehaviorEntry.ABORT);
        
        generalPanel.add(lblFontSize);
        generalPanel.add(txtFontSize);
        generalPanel.add(lblErrorMode);
        generalPanel.add(cmbErrorMode);
        
        generalPanel.setBorder(new TitledBorder(GUI.getString("configuration.general")));
        generalPanel.setLayout(new GridLayout(0, 2));
        getContentPane().add(generalPanel);

        btnOk.setIcon(new ImageIcon(getClass().getResource("/icons/dialog-ok-2.png")));
        btnOk.addActionListener(ae -> {writeData(); setVisible(false);});
        
        btnCancel.setIcon(new ImageIcon(getClass().getResource("/icons/dialog-cancel-2.png")));
        btnCancel.addActionListener(ae -> {readData(); setVisible(false);});
        
        commandPanel.add(btnOk);
        commandPanel.add(btnCancel);
        getContentPane().add(commandPanel);
        
        readData();
        pack();
    }
    /**
     * Liest die Daten aus der Konfiguration ein.
     */
    private void readData() {
    	txtFontSize.setText(String.valueOf(guiConf.getFontsize()));
    	switch (guiConf.getErrorBehavior()) {
    	case ABORT:
    		cmbErrorMode.setSelectedItem(ErrorBehaviorEntry.ABORT);
    		break;
    	case WARN:
    		cmbErrorMode.setSelectedItem(ErrorBehaviorEntry.WARN);
    		break;
    	case IGNORE:
    		cmbErrorMode.setSelectedItem(ErrorBehaviorEntry.IGNORE);
    		break;
    	}
    	
    }
    /**
     * überträgt die Daten zurück in die Konfiguration.
     */
    public void writeData() {
    	try {
    		guiConf.setFontsize(Integer.parseInt(txtFontSize.getText()));
    	} catch (NumberFormatException e) {
    		txtFontSize.setText(String.valueOf(guiConf.getFontsize()));
    	}
    	guiConf.setErrorBehavior(((ErrorBehaviorEntry)cmbErrorMode.getSelectedItem()).errorBehavior);
    }
    private JPanel generalPanel;
    private JPanel commandPanel;
    private JLabel lblFontSize;
    private JTextField txtFontSize;
    private JLabel lblErrorMode;
    private JComboBox<ErrorBehaviorEntry> cmbErrorMode;
    private JButton btnOk;
    private JButton btnCancel;
    /**
     * Eintrag in Dropdownbox.
     * @author Peter (Lathanda) Schneider
     *
     */
    private static class ErrorBehaviorEntry {
    	public static final ErrorBehaviorEntry ABORT = new ErrorBehaviorEntry(GUI.getString("configuration.errorbehavior.abort"), GuiConfiguration.ErrorBehavior.ABORT); 
    	public static final ErrorBehaviorEntry IGNORE = new ErrorBehaviorEntry(GUI.getString("configuration.errorbehavior.ignore"), GuiConfiguration.ErrorBehavior.IGNORE); 
    	public static final ErrorBehaviorEntry WARN = new ErrorBehaviorEntry(GUI.getString("configuration.errorbehavior.warn"), GuiConfiguration.ErrorBehavior.WARN); 
    	private final String label;
    	public final GuiConfiguration.ErrorBehavior errorBehavior;
		private ErrorBehaviorEntry(String label, ErrorBehavior errorBehavior) {
			this.label = label;
			this.errorBehavior = errorBehavior;
		}
		@Override
		public String toString() {
			return label;
		}
    }
}
