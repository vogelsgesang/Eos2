package de.lathanda.eos.gui.objectchart;

import de.lathanda.eos.base.FillStyle;
import de.lathanda.eos.gui.diagram.Drawing;
/**
 * Zeichnet Symbol für die Füllart.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.9.4
 */
public class FillStyleValue extends Unit {
	private FillStyle fillStyle;
	
	public FillStyleValue(FillStyle fillStyle) {
		super();
		this.fillStyle = fillStyle;
	}

	@Override
	public void drawUnit(Drawing d) {
		d.drawRect(0, 0, 8, 8);
		switch (fillStyle) {
		case TRANSPARENT:
			//nothing
			break;
		case RULED:
			for(int i = 2; i < 8; i += 2) {
				d.drawLine(0, i, 8, i);
			}
			break;
		case FILLED:
			d.fillRect(0, 0, 8, 8);
			break;
		case CHECKED:
			for(int i = 2; i < 8; i += 2) {
				d.drawLine(0, i, 8, i);
				d.drawLine(i, 0, i, 8);
			}			
			break;
		}

	}

	@Override
	public void layout(Drawing d) {
		width = 8;
		height = 8;
	}

}
