package de.lathanda.eos.gui.objectchart;

import de.lathanda.eos.base.Scaling;
import de.lathanda.eos.gui.diagram.Unit;
import de.lathanda.eos.gui.diagram.Drawing;
/**
 * Zeichnet Symbol für die Skalierung.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.9.4
 */
public class ScalingValue extends Unit {
	private Scaling scaling;
	
	public ScalingValue(Scaling scaling) {
		super();
		this.scaling = scaling;
	}

	@Override
	public void drawUnit(Drawing d) {
		d.drawRect(0, 2, 8, 4);
		switch (scaling) {
		case STRETCH:
			d.drawEllipse(0, 2, 8, 4);
			break;
		case CUT:
			d.drawEllipse(0, 0, 8, 8);
			break;
		case FIT:
			d.drawEllipse(2, 2, 4, 4);
			break;
		}
	}

	@Override
	public void layoutUnit(Drawing d) {
		width = 8;
		height = 8;
	}

}
