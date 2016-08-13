package de.lathanda.eos.gui.flowchart;

import java.awt.Color;
import java.awt.Font;

import de.lathanda.eos.common.ProgramSequence;
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
    private float arrowxE;
    private float arrowyE;
    private float arrowxB;
    private float arrowyB;
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
        } else {
            width = sequence.getWidth();
        }        
        texty = d.getAscent() + BORDER;
        textx = (width - textwidth) / 2;
        height = sequence.getHeight() + textheight + 2 * BORDER + 2 * SPACE;
        arrowyE = sequence.getHeight() + textheight + 2 * BORDER + SPACE;
        arrowxE = width / 2;
        arrowxB = arrowxE;
        arrowyB = textheight + 2 * BORDER;
        sequence.setOffsetX((width - sequence.getWidth())/2);
        sequence.setOffsetY(textheight + 2 * BORDER + SPACE);
    }

    @Override
    protected void drawUnit(Drawing d) {
        d.setColor(Color.BLACK);
        d.setFont(FONT);
        d.drawString(title, textx, texty);    
        sequence.draw(d);
        
        if (!sequence.neverEnds) {
            d.drawArrow(arrowxE, arrowyE, arrowxE, arrowyE + SPACE, 3);
        }       
        
        if (sequence.needsIncomingArrow) {
            d.drawArrow(arrowxB, arrowyB, arrowxB, arrowyB + SPACE, 3);
        } else {
            d.drawLine(arrowxB, arrowyB, arrowxB, arrowyB + SPACE);
        }        
    }
}
