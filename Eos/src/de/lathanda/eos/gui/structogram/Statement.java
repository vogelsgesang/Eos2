package de.lathanda.eos.gui.structogram;

import de.lathanda.eos.common.interpreter.ProgramNode;
import de.lathanda.eos.gui.diagram.Drawing;
import de.lathanda.eos.gui.diagram.Unit;

/**
 * Befehlskasten.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class Statement extends Unit {
    private float textheight;
    private float textwidth;
    private float textx;
    private float texty;
    private final String label;

    Statement(ProgramNode n) {
        label = n.getLabel();
        font = STANDARD_FONT;
    }
    @Override
	public void drawUnit(Drawing d) {
        d.drawLine(0,0, width, 0);
        d.drawLine(0,0, 0, height);
        d.drawString(label, textx, texty);
    };
    @Override
	public void layoutUnit(Drawing d) {
        textwidth = d.stringWidth(label);
        textheight = d.getHeight();
        textx = BORDER;
        texty = d.getAscent() + BORDER;
        width = textwidth + 2 * BORDER;
        height = textheight + 2 * BORDER;
    };      
}
