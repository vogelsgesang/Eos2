package de.lathanda.eos.gui.flowchart;

import de.lathanda.eos.common.interpreter.ProgramNode;
import de.lathanda.eos.gui.diagram.Drawing;

/**
 * Befehlskasten.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class Statement extends ConnectedUnit {
    private float textheight;
    private float textwidth;
    private float textx;
    private float texty;
    private final String label;
    Statement(ProgramNode n) {
        label = n.getLabel();
        font = STANDARD_FONT;
    }
    Statement(String label) {
        this.label = label;
    }
    @Override
	public void drawUnit(Drawing d) {
        d.drawRect(0, 0, width, height);
        d.drawString(label, textx, texty);
    };
    @Override
	public void layoutUnit(Drawing d) {
        textwidth = d.stringWidth(label);
        textheight = d.getHeight();
        width = textwidth + 2 * BORDER;
        height = textheight + 2 * BORDER;
        textx = width / 2 - textwidth / 2;
        texty = d.getAscent() + BORDER;
    };      
}
