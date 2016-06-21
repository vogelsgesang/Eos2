package de.lathanda.eos.gui.flowchart;

import de.lathanda.eos.gui.diagram.Drawing;
import de.lathanda.eos.interpreter.Node;
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
    Statement(Node n) {
        label = n.getLabel();
    }
    Statement(String label) {
        this.label = label;
    }
    @Override
    protected void drawUnit(Drawing d) {
        d.setColor(Color.BLACK);
        d.setFont(FONT);
        d.drawRect(0, 0, width, height);
        d.drawString(label, textx, texty);
    };
    @Override
    protected void layout(Drawing d) {
        d.setFont(FONT);
        textwidth = d.stringWidth(label);
        textheight = d.getHeight();
        width = textwidth + 2 * BORDER;
        height = textheight + 2 * BORDER;
        textx = width / 2 - textwidth / 2;
        texty = d.getAscent() + BORDER;
    };      
}
