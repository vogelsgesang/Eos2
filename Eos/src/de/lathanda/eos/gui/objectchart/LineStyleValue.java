package de.lathanda.eos.gui.objectchart;

import de.lathanda.eos.base.LineStyle;
import de.lathanda.eos.gui.diagram.Drawing;
/**
 * Zeichnet Symbol für die Linienart.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.9.4
 */
public class LineStyleValue extends Unit {
	private LineStyle lineStyle;
	
	public LineStyleValue(LineStyle lineStyle) {
		super();
		this.lineStyle = lineStyle;
	}

	@Override
	public void drawUnit(Drawing d) {
		switch (lineStyle) {
		case SOLID:
			d.fillRect(0, 3.75f, 8f, 0.5f);
			break;
		case INVISIBLE:
			//nothing
			break;
		case DOTTED:
			for(int i = 0; i < 8; i += 2) {
				d.fillRect(i, 3.75f, 0.5f, 0.5f);
			}
			break;
		case DASHED_DOTTED:
			for(int i = 0; i < 8; i += 4) {
				d.fillRect(i, 3.75f, 0.5f, 0.5f);
				d.fillRect(i+1f, 3.75f, 2.5f, 0.5f);
			}
			break;
		case DASHED:
			for(int i = 0; i < 8; i += 3) {
				d.fillRect(i, 3.75f, 0.5f, 0.5f);
				d.fillRect(i, 3.75f, 2f, 0.5f);
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
