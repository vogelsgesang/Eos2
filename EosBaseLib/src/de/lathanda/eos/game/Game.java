package de.lathanda.eos.game;

import java.awt.Color;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.TreeSet;

import de.lathanda.eos.base.Picture2D;
import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.game.geom.CollisionDetection;

/**
 * \brief Spielthread
 *
 * Diese Klasse verwaltet alle Sprites. Sie ruft die Methode update() periodisch
 * auf.
 *
 * Beispiel 
 * \code 
 *   Game game = new Game(300, 200, Color.GREEN, 100, "Titel"); 
 * \endcode
 *
 * @author Lathanda

 */
public class Game implements Runnable {

	/**
	 * Die vier Zustände des Spiels.
	 *
	 * @author Lathanda
	 *
	 */
	private enum State {

		PREPARE, // / Initialisierung
		RUNNING, // / Die Simulation läuft
		PAUSE, // / Die Simulation ist vorübergehend angehalten
		SHUTDOWN
		// / Die Simulation ist gerade dabei alle Resourcen freizugeben
	};

	/**
	 * \brief aktueller Zustand
	 */
	private State state = State.PREPARE;
	/**
	 * \brief Simulationsschritte pro Sekunde
	 */
	private int fps;
	/**
	 * \brief Simulationsschrittzähler seit dem Beginn.
	 */
	private long round = 0;
	/**
	 * \brief Tastaturzustand, Taste gedrückt?
	 */
	private final boolean[] keys = new boolean[256];
	/**
	 * \brief gedrückte Tasten, wird nach jeder Runde gelöscht
	 */
	private final boolean[] keysPressed = new boolean[256];
	/**
	 * \brief neue gedrückte Tasten
	 */
	private final boolean[] keysPress = new boolean[256];
	/**
	 * \brief Maustastenzustand, ist Maustaste gedrückt?
	 */
	private final boolean[] mouseBtn = new boolean[20];
	/**
	 * \brief neue gedrückte Maustasten
	 */
	private final boolean[] mouseBtnPress = new boolean[20];
	/**
	 * \brief gedrückte Maustasten, wird nach jeder Runde gelöscht
	 */
	private final boolean[] mouseBtnPressed = new boolean[20];
	/**
	 * \brief Position der Maus
	 */
	private Point mousePos = new Point(0d, 0d);

	/**
	 * \brief Nach Zeichenreihenfolge sortierte Liste der Sprites.
	 */
	private final TreeSet<Sprite> drawingList = new TreeSet<>();
	/**
	 * \brief Liste aller Sprites für Simulation
	 */
	private final TreeMap<Integer, Sprite> sprites = new TreeMap<>();
	/**
	 * \brief Zu löschende Sprites
	 */
	private final LinkedList<Sprite> pendingDelete = new LinkedList<>();
	/**
	 * \brief neue Sprites
	 */
	private final LinkedList<Sprite> pendingAdd = new LinkedList<>();

	/**
	 * \brief Kollisionserkennungsalgorithmus
	 */
	public CollisionDetection colDetect;
	// draw is running
	private final Object DRAW_LOCK = new Object();
	// sprite list change is queued
	private final Object CHANGE_LOCK = new Object();
	// keyboard status is updating
	private final Object INPUT_LOCK = new Object();

	private GameFrame gameFrame;

	/**
	 * Erzeugt ein neues Spiel die mit 30 Aktuallisierungen pro Sekunde
	 * arbeitet.
	 */
	public Game() {
		this(200, 200, Color.WHITE, 30, "");
	}

	/**
	 * Erzeugt ein neues Spiel, das mit der angegebenen Anzahl von
	 * Aktualisierungen pro Sekunde arbeitet. Diese Zahl sollte je nach Rechner
	 * nicht zu gross gewählt werden. Je nach Rechner und Anzahl der Sprites
	 * sind Werte zwischen 20 und 100 sinnvoll. Unter 20 führt zu sichbarem
	 * ruckeln, über 100 führt zu sehr unregelmässigen Aufrufsequenzen, da die
	 * meisten Betriebsysteme Threads nur auf grob 10ms genau ansteuern.
	 *
	 * @param fps
	 *            Anzahl der Simulationen pro Sekunde, dieser Wert wird im
	 *            Mittel erzwungen. Die einzelnen Intervalle können abweichen.
	 */
	public Game(int fps) {
		this(200, 200, Color.WHITE, fps, "");
	}

	public Game(double width, double height) {
		this(width, height, Color.WHITE, 30, "");
	}

	public Game(double width, double height, String title) {
		this(width, height, Color.WHITE, 30, title);
	}

	public Game(double width, double height, Color back, String title) {
		this(width, height, back, 30, title);
	}

	public Game(double width, double height, Color back, int fps, String title) {
		gameFrame = new GameFrame(width, height, back, this, title);
		colDetect = new CollisionDetection(this);
		this.fps = fps;
		start();
	}

	/**
	 * Wird vom Zeichenobjekt aufgerufen, um das Spiel zu informieren, dass eine
	 * Taste gedrückt wurde.
	 *
	 * @param keyCode
	 *            Tasturcode der gedrückten Taste
	 */
	public void keyDown(int keyCode) {
		synchronized (INPUT_LOCK) {
			try {
				keys[keyCode] = true;
				keysPress[keyCode] = true;
			} catch (ArrayIndexOutOfBoundsException e) {
			}
		}
	}

	/**
	 * Wird vom Zeichenobjekt aufgerufen, um das Spiel zu informieren, dass eine
	 * Taste losgelassen wurde.
	 *
	 * @param keyCode
	 *            Tasturcode der losgelassenen Taste
	 */
	public void keyUp(int keyCode) {
		try {
			keys[keyCode] = false;
		} catch (ArrayIndexOutOfBoundsException e) {
		}
	}

	/**
	 * Wird vom Zeichenobjekt aufgerufen, um das Spiel zu informieren, dass eine
	 * Maustaste gedrückt wurde.
	 *
	 * @param button
	 *            Nummer der Maustaste
	 */
	public void mouseDown(int button) {
		synchronized (INPUT_LOCK) {
			try {
				mouseBtn[button] = true;
				mouseBtnPress[button] = true;
			} catch (ArrayIndexOutOfBoundsException e) {
			}
		}
	}

	/**
	 * Wird vom Zeichenobjekt aufgerufen, um das Spiel zu informieren, dass eine
	 * Maustaste losgelassen wurde.
	 *
	 * @param button
	 *            Nummer der Maustaste
	 */
	public void mouseUp(int button) {
		try {
			mouseBtn[button] = false;
		} catch (ArrayIndexOutOfBoundsException e) {
		}
	}

	/**
	 * Wird vom Zeichenobjekt aufgerufen, um das Spiel zu informieren, wo sich
	 * die Maus befindet.
	 *
	 * @param p
	 *            Aktuelle Position der Maus
	 */
	public void mousePosition(Point p) {
		mousePos = p;
	}

	/**
	 * Setzt die Ausführung fort.
	 */
	public void resume() {
		state = State.RUNNING;
	}

	/**
	 * Unterbricht die Ausführung
	 */
	public void pause() {
		state = State.PAUSE;
	}

	/**
	 * Startet die Simulation. Achtung! Diese Methode darf nur einmal aufgerufen
	 * werden.
	 */
	public void start() {
		state = State.RUNNING;
		(new Thread(this)).start();
	}

	/**
	 * Beendet die Simulation
	 */
	public void shutDown() {
		state = State.SHUTDOWN;
	}

	/**
	 * Wird vom Zeichenobjekt aufgerufen, um alle Objekte zeichnen zulassen.
	 *
	 * @param p
	 *            Zeichenfläche
	 */
	public void render(Picture2D p) {
		synchronized (DRAW_LOCK) {
			for (Sprite s : drawingList) {
				try {
					p.restoreStyles();
					synchronized (s) {
						s.render(p);
					}
				} catch (Throwable t) {
					// drawing exception shall not stop execution
					t.printStackTrace(System.err);
					System.err.println("error occured while render, removing " + s);
					removeSprite(s);
				}
			}
		}

	}

	/**
	 * Führt einen Aktuallisierungsschritt aus.
	 */
	protected void step() {
		// perform step
		for (Sprite s : sprites.values()) {
			// the following sync block, enforces data sync.
			// Without it Sprite properties would have to be volatile
			try {
				synchronized (s) {
					s.update(round, this);

				}
			} catch (Throwable t) {
				// a single object shall not stop execution
				t.printStackTrace();
				System.err.println("error occured while step execution, removing " + s);
				removeSprite(s);
			}
		}
		synchronized (INPUT_LOCK) {
			// clear keypressed
			System.arraycopy(keysPress, 0, keysPressed, 0, keysPress.length);
			Arrays.fill(keysPress, false);
			// clear mousepressed
			System.arraycopy(mouseBtnPress, 0, mouseBtnPressed, 0, mouseBtnPress.length);
			Arrays.fill(mouseBtnPress, false);
		}
		// list changes
		Sprite[] del;
		Sprite[] add;
		synchronized (CHANGE_LOCK) {
			del = new Sprite[pendingDelete.size()];
			pendingDelete.toArray(del);
			pendingDelete.clear();
			add = new Sprite[pendingAdd.size()];
			pendingAdd.toArray(add);
			pendingAdd.clear();
		}
		if (del.length > 0 || add.length > 0) {
			synchronized (DRAW_LOCK) {
				for (int i = 0; i < del.length; i++) {
					colDetect.removeObject(del[i]);
					drawingList.remove(del[i]);
					sprites.remove(del[i].getID());
					del[i].cleanup(this);
				}
				for (int i = 0; i < add.length; i++) {
					add[i].init(this);
					sprites.put(add[i].getID(), add[i]);
					drawingList.add(add[i]);
					colDetect.addObject(add[i]);
				}
			}
		}
		// check for collisions
		colDetect.checkCollision();

	}

	/**
	 * Arbeitsmethode des Simulationsthread. Darf nicht explizit aufgerufen
	 * werden.
	 */
	@Override
	public void run() {
		long start = System.nanoTime();
		round = 0;
		long actual_time;
		long debit = -1;
		while (state != State.SHUTDOWN) {
			actual_time = System.nanoTime();
			if (debit <= actual_time) {
				switch (state) {
				case PAUSE:
					// adjust start to be in sync with pause
					start = actual_time - round * 1000000000 / fps;
					break;
				case PREPARE:
					// adjust start to be in sync with pause
					start = actual_time - round * 1000000000 / fps;
					break;
				case RUNNING:
					step();
					gameFrame.repaint();
					// manage time intervals
					round++;
					debit = start + round * 1000000000 / fps;
					break;
				default:
					throw new RuntimeException("unknown state");
				}

			} else {
				try {
					long pause = debit - actual_time;
					Thread.sleep(pause / 1000000, (int) (pause % 1000000));
				} catch (Exception e) {
					// shit happens
				}
			}
		}
	}

	/**
	 * Entfernt alle Sprites
	 */
	public void clearAll() {
		synchronized (CHANGE_LOCK) {
			pendingAdd.clear();
			pendingDelete.addAll(sprites.values());
		}
	}

	/*
	 * Prüft, ob die Taste gedrückt ist. Der KeyCode entspricht den Konstanten
	 * aus KeyEvent. zB KeyEvent.VK_Z, KeyEvent.VK_UP
	 */
	public boolean isKeyDown(int keyCode) {
		try {
			return keys[keyCode];
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}

	/*
	 * Prüft, ob die Taste gedrückt wurde. Der KeyCode entspricht den Konstanten
	 * aus KeyEvent. zB KeyEvent.VK_Z, KeyEvent.VK_UP
	 */
	public boolean isKeyPressed(int keyCode) {
		try {
			return keysPressed[keyCode];
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}

	/*
	 * Fügt ein Sprite in das Spiel hinzu.
	 * 
	 * @param obj Sprite
	 */
	public void addSprite(Sprite obj) {
		if (obj == null) {
			return;
		}
		synchronized (CHANGE_LOCK) {
			pendingAdd.add(obj);
		}
	}

	/*
	 * Löscht ein Sprite aus dem Spiel
	 * 
	 * @param Sprite
	 */
	public void removeSprite(Sprite obj) {
		synchronized (CHANGE_LOCK) {
			pendingDelete.add(obj);
			pendingAdd.remove(obj);
		}
	}

	/*
	 * Liefert die Position der Mouse
	 * 
	 * @return Mouseposition
	 */
	public Point getMouse() {
		return new Point(mousePos);
	}

	/*
	 * Prüft ob die Maustaste gedrückt ist.
	 * 
	 * @param buttom Nummer des Knopfes
	 */
	public boolean isMouseDown(int button) {
		try {
			return mouseBtn[button];
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}

	/*
	 * Prüft ob die Maustaste gedrückt wurde. Der erste Aufruf löscht den
	 * gedrückt Zustand.
	 * 
	 * @param buttom Nummer des Knopfes
	 */
	public boolean isMousePressed(int button) {
		try {
			return mouseBtnPressed[button];
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}
}
