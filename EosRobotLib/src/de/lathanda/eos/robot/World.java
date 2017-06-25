package de.lathanda.eos.robot;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.lathanda.eos.base.Readout;
import de.lathanda.eos.base.ResourceLoader;
import de.lathanda.eos.base.event.CleanupListener;
import de.lathanda.eos.robot.exceptions.CubeImmutableException;
import de.lathanda.eos.robot.exceptions.CubeMissingException;
import de.lathanda.eos.robot.exceptions.RobotEntranceMissingException;
import de.lathanda.eos.robot.exceptions.RobotNoSpaceException;
import de.lathanda.eos.robot.exceptions.WorldLoadFailedException;
import de.lathanda.eos.robot.exceptions.WorldNotFoundException;
import de.lathanda.eos.robot.gui.WorldFrame;

/**
 * Eine Welt (World) ist die Grundlage, um mit einem Roboter (Robot) zu arbeiten.
 * Sie erzeugt das 3D Fenster und verwaltet alle Inhalte der Welt.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class World implements CleanupListener, Readout {
	/**
	 * Menge aller Säulen.
	 */
	private final TreeMap<Coordinate, Column> columns = new TreeMap<Coordinate, Column>();
	/**
	 * Liste aller ungenutzten Eingänge.
	 */
	private final LinkedList<Entrance> entrances = new LinkedList<Entrance>();
	/**
	 * Liste aller aktiven Roboter.
	 */
	private final LinkedList<Robot> robots = new LinkedList<Robot>();
	/**
	 * Farbe neuer Steine die über die Welt erzeugt werden.
	 */
	private Color stoneColor = Color.RED;
	/**
	 * Farbe neuer Markierungen die über die Welt erzeugt werden.
	 */
	private Color markColor = Color.YELLOW;
	/**
	 * Genutzter x-Bereich.
	 */
	private IntRange xRange = new IntRange(0, 0);
	/**
	 * Genutzter y-Bereich
	 */
	private IntRange yRange = new IntRange(0, 0);
	/**
	 * Zugeordnetes Fenster
	 */
	private JFrame wf;
	private int cursorX;
	private int cursorY;
	private int cursorZ;
	private Direction cursorD; 
	private boolean showCursor = false;
	private Integer minX;
	private Integer maxX;
	private Integer minY;
	private Integer maxY;
	private static final Column BORDER = new BorderColumn();
	private Random random = new Random();
	/**
	 * Erzeugt eine neue Welt inklusive eines Fensters.
	 */
	public World() {
		entrances.add(new Entrance(0, 0, 0, Direction.EAST));
		wf = new WorldFrame(this);
		wf.setVisible(true);
	}

	/**
	 * Erzeugt eine neue Welt ohne eigenes Fenster.
	 */
	public World(JFrame wf) {
		entrances.add(new Entrance(0, 0, 0, Direction.EAST));
		this.wf = wf;
	}

	/**
	 * Lädt eine Welt aus einer Datei
	 * @param filename
	 * @throws WorldLoadFailedException
	 * @throws WorldNotFoundException
	 */
	public void load(String filename) throws WorldLoadFailedException, WorldNotFoundException {
		
		try (InputStream worldStream = ResourceLoader.getResourceAsStream(filename)) {
			load(worldStream);
		} catch (IOException fnfe) {
			throw new WorldNotFoundException();
		}
	}

	/**
	 * Lädt eine Welt aus einem Datenstrom.
	 * @param worldStream
	 * @throws WorldLoadFailedException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public void load(InputStream worldStream) throws WorldLoadFailedException {
		synchronized (columns) {
			columns.clear();
		}
		synchronized (entrances) {
			entrances.clear();
		}
		synchronized (robots) {
			robots.clear();
		}
		stoneColor = Color.RED;
		xRange = new IntRange(0, 0);
		yRange = new IntRange(0, 0);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc;
			doc = dBuilder.parse(worldStream);
			Element world = doc.getDocumentElement();

			switch (Integer.parseInt(world.getAttribute("version"))) {
			case 1:
				parseVersion1(world);
				break;
			default:
				throw new WorldLoadFailedException();
			}
		} catch (SAXException | IOException | ParserConfigurationException e) {
			throw new WorldLoadFailedException();
		}
	}

	/**
	 * Dekodiert eine Version 1 XML-Datei als Welt. 
	 * @param world
	 */
	private void parseVersion1(Element world) {
		if (world.hasAttribute("minx")) {
			minX = Integer.parseInt(world.getAttribute("minx"));
		} else {
			minX = null;
		}
		if (world.hasAttribute("maxx")) {
			maxX = Integer.parseInt(world.getAttribute("maxx"));
		} else {
			maxX = null;
		}
		if (world.hasAttribute("miny")) {
			minY = Integer.parseInt(world.getAttribute("miny"));
		} else {
			minY = null;
		}
		if (world.hasAttribute("maxy")) {
			maxY = Integer.parseInt(world.getAttribute("maxy"));
		} else {
			maxY = null;
		}
		NodeList nodes = world.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				switch (node.getNodeName()) {
				case "entrance":
					synchronized (entrances) {
						int x = Integer.parseInt(element.getAttribute("x"));
						int y = Integer.parseInt(element.getAttribute("y"));
						int z = Integer.parseInt(element.getAttribute("z"));
						Direction d = Direction.getDirection(Integer.parseInt(element.getAttribute("direction")));
						if (z >= 0) {
							entrances.add(new Entrance(x, y, z, d));
						}
					}
					break;
				case "column":
					Coordinate coordinate = new Coordinate(Integer.parseInt(element.getAttribute("x")),
							Integer.parseInt(element.getAttribute("y")));
					Column column = getColumn(coordinate);
					column.setMark(Integer.parseInt(element.getAttribute("mark")) == 1);
					NodeList cubeNodes = node.getChildNodes();
					for (int j = 0; j < cubeNodes.getLength(); j++) {
						Node cubeNode = cubeNodes.item(j);
						if (cubeNode.getNodeType() == Node.ELEMENT_NODE && cubeNode.getNodeName().equals("cube")) {
							Element cubeElement = (Element) cubeNode;
							Cube cube = Cube.createCube(Integer.parseInt(cubeElement.getAttribute("type")),
									new Color(Integer.parseInt(cubeElement.getAttribute("color"))));
							int level = Integer.parseInt(cubeElement.getAttribute("index"));
							if (level >= 0) {
								column.setCube(level, cube);
							}
						}
					}
					break;
				default:
				}
			}
		}
	}

	/**
	 * Speichert eine Welt als XML Strom
	 * @param targetStream
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 */
	public void save(OutputStream targetStream) throws ParserConfigurationException, TransformerException {
		Document world = buildDocumentVersion1();
		DOMSource worldSource = new DOMSource(world);
		TransformerFactory tf = TransformerFactory.newInstance();
		StreamResult target = new StreamResult(targetStream);
		Transformer serializer = tf.newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.transform(worldSource, target);
	}

	/**
	 * Kodiert eine Welt als Version 1 XML-Datei.
	 * @return
	 * @throws ParserConfigurationException
	 */
	private Document buildDocumentVersion1() throws ParserConfigurationException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.newDocument();

		// create world XML
		Element world = doc.createElement("world");
		world.setAttribute("version", "1");
		if (minX != null) {
			world.setAttribute("minx", Integer.toString(minX));
		}
		if (maxX != null) {		
			world.setAttribute("maxx", Integer.toString(maxX));
		}
		if (minY != null) {		
			world.setAttribute("miny", Integer.toString(minY));
		}
		if (maxY != null) {		
			world.setAttribute("maxy", Integer.toString(maxY));
		}
		doc.appendChild(world);
		synchronized (entrances) {
			for (Entrance entrance : entrances) {
				Element entranceElement = doc.createElement("entrance");
				entranceElement.setAttribute("x", "" + entrance.x);
				entranceElement.setAttribute("y", "" + entrance.y);
				entranceElement.setAttribute("z", "" + entrance.z);
				entranceElement.setAttribute("direction", "" + entrance.d.index);
				world.appendChild(entranceElement);
			}
		}
		synchronized (columns) {
			for (Entry<Coordinate, Column> col : this.columns.entrySet()) {
				Element columnElement = doc.createElement("column");
				Column column = col.getValue();
				Coordinate coordinate = col.getKey();
				columnElement.setAttribute("x", "" + coordinate.getX());
				columnElement.setAttribute("y", "" + coordinate.getY());
				columnElement.setAttribute("mark", (column.isMarked())?"1":"0");
				int index = 0;
				boolean saveColumn = column.isMarked();
				for (Cube cube : column.getCubes()) {
					if (!cube.isEmpty()) {
						Element cubeElement = doc.createElement("cube");
						cubeElement.setAttribute("index", "" + index);
						cubeElement.setAttribute("color", "" + cube.getColor().getRGB());
						cubeElement.setAttribute("type", "" + cube.getType());
						columnElement.appendChild(cubeElement);
						saveColumn = true;
					}
					index++;
				}
				if (saveColumn) {
					world.appendChild(columnElement);
				}
			}
		}
		return doc;
	}
	/**
	 * Füllt den Bereich der Welt zufällig mit Steinen.
	 * @param left Linke Grenze
	 * @param top Obere Grenze
	 * @param right Rechte Grenze
	 * @param bottom Untere Grenze
	 * @param density Wahrscheinlichkeit einen Stein zu setzen. 0 Nie 1 Immer 
	 */
	public void fillRandom(int left, int top, int right, int bottom, double density) {
		for (int x = left; x <= right; x++) {
			for (int y = bottom; y <= top; y++) {
				if (random.nextDouble() <= density) {
					dropStone(x, y);
				}
			}
		}
	}
	/**
	 * Liefert die Säule.
	 * @param x
	 * @param y
	 * @return
	 */
	protected Column getColumn(int x, int y) {
		return getColumn(new Coordinate(x, y));
	}

	/**
	 * Liefert die Säule
	 * @param co
	 * @return
	 */
	protected Column getColumn(Coordinate co) {
		if (minX != null && co.getX() < minX) {
			return BORDER;
		}
		if (minY != null && co.getY() < minY) {
			return BORDER;
		}
		if (maxX != null && co.getX() > maxX) {
			return BORDER;
		}
		if (maxY != null && co.getY() > maxX) {
			return BORDER;
		}
		Column c = null;
		synchronized (columns) {
			c = columns.get(co);
			if (c == null) {
				yRange.extend(co.getY(), 0);
				xRange.extend(co.getX(), 0);
				c = new Column();
				columns.put(co, c);
			}
		}
		return c;
	}

	/**
	 * Roboter betritt die Welt beim ersten freien Eingang.
	 * @param r 
	 */
	public void enter(Robot r) throws RobotEntranceMissingException, RobotNoSpaceException {
		synchronized (entrances) {
			if (entrances.size() == 0) {
				throw new RobotEntranceMissingException();
			}
			Entrance e = entrances.getFirst();
			if (!getColumn(e.x, e.y).isFree(e.z, Robot.SIZE)) {
				throw new RobotNoSpaceException();
			}
			r.initialize(this, e.x, e.y, e.z, e.d);
			entrances.removeFirst();
		}
		synchronized(robots) {
			robots.add(r);
		}
	}

	/**
	 * Erschafft einen neuen Eingang 
	 * @param x
	 * @param y
	 * @param z
	 * @param d 
	 */
	public void toggleEntrance(int x, int y, int z, Direction d) {
		synchronized (entrances) {
			Entrance e;
			Iterator<Entrance> i = entrances.iterator();
			while (i.hasNext()) {
				e = i.next();
				if (e.x == x && e.y == y && e.z == z) {
					i.remove();
					return;
				}
			}
			entrances.add(new Entrance(x, y, z, d));
		}
	}
	/**
	 * Erschafft einen neuen Eingang an der Cursorposition 
	 */
	public void toggleEntranceCursor() {
		toggleEntrance(cursorX, cursorY, cursorZ, cursorD);
	}
	/**
	 * Setzt einen Stein bei diesen Koordinaten ohne Rücksicht darauf was sich in diesem Feld befindet.
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setStone(int x, int y, int z) {
		getColumn(x, y).setCube(z, Cube.createStone(stoneColor));
	}
	/**
	 * Setzt einen farbigen Stein bei diesen Koordinaten ohne Rücksicht darauf was sich in diesem Feld befindet.
	 * @param x
	 * @param y
	 * @param z
	 * @param c Farbe des Steins
	 */
	public void setStone(int x, int y, int z, Color c) {
		getColumn(x, y).setCube(z, Cube.createStone(c));
	}
	public void setStoneCursor() {
		getColumn(cursorX, cursorY).setCube(cursorZ, Cube.createStone(stoneColor));
	}
	/**
	 * Setzt einen unveränderlichen Stein bei diesen Koordinaten ohne Rücksicht darauf was sich in diesem Feld befindet.
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setRock(int x, int y, int z) {
		getColumn(x, y).setCube(z, Cube.createRock(stoneColor));
	}
	
	public void setRockCursor() {
		getColumn(cursorX, cursorY).setCube(cursorZ, Cube.createRock(stoneColor));
	}

	public void toggleMarkCursor() {
		getColumn(cursorX, cursorY).toggleMark(markColor);
	}
	public void setMark(int x, int y, Color c) {
		getColumn(x, y).setMark(c);
	}
	/**
	 * Setzt die Farbe für neue Steine
	 * @param c
	 */
	public void setStoneColor(Color c) {
		this.stoneColor = c;
	}

	/**
	 * Liefert die Farbe für neue Steine.
	 * @return
	 */
	public Color getStoneColor() {
		return this.stoneColor;
	}
	/**
	 * Setzt die Farbe für neue Markierungen
	 * @param c
	 */
	public void setMarkColor(Color c) {
		this.markColor = c;
	}
	/**
	 * Liefert die Farbe für neue Markierungen.
	 * @return
	 */
	public Color getMarkColor() {
		return this.markColor;
	}
	/**
	 * Lässt einen Stein von ganz oben Fallen.
	 * @param x
	 * @param y
	 */
	public void dropStone(int x, int y) {
		getColumn(x, y).dropCube(Cube.createStone(stoneColor));
	}
	/**
	 * Lässt einen farbigen Stein von ganz oben Fallen.
	 * @param x
	 * @param y
	 * @param c Farbe des Steins
	 */
	public void dropStone(int x, int y, Color c) {
		getColumn(x, y).dropCube(Cube.createStone(c));
	}
	/**
	 * Entfernt den Stein von dieser Position.
	 * @param x
	 * @param y
	 * @param z
	 * @throws CubeImmutableException 
	 */
	public void removeStone(int x, int y, int z) throws CubeImmutableException {
		getColumn(x, y).removeCube(z);
	}

	/**
	 * Entfernt alles von dieser Position.
	 * @param x
	 * @param y
	 * @param z
	 */
	public void remove(int x, int y, int z) {
		getColumn(x, y).remove(z);
	}
	public void removeCursor() {
		getColumn(cursorX, cursorY).remove(cursorZ);
	}
	/**
	 * Hebt den obersten Stein auf.
	 * @param x
	 * @param y
	 * @throws CubeMissingException
	 * @throws CubeImmutableException
	 */
	public void pickupStone(int x, int y) throws CubeMissingException, CubeImmutableException {
		getColumn(x, y).pickup();
	}
	
	public TreeMap<Coordinate, Column> getColumns() {
		return columns;
	}

	public LinkedList<Entrance> getEntrances() {
		return entrances;
	}

	public LinkedList<Robot> getRobots() {
		return robots;
	}

	public IntRange getxRange() {
		IntRange result = new IntRange(xRange.min, xRange.max);
		if (minX != null) {
			result.min = minX;
		} else {
			result.min -= 10;
		}
		if (maxX != null) {
			result.max = maxX;
		} else {
			result.max += 10;
		}
		return result;
	}

	public IntRange getyRange() {
		IntRange result = new IntRange(yRange.min, yRange.max);
		if (minY != null) {
			result.min = minY;
		} else {
			result.min -= 10;
		}
		if (maxY != null) {
			result.max = maxY;
		} else {
			result.max += 10;
		}
		return result;
	}

	/**
	 * Integerbereichsklasse
	 * 
	 * @author Peter (Lathanda) Schneider
	 * @since 0.8
	 */
	public static class IntRange implements Iterable<Integer> {
		/**
		 * Minimaler Wert.
		 */
		private int min;
		/**
		 * Maximaler Wert.
		 */
		private int max;

		/**
		 * Erzeugt einen Bereich
		 * @param min
		 * @param max
		 */
		public IntRange(int min, int max) {
			this.min = min;
			this.max = max;
		}

		/**
		 * Erweitert den Bereich sodass der Wert und alle Werte die um den Radius größer und kleiner sind.
		 * @param value
		 * @param radius
		 */
		public void extend(int value, int radius) {
			if (value - radius < min) {
				min = value - radius;
			}
			if (value + radius > max) {
				max = value + radius;
			}
		}

		/**
		 * Anzahl der Zahl im Intervall
		 * @return Anzahl
		 */
		public int size() {
			return max - min + 1;
		}

		@Override
		public Iterator<Integer> iterator() {
			return this.new IntRangeIterator();
		}

		/**
		 * Iterator für ganze Zahlen.
		 * @author Peter (Lathanda) Schneider
		 * @since 0.8
		 */
		private class IntRangeIterator implements Iterator<Integer> {
			/**
			 * Aktueller Index.
			 */
			private int index;

			/**
			 * Neuer Iterator
			 */
			public IntRangeIterator() {
				index = IntRange.this.min;
			}

			@Override
			public boolean hasNext() {
				return index <= IntRange.this.max;
			}

			@Override
			public Integer next() {
				return index++;
			}
		}

		public int getMin() {
			return min;
		}

		public int getMax() {
			return max;
		}
		
	}
	public void setShowCursor(boolean showCursor) {
		this.showCursor = showCursor;
	}
	

	public int getCursorX() {
		return cursorX;
	}

	public int getCursorY() {
		return cursorY;
	}

	public int getCursorZ() {
		return cursorZ;
	}
	public Direction getCursorDirection() {
		return cursorD;
	}
	public void setCursor(int x, int y, int z, Direction d) {
		cursorX = x;
		cursorY = y;
		cursorZ = z;
		cursorD = d;
	}
	public boolean isShowCursor() {
		return showCursor;
	}

	public void moveCursorNorth() {
		cursorY++;
		cursorD = Direction.NORTH;
	}
	public void moveCursorSouth() {
		cursorY--;
		cursorD = Direction.SOUTH;
	}
	public void moveCursorWest() {
		cursorX--;
		cursorD = Direction.WEST;
	}
	public void moveCursorEast() {
		cursorX++;
		cursorD = Direction.EAST;
	}
	public void moveCursorUp() {
		cursorZ++;
	}
	public void moveCursorDown() {
		if (cursorZ > 0) {
			cursorZ--;
		}
	}
	@Override
	public void terminate() {
		if (wf != null) {
			wf.dispose();
			wf = null;
		}
		synchronized (columns) {
			columns.clear();
		}
		synchronized(entrances) {
			entrances.clear();
		}
		synchronized(robots) {
			robots.clear();
		}
	}
    @Override
 	public void getAttributes(LinkedList<Attribut> attributes) {
        attributes.add(new Attribut("robots", robots.size()));
        attributes.add(new Attribut("entrances", entrances.size()));
	}

	public void setRange(Integer minX, Integer maxX, Integer minY, Integer maxY) {
		this.minX= minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;	
	} 
}
