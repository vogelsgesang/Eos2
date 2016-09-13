package de.lathanda.eos.gui.flowchart;

import de.lathanda.eos.gui.diagram.Drawing;
import de.lathanda.eos.gui.diagram.LoopForeverUnit;

/**
 * Endlosschleife.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class LoopForever extends ConnectedUnit {
    private final Sequence sequence;    
    private float center;
    LoopForever(LoopForeverUnit n) {
        sequence = new Sequence(n.getSequence());
        neverEnds = true;
        needsIncomingArrow = false;
        font = STANDARD_FONT;        
    }
    @Override
	public void layoutUnit(Drawing d) {
        sequence.layout(d);
        sequence.setOffsetY(SPACE);
        sequence.setOffsetX(SPACE);
        height = sequence.getHeight() + 2 * SPACE;
        width = sequence.getWidth() + 2 * SPACE;
        center = width / 2;
    }

    @Override
	public void drawUnit(Drawing d) {
        sequence.draw(d);
               
        //draw back jump
        d.drawLine(center, height - SPACE, center, height);
        d.drawLine(center, height, width, height);
        d.drawLine(width, height, width, 0);
        d.drawLine(width, 0, center, 0);
        
        //draw initial arror
        d.drawArrow(center, 0, center, SPACE, 3);
    }
    
}
