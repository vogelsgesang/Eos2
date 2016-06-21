package de.lathanda.eos.gui.objectchart;

import java.awt.Font;
import java.util.LinkedList;
import de.lathanda.eos.base.Readout;
import de.lathanda.eos.base.Readout.Attribut;
import de.lathanda.eos.gui.diagram.Drawing;
/**
 * Objektkarte
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.9.4
 */
public class ObjectCard extends Unit {
	/**
	 * Übersetzung der Attribute und Konstanter Werte.
	 */
    private final static Font FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    private Unit header;
    private LinkedList<Property> properties;
	private float yLine;
	public ObjectCard(String name, String cls, Readout readout) {
		header = new TextValue(name + ":" + cls);
		properties = new LinkedList<>();
		LinkedList<Attribut> attr = new LinkedList<>();
		readout.getAttributes(attr);
		for(Attribut a : attr) {
			properties.add(new Property(lm.getName(a.name), a.value));
		}
	}
	@Override
	public void drawUnit(Drawing d) {
		d.setFont(FONT);
		d.drawRect(0, 0, width, height);
		d.drawLine(0, yLine, width, yLine);
		header.draw(d);
		for(Unit property : properties) {
			property.draw(d);
		}
	}
	@Override
	public void layout(Drawing d) {
        d.setFont(FONT);
        header.layout(d);
		for(Unit property : properties) {
			property.layout(d);
		}
		header.setOffsetX(INDENT);
		header.setOffsetX(INDENT);
		yLine = header.getHeight() + 2 * INDENT;
		if (properties.isEmpty()) {
			width = header.width + 2 * INDENT;
			height = yLine;
		} else {
			float alignedWidth = Property.align(d, properties);
			width = Math.max(header.width, alignedWidth) + 2 * INDENT;
			float y = yLine + INDENT;
			for(Unit property : properties) {
				property.setOffsetX(INDENT);
				property.setOffsetY(y);
				y += property.getHeight() + INDENT;
			}	
			height = y;
		}        
	}
}
