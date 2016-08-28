package de.lathanda.eos.gui.structogram;

import java.awt.Color;
import java.awt.Font;

import de.lathanda.eos.common.interpreter.ProgramSequence;
import de.lathanda.eos.gui.diagram.Drawing;

/**
 * Unterprogramm.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class Procedure extends Unit {
    private final Sequence sequence;
    private final String title;
    private final static Font FONT = new Font(Font.SANS_SERIF, Font.BOLD, 14);
    private float textx;
    private float texty;
    public Procedure(String title, ProgramSequence programSequence) {
        this.title = title;
        sequence = new Sequence(programSequence);
    }

    @Override
    protected void layout(Drawing d) {
        sequence.layout(d);
        d.setFont(FONT);
        float textwidth = d.stringWidth(title);
        float textheight = d.getHeight();
        if (textwidth + 2*BORDER > sequence.getWidth()) {
            width = textwidth + 2*BORDER;
            sequence.setWidth(width);
        } else {
            width = sequence.getWidth();
        }        
        texty = d.getAscent() + BORDER;
        textx = (sequence.getWidth() - textwidth) / 2;
        height = sequence.getHeight() + textheight + 2 * BORDER;
        sequence.setOffsetY(textheight + 2 * BORDER);
    }

    @Override
    protected void drawUnit(Drawing d) {
        d.setColor(Color.BLACK);
        d.setFont(FONT);
        d.drawString(title, textx, texty);    
        sequence.draw(d);
    }
}
