package de.lathanda.eos.gui.objectchart;

import de.lathanda.eos.gui.diagram.Drawing;
import de.lathanda.eos.robot.Direction;
/**
 * Zeichnet Symbol für die Richtung.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.9.4
 */
public class DirectionValue extends Unit {
	private Direction direction;
	
	public DirectionValue(Direction direction) {
		super();
		this.direction = direction;
	}

	@Override
	public void drawUnit(Drawing d) {
		switch (direction) {
		case NORTH:
			d.drawArrow(2, 5, 2, 0, 1);
			break;
		case SOUTH:
			d.drawArrow(2, 1, 2, 4, 1);
			break;
		case WEST:
			d.drawArrow(4, 3, 0, 3, 1);
			break;
		case EAST:
			d.drawArrow(0, 3, 4, 3, 1);
			break;
		}
	}

	@Override
	public void layout(Drawing d) {
		width = 6;
		height = 5;
	}

}
