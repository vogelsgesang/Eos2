package de.lathanda.eos.robot.gui;

import static de.lathanda.eos.base.ResourceLoader.loadImage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import de.lathanda.eos.robot.World;
import de.lathanda.eos.robot.exceptions.RobotException;
import de.lathanda.eos.robot.gui.WorldPanelOpenGLNoShader;
import de.lathanda.eos.spi.RobotLanguage;
/**
 * Welteditor
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.9
 */
public class WorldEditor extends JFrame implements KeyListener, DocumentListener {
	private static final long serialVersionUID = -3910302047095245337L;
    private static final Image     LOGO        = loadImage(RobotLanguage.ROBOT.getString("Icon"));
    private World world;
    private Component view;
    private final JFileChooser filechooser;
    private JPanel editorToolbar;
    private JButton btnUp;
    private JButton btnDown;
    private JButton btnLeft;
    private JButton btnRight;
    private JButton btnForward;
    private JButton btnBack;
    private JButton btnChooseColor;
    private JButton btnRemove;
    private JButton btnStone;
    private JButton btnRock;
    private JButton btnMark;
    private JButton btnEntrance;
    private JButton btnSaveAs;
    private JButton btnLoad;
    
    private JPanel rangeToolbar;    
    private JTextField txtMinX;
    private JTextField txtMaxX;
    private JTextField txtMinY;
    private JTextField txtMaxY;
  
    private JLabel lblRangeX;
    private JLabel lblRangeY;
    
	public WorldEditor( ) {
		super(RobotLanguage.ROBOT.getString("Title"));
        this.setIconImage(LOGO);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        world = new World(this);

        view = new WorldPanelOpenGLNoShader(world);
        world.setShowCursor(true);
        setLocation(0, 0);
    	
        filechooser = new JFileChooser();
        filechooser.addChoosableFileFilter(new FileNameExtensionFilter(RobotLanguage.ROBOT.getString("File.EOW"), "eow"));
        filechooser.setCurrentDirectory(new File("."));
    	
    	getContentPane().add(view, BorderLayout.CENTER);
    	
		editorToolbar = new JPanel();
		editorToolbar.setLayout(new GridLayout(2,7));
	
	    btnUp = new JButton(new ImageIcon(getClass().getResource("/icons/navigate_up2.png")));
	    btnUp.setToolTipText(RobotLanguage.ROBOT.getString("Tooltip.Up"));
	    mnemonics.put(KeyEvent.VK_A, evt -> upActionPerformed(evt));
	    btnUp.addActionListener(evt -> upActionPerformed(evt));

	    btnDown = new JButton(new ImageIcon(getClass().getResource("/icons/navigate_down2.png")));
	    btnDown.setToolTipText(RobotLanguage.ROBOT.getString("Tooltip.Down"));
	    mnemonics.put(KeyEvent.VK_Z, evt -> downActionPerformed(evt));
	    btnDown.addActionListener(evt -> downActionPerformed(evt));

	    
	    btnLeft = new JButton(new ImageIcon(getClass().getResource("/icons/navigate_left.png")));
	    btnLeft.setToolTipText(RobotLanguage.ROBOT.getString("Tooltip.Left"));
	    mnemonics.put(KeyEvent.VK_LEFT,	evt -> leftActionPerformed(evt));
	    btnLeft.addActionListener(evt -> leftActionPerformed(evt));
	    
	    btnRight = new JButton(new ImageIcon(getClass().getResource("/icons/navigate_right.png")));
	    btnRight.setToolTipText(RobotLanguage.ROBOT.getString("Tooltip.Right"));
	    mnemonics.put(KeyEvent.VK_RIGHT, evt -> rightActionPerformed(evt));
	    btnRight.addActionListener(evt -> rightActionPerformed(evt));
	    
	    btnForward = new JButton(new ImageIcon(getClass().getResource("/icons/navigate_up.png")));
	    btnForward.setToolTipText(RobotLanguage.ROBOT.getString("Tooltip.Forward"));
	    mnemonics.put(KeyEvent.VK_UP, evt -> forwardActionPerformed(evt));
	    btnForward.addActionListener(evt -> forwardActionPerformed(evt));
	    
	    btnBack = new JButton(new ImageIcon(getClass().getResource("/icons/navigate_down.png")));
	    btnBack.setToolTipText(RobotLanguage.ROBOT.getString("Tooltip.Back"));
	    mnemonics.put(KeyEvent.VK_DOWN, evt -> backActionPerformed(evt));
	    btnBack.addActionListener(evt -> backActionPerformed(evt));

	    btnSaveAs = new JButton(new ImageIcon(getClass().getResource("/icons/save_as.png")));
	    btnSaveAs.setToolTipText(RobotLanguage.ROBOT.getString("Tooltip.SaveAs"));
	    btnSaveAs.addActionListener(evt -> saveAsActionPerformed(evt));

	    btnLoad = new JButton(new ImageIcon(getClass().getResource("/icons/folder_open.png")));
	    btnLoad.setToolTipText(RobotLanguage.ROBOT.getString("Tooltip.Load"));
	    btnLoad.addActionListener(evt -> loadActionPerformed(evt));	    
	    
	    btnChooseColor = new JButton(new ImageIcon(getClass().getResource("/icons/painters_palette.png")));
	    btnChooseColor.setToolTipText(RobotLanguage.ROBOT.getString("Tooltip.ChooseColor"));
	    mnemonics.put(KeyEvent.VK_C, evt -> chooseColorActionPerformed(evt));
	    btnChooseColor.addActionListener(evt -> chooseColorActionPerformed(evt));
	    
	    btnRemove = new JButton(new ImageIcon(getClass().getResource("/icons/garbage.png")));
	    btnRemove.setToolTipText(RobotLanguage.ROBOT.getString("Tooltip.Remove"));
	    mnemonics.put(KeyEvent.VK_X, evt -> removeActionPerformed(evt));
	    btnRemove.addActionListener(evt -> removeActionPerformed(evt));
	    
	    btnStone = new JButton(new ImageIcon(getClass().getResource("/icons/cubes.png")));
	    btnStone.setToolTipText(RobotLanguage.ROBOT.getString("Tooltip.Stone"));
	    mnemonics.put(KeyEvent.VK_S, evt -> stoneActionPerformed(evt));
	    btnStone.addActionListener(evt -> stoneActionPerformed(evt));
	    
	    btnRock = new JButton(new ImageIcon(getClass().getResource("/icons/brickwall.png"))); 
	    btnRock.setToolTipText(RobotLanguage.ROBOT.getString("Tooltip.Rock"));
	    mnemonics.put(KeyEvent.VK_R, evt -> rockActionPerformed(evt));
	    btnRock.addActionListener(evt -> rockActionPerformed(evt));
	    
	    btnMark = new JButton(new ImageIcon(getClass().getResource("/icons/gold_bar.png"))); 
	    btnMark.setToolTipText(RobotLanguage.ROBOT.getString("Tooltip.Mark"));
	    mnemonics.put(KeyEvent.VK_M, evt -> markActionPerformed(evt));
	    btnMark.addActionListener(evt -> markActionPerformed(evt));
	    
	    btnEntrance = new JButton(new ImageIcon(getClass().getResource("/icons/robot.png")));
	    btnEntrance.setToolTipText(RobotLanguage.ROBOT.getString("Tooltip.Entrance"));
	    mnemonics.put(KeyEvent.VK_E, evt -> entranceActionPerformed(evt));
	    btnEntrance.addActionListener(evt -> entranceActionPerformed(evt));

	    editorToolbar.add(btnUp);
	    editorToolbar.add(btnChooseColor);
	    editorToolbar.add(btnForward);
	    editorToolbar.add(btnRemove);
	    editorToolbar.add(btnEntrance);
	    editorToolbar.add(btnStone);
	    editorToolbar.add(btnSaveAs);
	    editorToolbar.add(btnDown);
	    editorToolbar.add(btnLeft);
	    editorToolbar.add(btnBack);
	    editorToolbar.add(btnRight);
	    editorToolbar.add(btnMark);
	    editorToolbar.add(btnRock);
	    editorToolbar.add(btnLoad);

	    getContentPane().add(editorToolbar, BorderLayout.SOUTH);
	    
		view.addKeyListener(this);
	    editorToolbar.addKeyListener(this);
	    btnUp.addKeyListener(this);
	    btnDown.addKeyListener(this);
	    btnLeft.addKeyListener(this);
	    btnRight.addKeyListener(this);
	    btnForward.addKeyListener(this);
	    btnBack.addKeyListener(this);
	    btnChooseColor.addKeyListener(this);
	    btnRemove.addKeyListener(this);
	    btnStone.addKeyListener(this);
	    btnRock.addKeyListener(this);
	    btnMark.addKeyListener(this);
	    btnEntrance.addKeyListener(this);
	    btnSaveAs.addKeyListener(this);
	    btnLoad.addKeyListener(this);
	    
	    rangeToolbar = new JPanel();
	    rangeToolbar.setLayout(new GridLayout(1,6));
	    
	    txtMinX   = new JTextField();
	    lblRangeX = new JLabel(RobotLanguage.ROBOT.getString("Range.X"));
	    lblRangeX.setHorizontalAlignment(JTextField.CENTER);
	    txtMaxX   = new JTextField();
	    
	    txtMinY   = new JTextField();
	    lblRangeY = new JLabel(RobotLanguage.ROBOT.getString("Range.Y"));
	    lblRangeY.setHorizontalAlignment(JTextField.CENTER);
	    txtMaxY   = new JTextField();

	    
	    txtMinX.getDocument().addDocumentListener(this);
	    txtMaxX.getDocument().addDocumentListener(this);
	    txtMinY.getDocument().addDocumentListener(this);
	    txtMaxY.getDocument().addDocumentListener(this);
	    
	    rangeToolbar.add(txtMinX);
	    rangeToolbar.add(lblRangeX);
	    rangeToolbar.add(txtMaxX);
	    rangeToolbar.add(txtMinY);
	    rangeToolbar.add(lblRangeY);
	    rangeToolbar.add(txtMaxY);

	    getContentPane().add(rangeToolbar, BorderLayout.NORTH);
	    
	    pack();

	}
	
	private void entranceActionPerformed(ActionEvent evt) {
		world.toggleEntranceCursor();
	}
	private void markActionPerformed(ActionEvent evt) {
		world.toggleMarkCursor();
	}
	private void rockActionPerformed(ActionEvent evt) {
		world.setRockCursor();
	}
	private void stoneActionPerformed(ActionEvent evt) {
		world.setStoneCursor();
	}
	private void removeActionPerformed(ActionEvent evt) {
		world.removeCursor();
	}
	private void chooseColorActionPerformed(ActionEvent evt) {
		Color color = JColorChooser.showDialog(this, RobotLanguage.ROBOT.getString("Color.Title"), world.getStoneColor());
		if (color != null) {
			world.setStoneColor(color);
		}
	}
	private void backActionPerformed(ActionEvent evt) {
		world.moveCursorSouth();
	}
	private void forwardActionPerformed(ActionEvent evt) {
		world.moveCursorNorth();
	}
	private void rightActionPerformed(ActionEvent evt) {
		world.moveCursorEast();
	}
	private void leftActionPerformed(ActionEvent evt) {
		world.moveCursorWest();
	}
	private void downActionPerformed(ActionEvent evt) {
		world.moveCursorDown();
	}
	public void upActionPerformed(ActionEvent ae) {
		world.moveCursorUp();
	}
	public void saveAsActionPerformed(ActionEvent ae) {
        try {
            if (filechooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            	try (FileOutputStream fos = new FileOutputStream(filechooser.getSelectedFile())) {
            		world.save(fos);
            	}
            }
        } catch (TransformerException | ParserConfigurationException | IOException io) {
            JOptionPane.showMessageDialog(this, RobotLanguage.ROBOT.getString("Save.Error.Title"),
                    io.getLocalizedMessage(),
                    JOptionPane.ERROR_MESSAGE
            );
        }
	}	
	public void loadActionPerformed(ActionEvent ae) {
        try {
            if (filechooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            	try (FileInputStream fis = new FileInputStream(filechooser.getSelectedFile())) {
            		world.load(fis);
            	}
            }
        } catch (RobotException | IOException io) {
            JOptionPane.showMessageDialog(this, RobotLanguage.ROBOT.getString("Open.Error.Title"),
                    io.getLocalizedMessage(),
                    JOptionPane.ERROR_MESSAGE
            );
        }
	}

	TreeMap<Integer, ActionListener> mnemonics = new TreeMap<>();
	@Override
	public void keyPressed(KeyEvent ke) {
		ActionListener actionListener = mnemonics.get(ke.getKeyCode());
		if (actionListener != null) {
			actionListener.actionPerformed(null);
		}
	}

	@Override
	public void keyReleased(KeyEvent ke) {
	}

	@Override
	public void keyTyped(KeyEvent ke) {
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		refreshRange();
	}
	@Override
	public void insertUpdate(DocumentEvent de) {
		refreshRange();
	}
	@Override
	public void removeUpdate(DocumentEvent de) {
		refreshRange();
	}
	private void refreshRange() {
		Integer minX = checkRangeText(txtMinX);
		Integer maxX = checkRangeText(txtMaxX);
		Integer minY = checkRangeText(txtMinY);
		Integer maxY = checkRangeText(txtMaxY);
		world.setRange(minX, maxX, minY, maxY);			
	}
	private static Integer checkRangeText(JTextField f) {
		String text = f.getText();
		try {
			return Integer.parseInt(text);			
		} catch (Throwable t) {
			return null;
		}
	}	
}
