package de.lathanda.eos.robot.gui;

import static de.lathanda.eos.base.ResourceLoader.loadImage;
import static de.lathanda.eos.spi.RobotLanguage.ROBOT;
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

import de.lathanda.eos.common.gui.Messages;
import de.lathanda.eos.robot.World;
import de.lathanda.eos.robot.World.IntRange;
import de.lathanda.eos.robot.exceptions.RobotException;
import de.lathanda.eos.robot.gui.WorldPanelOpenGLNoShader;
import de.lathanda.eos.spi.RobotLanguage;
import de.lathanda.eos.util.GuiToolkit;
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
    private JButton btnChooseColorStone;
    private JButton btnChooseColorMark;
    private JButton btnRemove;
    private JButton btnStone;
    private JButton btnRock;
    private JButton btnMark;
    private JButton btnEntrance;
    private JButton btnSaveAs;
    private JButton btnSave;
    private JButton btnLoad;
    
    private JPanel rangeToolbar;    
    private JTextField txtMinX;
    private JTextField txtMaxX;
    private JTextField txtMinY;
    private JTextField txtMaxY;
  
    private JLabel lblRangeX;
    private JLabel lblRangeY;
	/**
	 * Aktuell geöffnete Datei
	 */
	private File activeFile = null;
	
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
		editorToolbar.setLayout(new GridLayout(2,8));
	
		btnUp = GuiToolkit.createButton("icons/navigate_up2.png", ROBOT.getString("Tooltip.Up"), evt -> upActionPerformed(evt));
	    mnemonics.put(KeyEvent.VK_A, evt -> upActionPerformed(evt));

	    btnDown = GuiToolkit.createButton("icons/navigate_down2.png", ROBOT.getString("Tooltip.Down"), evt -> downActionPerformed(evt)); 
	    mnemonics.put(KeyEvent.VK_Z, evt -> downActionPerformed(evt));
	    
	    btnLeft = GuiToolkit.createButton("icons/navigate_left.png", ROBOT.getString("Tooltip.Left"), evt -> leftActionPerformed(evt));
	    mnemonics.put(KeyEvent.VK_LEFT,	evt -> leftActionPerformed(evt));
	    
	    btnRight = GuiToolkit.createButton("icons/navigate_right.png", ROBOT.getString("Tooltip.Right"), evt -> rightActionPerformed(evt)); 
	    mnemonics.put(KeyEvent.VK_RIGHT, evt -> rightActionPerformed(evt));
	    
	    btnForward = GuiToolkit.createButton("icons/navigate_up.png", ROBOT.getString("Tooltip.Forward"), evt -> forwardActionPerformed(evt)); 
	    mnemonics.put(KeyEvent.VK_UP, evt -> forwardActionPerformed(evt));
	    
	    btnBack = GuiToolkit.createButton("icons/navigate_down.png", ROBOT.getString("Tooltip.Back"), evt -> backActionPerformed(evt));
	    mnemonics.put(KeyEvent.VK_DOWN, evt -> backActionPerformed(evt));

	    btnSaveAs = GuiToolkit.createButton("icons/save_as.png", ROBOT.getString("Tooltip.SaveAs"), evt -> saveAsActionPerformed(evt)); 

	    btnSave = GuiToolkit.createButton("icons/floppy_disk.png", ROBOT.getString("Tooltip.Save"), evt -> saveActionPerformed(evt)); 
	    
	    btnLoad = GuiToolkit.createButton("icons/folder_open.png", ROBOT.getString("Tooltip.Load"), evt -> loadActionPerformed(evt)); 
	    
	    btnChooseColorStone = GuiToolkit.createButton("icons/painters_palette.png", ROBOT.getString("Tooltip.ChooseColorStone"), evt -> chooseColorStoneActionPerformed(evt)); 
	    mnemonics.put(KeyEvent.VK_C, evt -> chooseColorStoneActionPerformed(evt));
	    
	    btnChooseColorMark = GuiToolkit.createButton("icons/painters_palette2.png", ROBOT.getString("Tooltip.ChooseColorMark"), evt -> chooseColorMarkActionPerformed(evt)); 
	    mnemonics.put(KeyEvent.VK_V, evt -> chooseColorMarkActionPerformed(evt));

	    btnRemove = GuiToolkit.createButton("icons/garbage.png", ROBOT.getString("Tooltip.Remove"), evt -> removeActionPerformed(evt)); 
	    mnemonics.put(KeyEvent.VK_X, evt -> removeActionPerformed(evt));
	    
	    btnStone = GuiToolkit.createButton("icons/cubes.png", ROBOT.getString("Tooltip.Stone"), evt -> stoneActionPerformed(evt)); 
	    mnemonics.put(KeyEvent.VK_S, evt -> stoneActionPerformed(evt));
	    
	    btnRock = GuiToolkit.createButton("icons/brickwall.png", ROBOT.getString("Tooltip.Rock"), evt -> rockActionPerformed(evt));
	    mnemonics.put(KeyEvent.VK_R, evt -> rockActionPerformed(evt));
	    
	    btnMark = GuiToolkit.createButton("icons/gold_bar.png", ROBOT.getString("Tooltip.Mark"), evt -> markActionPerformed(evt)); 
	    mnemonics.put(KeyEvent.VK_M, evt -> markActionPerformed(evt));
	    
	    btnEntrance = GuiToolkit.createButton("icons/robot.png", ROBOT.getString("Tooltip.Entrance"), evt -> entranceActionPerformed(evt)); 
	    mnemonics.put(KeyEvent.VK_E, evt -> entranceActionPerformed(evt));

	    editorToolbar.add(btnUp);
	    editorToolbar.add(btnEntrance);
	    editorToolbar.add(btnForward);
	    editorToolbar.add(btnStone);
	    editorToolbar.add(btnRock);
	    editorToolbar.add(btnChooseColorStone);
	    editorToolbar.add(btnSaveAs);
	    editorToolbar.add(btnSave);
	    editorToolbar.add(btnDown);
	    editorToolbar.add(btnLeft);
	    editorToolbar.add(btnBack);
	    editorToolbar.add(btnRight);
	    editorToolbar.add(btnMark);
	    editorToolbar.add(btnChooseColorMark);
	    editorToolbar.add(btnLoad);
	    editorToolbar.add(btnRemove);

	    getContentPane().add(editorToolbar, BorderLayout.SOUTH);
	    
		view.addKeyListener(this);
	    editorToolbar.addKeyListener(this);
	    btnUp.addKeyListener(this);
	    btnDown.addKeyListener(this);
	    btnLeft.addKeyListener(this);
	    btnRight.addKeyListener(this);
	    btnForward.addKeyListener(this);
	    btnBack.addKeyListener(this);
	    btnChooseColorStone.addKeyListener(this);
	    btnChooseColorMark.addKeyListener(this);
	    btnRemove.addKeyListener(this);
	    btnStone.addKeyListener(this);
	    btnRock.addKeyListener(this);
	    btnMark.addKeyListener(this);
	    btnEntrance.addKeyListener(this);
	    btnSave.addKeyListener(this);
	    btnSaveAs.addKeyListener(this);
	    btnLoad.addKeyListener(this);
	    
	    rangeToolbar = new JPanel();
	    rangeToolbar.setLayout(new GridLayout(1,6));
	    
	    txtMinX   = GuiToolkit.createTextField();
	    lblRangeX = GuiToolkit.createLabel(ROBOT.getString("Range.X"));
	    lblRangeX.setHorizontalAlignment(JTextField.CENTER);
	    txtMaxX   = GuiToolkit.createTextField();
	    
	    txtMinY   = GuiToolkit.createTextField();
	    lblRangeY = GuiToolkit.createLabel(ROBOT.getString("Range.Y"));
	    lblRangeY.setHorizontalAlignment(JTextField.CENTER);
	    txtMaxY   = GuiToolkit.createTextField();

	    
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
		world.setEntranceCursor();
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
	private void chooseColorStoneActionPerformed(ActionEvent evt) {
		Color color = JColorChooser.showDialog(this, RobotLanguage.ROBOT.getString("Color.Title.Stone"), world.getStoneColor());
		if (color != null) {
			world.setStoneColor(color);
		}
	}
	private void chooseColorMarkActionPerformed(ActionEvent evt) {
		Color color = JColorChooser.showDialog(this, RobotLanguage.ROBOT.getString("Color.Title.Mark"), world.getMarkColor());
		if (color != null) {
			world.setMarkColor(color);
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
            	activeFile = filechooser.getSelectedFile();
            	try (FileOutputStream fos = new FileOutputStream(activeFile)) {
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
	public void saveActionPerformed(ActionEvent ae) {
		try {
			if (activeFile != null) {
            	try (FileOutputStream fos = new FileOutputStream(activeFile)) {
            		world.save(fos);
            	}
			} else {
				saveAsActionPerformed(ae);
			}
		} catch (TransformerException | ParserConfigurationException | IOException io) {
			JOptionPane.showMessageDialog(this, Messages.getString("Save.Error.Title"), io.getLocalizedMessage(),
					JOptionPane.ERROR_MESSAGE);
		}
	}	
	public void loadActionPerformed(ActionEvent ae) {
        try {
            if (filechooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            	activeFile = filechooser.getSelectedFile();
            	try (FileInputStream fis = new FileInputStream(filechooser.getSelectedFile())) {
            		world.load(fis);
            		IntRange xr = world.getxRange();
            		IntRange yr = world.getyRange();
            		txtMinX.setText(String.valueOf(xr.getMin()));
            		txtMaxX.setText(String.valueOf(xr.getMax()));
            		txtMinY.setText(String.valueOf(yr.getMin()));
            		txtMaxY.setText(String.valueOf(yr.getMax()));
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
