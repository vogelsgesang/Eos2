package de.lathanda.eos.gui.objectchart;

import java.awt.Font;

import de.lathanda.eos.gui.diagram.Drawing;
/**
 * Zeichnet eine Variable oder ein Attribut.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.9.4
 */
public class Property extends Unit {

    private final static Font FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    private String name;
    private Unit value;
	private float xEquals;
	private float yLine;

	public Property(String name, Object data) {
		value = formatValue(data);
		this.name = name;
	}
	@Override
	public void drawUnit(Drawing d) {
		d.drawString(name, 0, yLine);
		d.drawString("=", xEquals, yLine);
		value.draw(d);
	}

	@Override
	public void layout(Drawing d) {
        d.setFont(FONT);
        xEquals = d.stringWidth(name) + INDENT;
        value.layout(d);
        value.setOffsetX(xEquals + d.stringWidth("=") + INDENT);
        yLine = d.getAscent();
        this.height = Math.max(d.getHeight(), value.height);
        this.width = xEquals + INDENT + value.getWidth();
	}
	public static float align(Drawing d, Iterable<Property> properties) {
		float alignedValueWidth = 0;
		float alignedXEquals = 0;
		for (Property property : properties) {
			if (alignedValueWidth < property.value.width) {
				alignedValueWidth = property.value.width;
			}
			if (alignedXEquals < property.xEquals) {
				alignedXEquals = property.xEquals;
			}
		}
		float alignedXValues = alignedXEquals + d.stringWidth("=") + INDENT;
		float alignedWidth = alignedXValues + alignedValueWidth;
		for (Property property : properties) {
			property.xEquals = alignedXEquals;
			property.value.setOffsetX(alignedXValues);
			property.width = alignedWidth;
		}
		return alignedWidth;
	}
}
