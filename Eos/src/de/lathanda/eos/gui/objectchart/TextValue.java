package de.lathanda.eos.gui.objectchart;

import de.lathanda.eos.gui.diagram.Drawing;
/**
 * Text Attributwert.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.9.4
 */
public class TextValue extends Unit {
	private String text;
	private float y;
	
	public TextValue(String text) {
		super();
		if (text != null) {
			this.text = text;
		} else {
			this.text = "\uFFFD";
		}
	}

	@Override
	public void drawUnit(Drawing d) {
		d.drawString(text, 0, y);
	}

	@Override
	public void layout(Drawing d) {
		this.height = d.getHeight();
		this.width = d.stringWidth(text);
		this.y = d.getAscent();
	}

}
