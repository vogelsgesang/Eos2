package de.lathanda.eos.game;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import de.lathanda.eos.base.Picture2D;

/**
 * \brief Spielfeld
 * 
 * AWT Komponente um ein Spiel anzuzeigen.
 * 
 * @author Lathanda
 */
/*package*/ class GamePanel extends JPanel
	implements KeyEventDispatcher, MouseListener, MouseMotionListener {

	/**
	 * \brief ID
	 */
	private static final long serialVersionUID = -2303718998046834449L;
	/**
	 * \brief Welt mit den Weltobjekten
	 */
	private Game game;
	
	/**
	 * \brief Zeichenobjekt
	 */
	private Picture2D p;
	/**
	 * \brief angepeilte Framerate
	 */
	double fps;
    private int prefHeight;
    private int prefWidth;

	/**
	 * Erzeugt einen neues Zeichenobjekt mit 30 Bildern pro Sekunde.
	 * 
	 * @param game Spiel
	 * @param width Breite in mm
	 * @param height Höhe in mm
	 */
	public GamePanel(Game game, double width, double height)  {
		this(game, 30, Color.WHITE, width, height);
	}

	/**
	 * Erzeugt einen neues Zeichenobjekt.
	 * 
	 * @param game Spiel
	 * @param fps angestrebte Bilder pro Sekunde
	 * @param width Breite in mm
	 * @param height Höhe in mm
	 */
	public GamePanel(Game game, int fps, double width, double height) {
		this(game, fps, Color.WHITE, width, height);
	}
	
	/**
	 * Erzeugt einen neues Zeichenobjekt.
	 * 
	 * @param game Spiel
	 * @param fps angestrebte Bilder pro Sekunde
	 * @param background Hintergrundfarbe
	 * @param width Breite in mm
	 * @param height Höhe in mm
	 */
	public GamePanel(Game game, int fps, Color background, double width, double height) {
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
		.addKeyEventDispatcher(this);
		this.p = new Picture2D(width, height, this);
        prefHeight = p.mm2pixel(height);
        prefWidth  = p.mm2pixel(width);
        this.game = game;
		this.fps = fps;
		setForeground(Color.BLACK);
		setBackground(background);
		// add events
		addMouseListener(this);
		addMouseMotionListener(this);
	}
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(prefWidth, prefHeight);
    }
	/**
	 * Dieses Ereignis wird ignoriert
	 * @param e Mausereignis
	 */
	public void mouseClicked(MouseEvent e) {
		// no action
	}

	/**
	 * Registriert das Drücken der Maustaste.
	 * @param e Mausereignis
	 */
	public void mousePressed(MouseEvent e) {
		game.mouseDown(e.getButton());
	}

	/**
	 * Registriert das Loslassen der Maustaste.
	 * @param e Mausereignis
	 */
	public void mouseReleased(MouseEvent e) {
		game.mouseUp(e.getButton());
	}

	/**
	 * Dieses Ereignis wird ignoriert
	 * @param e Mausereignis
	 */
	public void mouseEntered(MouseEvent e) {
		// no action
	}

	/**
	 * Dieses Ereignis wird ignoriert
	 * @param e Mausereignis
	 */
	public void mouseExited(MouseEvent e) {
		// no action
	}

	// MouseMotionListener
	/**
	 * Speichert die aktuelle Mausposition, die Tatsache, dass es ein Ziehen ist
	 * wird ignoriert.
	 * @param e Mausereignis
	 */
	public void mouseDragged(MouseEvent e) {
		game.mousePosition(p.pointFromPixel(e.getX(), e.getY()));
	}

	/**
	 * Speichert die aktuelle Mausposition.
	 * @param e Mausereignis
	 */
	public void mouseMoved(MouseEvent e) {
		game.mousePosition(p.pointFromPixel(e.getX(), e.getY()));
	}

	/**
	 * Behandelt Tastaturereignisse direkt bei Manager, wodurch der Fokus keine
	 * Rolle spielt.
	 * @param k Tastatur Ereignis
	 */
	public boolean dispatchKeyEvent(KeyEvent k) {
		switch (k.getID()) {
		case KeyEvent.KEY_PRESSED:
			game.keyDown(k.getKeyCode());
			break;
		case KeyEvent.KEY_RELEASED:
			game.keyUp(k.getKeyCode());
			break;
		}

		return false;
	}
	/**
	 * Fügt das Weltobjekt der Welt hinzu.
	 *  
	 * @param sprite
	 *            neues Objekt, möglichst ein Objekt der Klasse PlayObjekt
	 */
	public void addSprite(Sprite sprite) {
		game.addSprite(sprite);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
        AffineTransform af = ((Graphics2D)g).getTransform();
		p.setGraphics((Graphics2D)g);
		game.render(p);
        ((Graphics2D)g).setTransform(af);		
	}
}