package de.lathanda.eos.gui.structogram;

import java.awt.Color;
import java.awt.Font;

import de.lathanda.eos.gui.diagram.Drawing;
import de.lathanda.eos.gui.diagram.LoopUnit;

/**
 * Schleife.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class Loop extends Unit {
    private final String label;
    private final Sequence sequence;
    private final boolean pre;
    private final static Font FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    private final static float BAR_WIDTH = 7;
    private float textx;
    private float texty;    
    Loop(LoopUnit loop) {
        sequence = new Sequence(loop.getSequence());
        label = loop.getLabel();
        pre = loop.isPre();
    }
    @Override
    protected void layout(Drawing d) {
        sequence.layout(d);
        d.setFont(FONT);
        float textwidth = d.stringWidth(label);
        float textheight = d.getHeight();
        if (textwidth + 2*BORDER > sequence.getWidth() + BAR_WIDTH) {
            width = textwidth + 2*BORDER;
            sequence.setWidth(width - BAR_WIDTH);
        } else {
            width = BAR_WIDTH + sequence.getWidth();
        }     
        if (pre) {
            texty = d.getAscent() + BORDER;
            sequence.setOffsetY(textheight + 2 * BORDER);
        } else {
            texty = sequence.getHeight() + d.getAscent() + BORDER;
        }
        textx = (width - textwidth) / 2;
        sequence.setOffsetX(BAR_WIDTH);
        height = sequence.getHeight() + textheight + 2 * BORDER;
    }

    @Override
    protected void drawUnit(Drawing d) {
        d.setColor(Color.BLACK);
        d.setFont(FONT);
        d.drawRect(0, 0, width, height);
        d.drawString(label, textx, texty);    
        sequence.draw(d);
    }

    @Override
    public void setWidth(float width) {
        float diff = width - this.width;
        super.setWidth(width);
        textx = textx + diff / 2;
        sequence.setWidth(width - BAR_WIDTH);
    }
    
}
