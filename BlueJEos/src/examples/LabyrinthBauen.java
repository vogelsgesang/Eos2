package examples;

import java.awt.Color;
import java.io.FileOutputStream;

import de.lathanda.eos.game.tools.Labyrinth;
import de.lathanda.eos.robot.Direction;
import de.lathanda.eos.robot.World;


public class LabyrinthBauen {
	private static final int WIDTH = 20;
	private static final int HEIGHT = 20;
	private static final int SEED = 42;
	private static final Color WALL = Color.DARK_GRAY;
	private static final String FILENAME = "labyrinth.eow";
	public static void main(String[] args) {
		Labyrinth laby = new Labyrinth(2*WIDTH+1, 2*HEIGHT+1);
		World world = new World();
		world.toggleEntrance(0, 0, 0, Direction.EAST);
		world.toggleEntrance(1, 1, 0, Direction.NORTH);
		laby.createLabyrinth(SEED);
		for(int x = 0; x < 2*WIDTH+1; x++) {
			for(int y = 0; y < 2*HEIGHT+1; y++) {
				if (laby.getCell(x, y) == Labyrinth.WALL) {
					world.setStoneColor(WALL);
					world.setRock(x, y, 0);
					world.setRock(x, y, 1);
					world.setRock(x, y, 2);
				}
			}
		}
		world.setMark(2*WIDTH-1, 2*HEIGHT-1);
		try (FileOutputStream file = new FileOutputStream(FILENAME)){
			world.save(file);
		} catch (Exception e) {
			System.out.println("Speichern gescheitert wegen "+e.getMessage());
		}
	}
}
