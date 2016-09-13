package de.lathanda.eos.gui.objectchart;

import de.lathanda.eos.base.Alignment;
import de.lathanda.eos.gui.diagram.Unit;
import de.lathanda.eos.gui.diagram.Drawing;
/**
 * Zeichnet Symbol für die Textausrichtung.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.9.4
 */
public class AlignmentValue extends Unit {
	private Alignment alignment;
	
	public AlignmentValue(Alignment alignment) {
		super();
		this.alignment = alignment;
	}

	@Override
	public void drawUnit(Drawing d) {
		d.drawRect(0, 0, 8, 8);
		switch(alignment) {
		case TOP:
			d.drawLine(1, 1, 7, 1);
			d.drawLine(2, 1, 7, 1);
			d.drawLine(3, 1, 7, 1);
			d.drawLine(4, 1, 7, 1);
			break;
		case BOTTOM:
			d.drawLine(6, 1, 7, 1);
			d.drawLine(5, 1, 7, 1);
			d.drawLine(4, 1, 7, 1);
			d.drawLine(3, 1, 7, 1);
			break;
		case LEFT:
			d.drawLine(1, 1, 4, 1);
			d.drawLine(1, 2, 2, 2);
			d.drawLine(1, 3, 5, 3);
			d.drawLine(1, 4, 3, 4);			
			d.drawLine(1, 5, 4, 5);			
			d.drawLine(1, 6, 3, 6);			
			break;
		case RIGHT:
			d.drawLine(3, 1, 6, 1);
			d.drawLine(2, 2, 6, 2);
			d.drawLine(4, 3, 6, 3);
			d.drawLine(3, 4, 6, 4);			
			d.drawLine(5, 5, 6, 5);			
			d.drawLine(4, 6, 6, 6);					
			break;
		case CENTER:
			d.drawLine(3, 2, 4, 2);
			d.drawLine(2, 3, 5, 3);
			d.drawLine(2, 4, 5, 4);
			d.drawLine(3, 5, 4, 5);			
			break;
		}

	}

	@Override
	public void layoutUnit(Drawing d) {
		width = 8;
		height = 8;
	}

}
