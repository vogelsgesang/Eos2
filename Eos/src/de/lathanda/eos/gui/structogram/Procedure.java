package de.lathanda.eos.gui.structogram;

import de.lathanda.eos.common.interpreter.ProgramSequence;
import de.lathanda.eos.gui.diagram.Drawing;
import de.lathanda.eos.gui.diagram.Unit;

/**
 * Unterprogramm.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class Procedure extends Unit {
    private final Sequence sequence;
    private final String title;

    private float textx;
    private float texty;
    public Procedure(String title, ProgramSequence programSequence) {
        this.title = title;
        sequence = new Sequence(programSequence);
        font = HEADER_FONT;
    }

    @Override
	public void layoutUnit(Drawing d) {
        sequence.layout(d);

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
	public void drawUnit(Drawing d) {
        d.drawString(title, textx, texty);    
        sequence.draw(d);
    }
}
