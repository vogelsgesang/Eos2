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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
public class WorldEditor extends JFrame implements KeyListener {
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
	public WorldEditor( ) {
		super(RobotLanguage.ROBOT.getString("Title"));
        this.setIconImage(LOGO);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        world = new World(this);
        //TODO use graphic card dependent factory and/or
        //resolve gl errors in order to use alternative renderer
        view = new WorldPanelOpenGLNoShader(world);
        world.setShowCursor(true);
        setLocation(0, 0);
    	
        filechooser = new JFileChooser();
        filechooser.addChoosableFileFilter(new FileNameExtensionFilter(RobotLanguage.ROBOT.getString("File.EOW"), "eow"));
        filechooser.setCurrentDirectory(new File("."));
    	
    	getContentPane().add(view, BorderLayout.CENTER);
    	
		editorToolbar = new JPanel();
		editorToolbar.setLayout(new GridLayout(2,7));
		
	    btnUp = new JButton(new ImageIcon(getClass().getResource("/icons/arrow-up-double-2.png")));
	    mnemonics.put(KeyEvent.VK_A, evt -> upActionPerformed(evt));
	    btnUp.addActionListener(evt -> upActionPerformed(evt));

	    btnDown = new JButton(new ImageIcon(getClass().getResource("/icons/arrow-down-double-2.png"))); 
	    mnemonics.put(KeyEvent.VK_Z, evt -> downActionPerformed(evt));
	    btnDown.addActionListener(evt -> downActionPerformed(evt));

	    btnLeft = new JButton(new ImageIcon(getClass().getResource("/icons/arrow-left-2.png")));
	    mnemonics.put(KeyEvent.VK_LEFT,	evt -> leftActionPerformed(evt));
	    btnLeft.addActionListener(evt -> leftActionPerformed(evt));
	    
	    btnRight = new JButton(new ImageIcon(getClass().getResource("/icons/arrow-right-2.png"))); 
	    mnemonics.put(KeyEvent.VK_RIGHT, evt -> rightActionPerformed(evt));
	    btnRight.addActionListener(evt -> rightActionPerformed(evt));
	    
	    btnForward = new JButton(new ImageIcon(getClass().getResource("/icons/arrow-up-2.png")));
	    mnemonics.put(KeyEvent.VK_UP, evt -> forwardActionPerformed(evt));
	    btnForward.addActionListener(evt -> forwardActionPerformed(evt));
	    
	    btnBack = new JButton(new ImageIcon(getClass().getResource("/icons/arrow-down-2.png")));
	    mnemonics.put(KeyEvent.VK_DOWN, evt -> backActionPerformed(evt));
	    btnBack.addActionListener(evt -> backActionPerformed(evt));

	    btnSaveAs = new JButton(new ImageIcon(getClass().getResource("/icons/document-save-3.png")));
	    btnSaveAs.addActionListener(evt -> saveAsActionPerformed(evt));

	    btnLoad = new JButton(new ImageIcon(getClass().getResource("/icons/document-open-5.png")));
	    btnLoad.addActionListener(evt -> loadActionPerformed(evt));	    
	    
	    btnChooseColor = new JButton(new ImageIcon(getClass().getResource("/icons/colorize-2.png"))); 
	    mnemonics.put(KeyEvent.VK_C, evt -> chooseColorActionPerformed(evt));
	    btnChooseColor.addActionListener(evt -> chooseColorActionPerformed(evt));
	    
	    btnRemove = new JButton(new ImageIcon(getClass().getResource("/icons/trash-empty-3.png")));
	    mnemonics.put(KeyEvent.VK_X, evt -> removeActionPerformed(evt));
	    btnRemove.addActionListener(evt -> removeActionPerformed(evt));
	    
	    btnStone = new JButton(new ImageIcon(getClass().getResource("/icons/kcmdf-3.png")));
	    mnemonics.put(KeyEvent.VK_S, evt -> stoneActionPerformed(evt));
	    btnStone.addActionListener(evt -> stoneActionPerformed(evt));
	    
	    btnRock = new JButton(new ImageIcon(getClass().getResource("/icons/kblackbox-3.png"))); 
	    mnemonics.put(KeyEvent.VK_R, evt -> rockActionPerformed(evt));
	    btnRock.addActionListener(evt -> rockActionPerformed(evt));
	    
	    btnMark = new JButton(new ImageIcon(getClass().getResource("/icons/view-pim-notes.png"))); 
	    mnemonics.put(KeyEvent.VK_M, evt -> markActionPerformed(evt));
	    btnMark.addActionListener(evt -> markActionPerformed(evt));
	    
	    btnEntrance = new JButton(new ImageIcon(getClass().getResource("/icons/im-user.png")));
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
                world.save(new FileOutputStream(filechooser.getSelectedFile()));
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
                world.load(new FileInputStream(filechooser.getSelectedFile()));
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
}
