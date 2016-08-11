package de.lathanda.eos.gui.objectchart;

import java.awt.Color;
import java.text.MessageFormat;
import de.lathanda.eos.base.Alignment;
import de.lathanda.eos.base.FillStyle;
import de.lathanda.eos.base.LineStyle;
import de.lathanda.eos.base.Readout;
import de.lathanda.eos.base.Scaling;
import de.lathanda.eos.gui.diagram.Drawing;
import de.lathanda.eos.gui.diagram.MemoryEntry;
import de.lathanda.eos.interpreter.parsetree.ReservedVariables;
import de.lathanda.eos.spi.LanguageManager;
import de.lathanda.eos.util.ConcurrentLinkedList;
/**
 * Grundlegende Anzeigeeinheit
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.9.4
 */
public abstract class Unit {
	/**
	 * übersetzung der Attribute und Konstanter Werte.
	 */
    protected static final LanguageManager lm = LanguageManager.getInstance();
    protected final static float INDENT = 1;
    protected float width;
    protected float height;
    private float offsetX = 0;
    private float offsetY = 0;
    protected final static float BORDER = 1;
	public abstract void drawUnit(Drawing d);
    public abstract void layout(Drawing d);
    public final void draw(Drawing d) {
        d.pushTransform();
        d.translate(offsetX, offsetY);
        drawUnit(d);
        d.popTransform();
    }
    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public float getHeight() {
        return height;
    }
    public Unit formatValue(Object value) {
    	if (value == null) {
    		return new TextValue(lm.getName("1null"));
    	}
		if (value instanceof Boolean) {
			return new TextValue(lm.getName(value.toString()));
		} else if (value instanceof Integer) {
			return new TextValue(value.toString());
		} else if (value instanceof Number) {
			return new TextValue(MessageFormat.format("{0,number,#.####}", value));
		} else if (value instanceof ConcurrentLinkedList<?>) {			
			return new ListValue((ConcurrentLinkedList<?>)value);
		} else if (value instanceof Color) {
			return new ColorValue((Color)value);
		} else if (value instanceof LineStyle) {
			return new LineStyleValue((LineStyle)value);
		} else if (value instanceof FillStyle) {
			return new FillStyleValue((FillStyle)value);
		} else if (value instanceof Alignment) {
			return new AlignmentValue((Alignment)value);
		} else if (value instanceof Scaling) {
			return new ScalingValue((Scaling)value);
		} else {
			return lm.createUnit(value);			
		}    	
    }
    public static Unit create(MemoryEntry v) {
		if (v.name.equals(ReservedVariables.RESULT)) {
			return new Property(lm.getName(ReservedVariables.RESULT), v.data);
		} else if (v.name.equals(ReservedVariables.WINDOW)) {
			return new ObjectCard(lm.getName(ReservedVariables.WINDOW), v.type, (Readout)v.data);
		} else if (v.name.startsWith(ReservedVariables.REPEAT_TIMES_INDEX)) {
			return new Property(lm.getName(ReservedVariables.REPEAT_TIMES_INDEX), v.data);
		} else if (v.data instanceof Readout) {
			return new ObjectCard(v.name, v.type, (Readout)v.data);
		} else {
			return new Property(v.name, v.data);
		}
    }
    public class FormattedText {
    	public final String text;
    	public final Color color;
		public FormattedText(String text, Color color) {
			super();
			this.text = text;
			this.color = color;
		}
		public FormattedText(String text) {
			super();
			this.text = text;
			this.color = Color.BLACK;
		}
    }
}
