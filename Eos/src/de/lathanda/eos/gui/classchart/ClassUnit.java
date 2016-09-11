package de.lathanda.eos.gui.classchart;

import java.util.Collections;
import java.util.LinkedList;
import de.lathanda.eos.common.interpreter.AutoCompleteInformation;
import de.lathanda.eos.gui.diagram.Drawing;
import de.lathanda.eos.interpreter.Type;

public class ClassUnit {
	private float width;
	private float height;
    private float yLabel;
    private float yProperties;
    
    protected final static float BORDER = 1;	
    String name;
	LinkedList<AutoCompleteInformation> methods = new LinkedList<>();
	LinkedList<AutoCompleteInformation> properties = new LinkedList<>();
	LinkedList<TextBlock> text = new LinkedList<>();
	public ClassUnit(Type t) {
		name = t.getName();
		for(AutoCompleteInformation aci:t.getAutoCompletes()) {
			switch (aci.getType()) {
			case AutoCompleteInformation.METHOD:
				methods.add(aci);
				break;
			case AutoCompleteInformation.PROPERTY:
				properties.add(aci);
				break;
			}			
		}
		Collections.sort(methods,    (a,b) -> {return a.getLabel().compareTo(b.getLabel());});
		Collections.sort(properties, (a,b) -> {return a.getLabel().compareTo(b.getLabel());});		
	}

	
	public void drawUnit(Drawing d) {
		d.drawRect(0, 0, width, height);
		d.drawRect(0, yLabel, width, yProperties);
		for(TextBlock t:text) {
			t.drawUnit(d);
		}
	}


	public void layout(Drawing d) {
		//header
		TextBlock header = new TextBlock(name);
		header.layout(d);
		width = header.width;
		height = header.height + BORDER * 2;
		text.add(header);
		yLabel = height;
		//properties
		height += BORDER;
		for(AutoCompleteInformation aci:properties) {
			TextBlock prop = new TextBlock(aci.getLabelLong());
			prop.layout(d);
			prop.move(BORDER, height);
			height += prop.height + BORDER;
			if (prop.width > width) {
				width = prop.width;
			}
			text.add(prop);
		}
		yProperties = height;
		height += BORDER;
		for(AutoCompleteInformation aci:methods) {
			TextBlock meth = new TextBlock(aci.getLabelLong());
			meth.layout(d);
			meth.move(BORDER, height);
			height += meth.height + BORDER;
			text.add(meth);
			if (meth.width > width) {
				width = meth.width;
			}
		}
		//adjust width
		width += 2*BORDER;
		header.move((width - header.width) / 2, 0);		
	}
    public final void draw(Drawing d) {
        d.pushTransform();
        drawUnit(d);
        d.popTransform();
    }
    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }
    class TextBlock {
    	float width;
    	float height;
    	float x;
    	float y;
    	String text;
    	public TextBlock(String text) {
    		this.text = text;
    	}
    	public void drawUnit(Drawing d) {
    		d.drawString(text, x, y);
    	}
    	public void layout(Drawing d) {
    		this.height = d.getHeight();
    		this.width = d.stringWidth(text);
    		this.y = d.getAscent();
    	}    	
    	public void move(float dx, float dy) {
    		x += dx;
    		y += dy;
    	}
    }
}
