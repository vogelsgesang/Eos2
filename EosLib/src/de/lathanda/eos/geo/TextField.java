package de.lathanda.eos.geo;

import de.lathanda.eos.base.Alignment;
import de.lathanda.eos.base.layout.Dimension;
import de.lathanda.eos.base.Picture;
import java.awt.Color;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Rechteck mit Text.
 *
 * @author Peter (Lathanda) Schneider
 */
public class TextField extends Rectangle {

    private Alignment vertical;
    private Alignment horizontal;
    private boolean autoadjust;
    private boolean dirty = false;
    private int size;
    private Color color;
    private boolean hideBorder;
    private LinkedList<String> text;
    private String fontname;

    public TextField() {
        autoadjust = true;
        vertical = Alignment.CENTER;
        horizontal = Alignment.CENTER;
        size = 16;
        color = Color.BLACK;
        text = new LinkedList<>();
        hideBorder = false;
    }

    @Override
    protected void beforeDrawing(Picture p) {
        super.beforeDrawing(p);
        if (dirty && autoadjust) {
            p.setFont(fontname, size);
            p.setTextSpacing(1, 1);
            p.setTextAlignment(vertical, horizontal);
            Dimension dim = p.getTextDimension(text);
            width = dim.getWidth();
            height = dim.getHeight();
            dirty = false;
        }
    }

    @Override
    protected void drawObject(Picture p) {
        if (!hideBorder) {
            super.drawObject(p);
        }
        p.setFont(fontname, size);
        p.setTextSpacing(1, 1);
        p.setTextAlignment(vertical, horizontal);        
        p.drawText(text, -width/2, -height/2, width, height);
    }

    public void setTextColor(Color color) {
        this.color = color;
        fireDataChanged();
    }

    public Color getTextColor() {
        return color;
    }
    public void setHideBorder(boolean hideBorder) {
        this.hideBorder = hideBorder;
        fireDataChanged();
    }
    public boolean getHideBorder() {
        return hideBorder;
    }
    public void setTextSize(int size) {
        this.size = size;
        dirty = true;
        fireDataChanged();
    }

    public int getTextSize() {
        return size;
    }

    public void setFont(String fontname) {
        this.fontname = fontname;
        dirty = true;
        fireDataChanged();
    }

    public String getFont() {
        return fontname;
    }

    public void setAlignmentVertical(Alignment alignment) {
        vertical = alignment;
        dirty = true;
        fireDataChanged();
    }

    public Alignment getAlignmentVertical() {
        return vertical;
    }

    public Alignment getAlignmentHorizontal() {
        return horizontal;
    }

    public void setAlignmentHorizontal(Alignment alignment) {
        this.horizontal = alignment;
        dirty = true;
        fireDataChanged();
    }

    public void setAutoAdjust(boolean auto) {
        autoadjust = auto;
        dirty = true;
        fireDataChanged();
    }

    public boolean getAutoAdjust() {
        return autoadjust;
    }

    public void appendLine(String text) {
        String[] lines = text.split("\n");
        this.text.addAll(Arrays.asList(lines));
        dirty = true;
        fireDataChanged();
    }
    public void setLine(int linenr, String text) {
    	int linenumber;
    	if (linenr > 0) {
    		linenumber = linenr - 1;
    	} else {
    		linenumber = 0;
    	}
        String[] lines = text.split("\n");
        for(int i = this.text.size(); i < linenumber + lines.length; i++) {
        	this.text.add("");
        }
        for(int i = 0; i < lines.length; i++) {
        	this.text.set(i + linenumber, lines[i]);
        }
        dirty = true;
        fireDataChanged();        
    }
    public void deleteLine() {
    	if (!text.isEmpty()) {
    		text.removeLast();
    		dirty = true;
    		fireDataChanged();
    	}
    }
    public void deleteText() {
    	text.clear();
    	dirty = true;
    	fireDataChanged();    	
    }
    @Override
    public Figure copy() {
        TextField tf = (TextField)super.copy();
        tf.text = new LinkedList<>();
        for(String line : text) {
        	tf.text.add(line);
        }
        return tf;
    }    
    @Override
	public void getAttributes(LinkedList<Attribut> attributes) {
    	super.getAttributes(attributes);
    	attributes.add(new Attribut("vertical", vertical));
        attributes.add(new Attribut("horizontal", horizontal));
        attributes.add(new Attribut("autoadjust", autoadjust));
        attributes.add(new Attribut("fontsize", size));
        attributes.add(new Attribut("fontcolor", color));
        attributes.add(new Attribut("hideborder", hideBorder));
        attributes.add(new Attribut("text", text));
        attributes.add(new Attribut("fontname", fontname));     
	}     
}
