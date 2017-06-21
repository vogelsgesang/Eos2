package de.lathanda.eos.robot.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.util.Map.Entry;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import de.lathanda.eos.base.ResourceLoader;
import de.lathanda.eos.robot.Column;
import de.lathanda.eos.robot.Coordinate;
import de.lathanda.eos.robot.Cube;
import de.lathanda.eos.robot.Entrance;
import de.lathanda.eos.robot.Robot;
import de.lathanda.eos.robot.World;
import de.lathanda.eos.robot.World.IntRange;
import de.lathanda.eos.robot.geom3d.Material;
import de.lathanda.eos.robot.geom3d.NFaceException;
import de.lathanda.eos.robot.geom3d.ObjLoader;
import de.lathanda.eos.robot.geom3d.Polyhedron;

/**
 * Grafische Darstellung der Welt.
 * 
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.9.6
 */
public class WorldPanelOpenGLNoShader extends GLCanvas
		implements GLEventListener, MouseListener, MouseMotionListener, MouseWheelListener {
	private static final long serialVersionUID = -7254900679929645124L;
	private GLU glu;
	private FPSAnimator animator;

	private double[] cameraPositionXYZ;
	private double cameraRotationX; // 360°
	private double cameraRotationZ; // 360°

	private int lastMouseX, lastMouseY;
	private int lastButton;
	private World world;

	public WorldPanelOpenGLNoShader(World world) {
		super(new GLCapabilities(GLProfile.get(GLProfile.GL2)));
		this.world = world;
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension(screen.width / 2, screen.height / 2));

		init();
	}

	private void init() {
		glu = new GLU();

		// initial camera position
		cameraPositionXYZ = new double[] { 0d, -10, 6 };
		cameraRotationX = -60;
		cameraRotationZ = 0;

		// link controller
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addGLEventListener(this);

		// start animation
		animator = new FPSAnimator(this, 30, true);
		animator.start();

	}

	@Override
	public void display(GLAutoDrawable glad) {
		GL2 gl = glad.getGL().getGL2();

		// prepare rendering
		gl.glClearColor(0.25f, 0.6f, 1f, 0f);

		// store status
		gl.glPushMatrix();
		gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);

		// apply camera position
		gl.glRotated(cameraRotationX, 1d, 0d, 0d);
		gl.glRotated(cameraRotationZ, 0d, 0d, 1d);

		// setup rendering parameters
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT | GL.GL_STENCIL_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		// light
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, new float[] { 0.2f, 0.2f, 0.2f, 1f }, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, new float[] { 0.2f, 0.2f, 0.2f, 1f }, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, new float[] { 0.8f, 0.8f, 0.8f, 1f }, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, new float[] { -0.5f, -0.3f, 1f, 0f }, 0);

		renderSkybox(gl);

		gl.glTranslated(-cameraPositionXYZ[0], -cameraPositionXYZ[1], -cameraPositionXYZ[2]);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);

		// render world
		renderWorld(gl);

		// render cursor
		if (world.isShowCursor()) {
			renderCursor(gl);
		}
		// end rendering
		gl.glPopAttrib();
		gl.glPopMatrix();
	}

	@Override
	public void dispose(GLAutoDrawable glad) {
		GLObjectBuffer.clear(glad.getGL());
		GLTextureBuffer.clear(glad.getGL());
	}

	@Override
	public void init(GLAutoDrawable glad) {
	}

	@Override
	public void reshape(GLAutoDrawable glad, int x, int y, int width, int height) {
		GL2 gl = glad.getGL().getGL2();
		double ratio = (double) width / (double) height;

		// lighting
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);

		// projection
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(60, ratio, 0.1d, 200d);

		gl.glMatrixMode(GL2.GL_MODELVIEW);

	}

	// Mouse routines
	private static final double ROTATION_SPEED_SIDE = 0.15d;
	private static final double ROTATION_SPEED_UP_DOWN = 0.4d;
	private static final double MOVE_SPEED_SIDE = 0.14d;
	private static final double MOVE_SPEED_UP_DOWN = 0.07d;
	private static final double MOVE_SPEED = 2d;

	@Override
	public void mouseDragged(MouseEvent e) {
		int dX = e.getX() - lastMouseX;
		int dY = e.getY() - lastMouseY;
		switch (lastButton) {
		case MouseEvent.BUTTON1: // Left
			cameraPositionXYZ[0] -= (dX * cos(cameraRotationZ) - dY * sin(cameraRotationZ)) * MOVE_SPEED_SIDE;
			cameraPositionXYZ[1] += (dX * sin(cameraRotationZ) + dY * cos(cameraRotationZ)) * MOVE_SPEED_SIDE;
			break;
		case MouseEvent.BUTTON2: // middle
			cameraPositionXYZ[0] -= dX * cos(cameraRotationZ) * MOVE_SPEED_SIDE;
			cameraPositionXYZ[1] += dX * sin(cameraRotationZ) * MOVE_SPEED_SIDE;
			cameraPositionXYZ[2] += dY * MOVE_SPEED_UP_DOWN;
			break;
		case MouseEvent.BUTTON3: // right
			cameraRotationZ += dX * ROTATION_SPEED_SIDE;
			cameraRotationX += dY * ROTATION_SPEED_UP_DOWN;
			break;
		}
		restrictCamera();
		lastMouseX = e.getX();
		lastMouseY = e.getY();
	}

	private void restrictCamera() {
		if (cameraPositionXYZ[2] < 0.1) {
			cameraPositionXYZ[2] = 0.1;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		lastButton = e.getButton();
		lastMouseX = e.getX();
		lastMouseY = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		lastMouseX = e.getX();
		lastMouseY = e.getY();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		double mouseRotation = e.getPreciseWheelRotation();
		moveCamera(MOVE_SPEED * mouseRotation);
	}

	private void moveCamera(double distance) {
		cameraPositionXYZ[0] += sin(cameraRotationZ) * sin(cameraRotationX) * distance;
		cameraPositionXYZ[1] += cos(cameraRotationZ) * sin(cameraRotationX) * distance;
		cameraPositionXYZ[2] += cos(cameraRotationX) * distance;
		restrictCamera();
	}

	private static double sin(double degree) {
		return Math.sin(degree / 180 * Math.PI);
	}

	private static double cos(double degree) {
		return Math.cos(degree / 180 * Math.PI);
	}

	// rendering routines
	static {
		try {
			skybox = ObjLoader.loadObj("obj/", "skybox.obj");
			markObj = ObjLoader.loadObj("obj/", "mark.obj");
			cubeObj = ObjLoader.loadObj("obj/", "cube.obj");
			rockObj = ObjLoader.loadObj("obj/", "rock.obj");
			robotObj = ObjLoader.loadObj("obj/", "robot.obj", 50000);
			cursorObj = ObjLoader.loadObj("obj/", "cursor.obj");
		} catch (IOException ioe) {
			System.err.println("missing file! " + ioe.getMessage());
			System.exit(-1);
		} catch (NFaceException e) {
			System.err.println("Illegal Face with "+e.getFaces()+" vertices");
			System.exit(-1);
		}
	}

	private static Polyhedron skybox;
	private static Polyhedron markObj;
	private static Polyhedron cubeObj;
	private static Polyhedron rockObj;
	private static Polyhedron robotObj;
	private static Polyhedron cursorObj;

	private static Color CURSOR_COLOR = new Color(1f, 1f, 0f, .5f);
	public static double WIDTH = 1d;
	public static double HEIGHT = 0.707117d;
	public static double DEPTH = 1d;
	private static final Material FLOOR_MATERIAL = new Material(ResourceLoader.loadImage("obj/stone.png"));

	private void renderSkybox(GL2 gl) {
		renderPolyhedron(skybox, Color.BLACK, gl);
	}

	public void renderWorld(GL2 gl) {
		// floor
		GLTextureBuffer floor = GLTextureBuffer.get(FLOOR_MATERIAL, gl); 
		floor.openMaterial(Color.GREEN, gl);
		gl.glBegin(GL2.GL_QUADS);
		gl.glNormal3f(0f, 0f, 1f);
		IntRange rangeX = world.getxRange();
		IntRange rangeY = world.getyRange(); 
		double left = rangeX.getMin() - 0.5d;
		double width = rangeX.size();
		double bottom = rangeY.getMin() - 0.5d;
		double height = rangeY.size();
		gl.glTexCoord2d(0, 0);
		gl.glVertex3d(left * WIDTH, bottom * DEPTH, 0);
		gl.glTexCoord2d(0, height);
		gl.glVertex3d(left * WIDTH, (bottom + height) * DEPTH, 0);
		gl.glTexCoord2d(width, height);
		gl.glVertex3d((left + width) * WIDTH, (bottom + height) * DEPTH, 0);
		gl.glTexCoord2d(width, 0);
		gl.glVertex3d((left + width) * WIDTH, bottom * DEPTH, 0);
		gl.glEnd();
		floor.closeMaterial(gl);

		// render columns with stones
		synchronized (world.getColumns()) {
			for (Entry<Coordinate, Column> col : world.getColumns().entrySet()) {
				Coordinate co = col.getKey();
				gl.glPushMatrix();
				gl.glTranslated(co.getX() * WIDTH, co.getY() * DEPTH, 0d);
				renderColumn(col.getValue(), gl);
				gl.glPopMatrix();
			}
		}

		// render robots
		synchronized (world.getRobots()) {
			for (Robot r : world.getRobots()) {
				renderRobot(r, gl);
			}
		}
		// render entrances
		synchronized (world.getEntrances()) {
			for (Entrance e : world.getEntrances()) {
				renderEntrance(e, gl);
			}
		}
	}

	private void renderCursor(GL2 gl) {
		gl.glPushMatrix();
		gl.glTranslated(world.getCursorX() * WIDTH, world.getCursorY() * DEPTH, world.getCursorZ() * HEIGHT);
		renderPolyhedron(cursorObj, CURSOR_COLOR, gl);
		gl.glPopMatrix();
	}

	private void renderEntrance(Entrance e, GL2 gl) {
		gl.glPushMatrix();
		gl.glTranslated(e.x * WIDTH, e.y * DEPTH, e.z * HEIGHT);
		gl.glRotatef(e.d.getAngle(), 0f, 0f, 1f);
		renderPolyhedron(robotObj, new Color(0.5f, 0.5f, 0.5f, 0.5f), gl);
		gl.glPopMatrix();
	}

	private void renderRobot(Robot r, GL2 gl) {
		gl.glPushMatrix();
		gl.glTranslated(r.getX() * WIDTH, r.getY() * DEPTH, r.getZ() * HEIGHT);
		gl.glRotatef(r.getDirection().getAngle(), 0f, 0f, 1f);
		renderPolyhedron(robotObj, r.getRobotColor(), gl);
		gl.glPopMatrix();
	}

	private void renderColumn(Column column, GL2 gl) {
		gl.glPushMatrix();
		boolean doMark = true;
		Cube[] cubes = column.getCubes();
		for (int i = 0; i < cubes.length; i++) {
			if (!cubes[i].isEmpty()) {
				renderCube(cubes[i], gl);
				doMark = true;
			} else if (column.isMarked() && doMark) {
				renderPolyhedron(markObj, Color.YELLOW, gl);
				doMark = false;
			}
			gl.glTranslated(0d, 0d, HEIGHT);
		}
		if (column.isMarked() && doMark) {
			renderPolyhedron(markObj, Color.YELLOW, gl);
		}
		gl.glPopMatrix();
	}

	private void renderCube(Cube cube, GL2 gl) {
		switch (cube.getType()) {
		case 0: // empty
			break;
		case 1: // ground
			break;
		case 2: // stone
			renderPolyhedron(cubeObj, cube.getColor(), gl);
			break;
		case 3: // rock
			renderPolyhedron(rockObj, cube.getColor(), gl);
			break;
		}
	}

	private void renderPolyhedron(Polyhedron poly, Color base, GL2 gl) {
		GLObjectBuffer.get(poly).render(base, gl);
	}
}
