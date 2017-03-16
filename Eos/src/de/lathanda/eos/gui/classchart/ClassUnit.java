package de.lathanda.eos.gui.classchart;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import de.lathanda.eos.common.interpreter.AutoCompleteInformation;
import de.lathanda.eos.gui.diagram.Drawing;
import de.lathanda.eos.gui.diagram.TextUnit;
import de.lathanda.eos.gui.diagram.Unit;
import de.lathanda.eos.interpreter.parsetree.Type;

public class ClassUnit extends Unit {
	
    private float yLabel;
    private float yProperties;
    
    protected final static float BORDER = 1;	
    String name;
	LinkedList<AutoCompleteInformation> methods = new LinkedList<>();
	LinkedList<AutoCompleteInformation> properties = new LinkedList<>();
	LinkedList<TextUnit> text = new LinkedList<>();
	ArrayList<String> tooltip = new ArrayList<>();
	public ClassUnit(Type t) {
		name = t.getName();
		for(AutoCompleteInformation aci:t.getAutoCompletes()) {
			switch (aci.getType()) {
			case AutoCompleteInformation.METHOD:
				methods.add(aci);
				break;
			case AutoCompleteInformation.PROPERTY:
			case AutoCompleteInformation.PRIVATE:
				properties.add(aci);
				break;
			}			
		}
		Collections.sort(methods,    (a,b) -> {return a.getLabel().compareTo(b.getLabel());});
		Collections.sort(properties, (a,b) -> {return a.getLabel().compareTo(b.getLabel());});		
	}

	@Override
	public void drawUnit(Drawing d) {
		d.setColor(Color.BLACK);
		d.drawRect(0, 0, width, height);
		d.drawLine(0, yLabel, width, yLabel);
		d.drawLine(0, yProperties, width, yProperties);
		for(TextUnit t:text) {
			t.draw(d);
		}
	}

	@Override
	public void layoutUnit(Drawing d) {
		text.clear();
		//header
		TextUnit header = new TextUnit(name);
		header.setFont(HEADER_FONT);
		header.layout(d);
		header.setOffsetX(INDENT);
		header.setOffsetY(INDENT);
		width = header.getWidth();
		height = header.getHeight() + INDENT * 2;
		text.add(header);
		tooltip.add(null);
		yLabel = height;
		//properties
		height += INDENT;
		for(AutoCompleteInformation aci:properties) {
			TextUnit prop = new TextUnit(aci.getLabelLong());
			prop.layout(d);
			prop.setOffsetX(INDENT);
			prop.setOffsetY(height);
			height += prop.getHeight() + INDENT;
			if (prop.getWidth() > width) {
				width = prop.getWidth();
			}
			text.add(prop);
			tooltip.add("<html><p>"+aci.getTooltip()+"</p></html>");
		}
		yProperties = height;
		height += INDENT;
		for(AutoCompleteInformation aci:methods) {
			TextUnit meth = new TextUnit(aci.getLabelLong());
			meth.layout(d);
			meth.setOffsetX(INDENT);
			meth.setOffsetY(height);
			height += meth.getHeight() + INDENT;
			text.add(meth);
			tooltip.add("<html><p>"+aci.getTooltip()+"</p></html>");
			if (meth.getWidth() > width) {
				width = meth.getWidth();
			}
		}
		//adjust width
		width += 2*BORDER;
	}

	public String getToolTipText(float x, float y) {
		int i = 0;
		for(TextUnit tu : text ) {
			if (tu.getOffsetX() < x && x < tu.getRight() && tu.getOffsetY() < y && y < tu.getBottom()) {
				return tooltip.get(i);
			}
			i++;
		}
		return null;
	}
}
