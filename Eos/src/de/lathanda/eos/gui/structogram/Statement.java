package de.lathanda.eos.gui.structogram;

import de.lathanda.eos.common.interpreter.ProgramNode;
import de.lathanda.eos.gui.diagram.Drawing;

import java.awt.Color;
import java.awt.Font;

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
    private final static Font FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);

    Statement(ProgramNode n) {
        label = n.getLabel();
    }
    @Override
    protected void drawUnit(Drawing d) {
        d.setFont(FONT);
        d.setColor(Color.BLACK);
        d.drawLine(0,0, width, 0);
        d.drawLine(0,0, 0, height);
        d.drawString(label, textx, texty);
    };
    @Override
    protected void layout(Drawing d) {
        d.setFont(FONT);
        textwidth = d.stringWidth(label);
        textheight = d.getHeight();
        textx = BORDER;
        texty = d.getAscent() + BORDER;
        width = textwidth + 2 * BORDER;
        height = textheight + 2 * BORDER;
    };      
}
