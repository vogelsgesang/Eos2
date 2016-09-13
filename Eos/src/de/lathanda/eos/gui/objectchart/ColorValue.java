package de.lathanda.eos.gui.objectchart;

import java.awt.Color;

import de.lathanda.eos.gui.diagram.Unit;
import de.lathanda.eos.gui.diagram.Drawing;
/**
 * Zeichnet die Farbe.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.9.4
 */
public class ColorValue extends Unit {
	private Color color;
	
	public ColorValue(Color color) {
		super();
		this.color = color;
	}

	@Override
	public void drawUnit(Drawing d) {
		Color org = d.getColor();
		d.setColor(color);
		d.fillRect(0, 1, 4, 4);
		d.setColor(Color.BLACK);
		d.drawRect(0, 1, 4, 4);
		d.setColor(org);
	}

	@Override
	public void layoutUnit(Drawing d) {
		width = 5;
		height = 6;
	}

}
