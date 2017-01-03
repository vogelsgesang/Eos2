package de.lathanda.eos.gui.flowchart;

import de.lathanda.eos.gui.diagram.Drawing;
import de.lathanda.eos.gui.diagram.LoopUnit;

/**
 * Bedingte Schleife.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class LoopWhile extends ConnectedUnit {
    private final Diamond diam;
    private final Sequence sequence;
    private final String yes = lm.getLabel("Yes");
    private final String no = lm.getLabel("No");

    private float yesx;
    private float yesy;     
    private float nox;
    private float noy;     
    
    LoopWhile(LoopUnit lu) {
        diam = new Diamond(lu.getLabel());
        sequence = new Sequence(lu.getSequence());
        font = STANDARD_FONT;
    }

    @Override
	public void layoutUnit(Drawing d) {
        sequence.layout(d);
        diam.layout(d);
        
        width = Math.max(sequence.getWidth(), diam.getWidth()) + 2 * SPACEX;
        diam.centerX(width);
        sequence.centerX(width);
        sequence.setOffsetY(diam.getHeight() + SPACE);
        
        yesx = diam.getX(3) + 2*BORDER;
        yesy = diam.getY(3) + d.getAscent();
        nox = diam.getX(0) - d.stringWidth(no) - BORDER;
        noy = diam.getY(0) - BORDER;
        height = diam.getHeight() + SPACE + sequence.getHeight() + SPACE + SPACE;
    }

    @Override
	public void drawUnit(Drawing d) {
        //condition diamond
        diam.draw(d);
               
        //false skip line
        d.drawString(no, nox, noy);    
        d.drawLine(diam.getX(0), diam.getY(0), 0, diam.getY(0));
        d.drawLine(0, diam.getY(0), 0, height);
        d.drawLine(0, height, width / 2, height);
        
        //jump start arrow
        d.drawLine(width / 2, height - 2 * SPACE, width / 2, height - SPACE);
        d.drawLine(width / 2, height - SPACE, width, height - SPACE);
        d.drawLine(width, height - SPACE, width, diam.getY(2));
        d.drawArrow(width, diam.getY(2), diam.getX(2), diam.getY(2), 2);

        //true arrow
        d.drawString(yes, yesx, yesy);    
        d.drawArrow(diam.getX(3), diam.getY(3), diam.getX(3), diam.getY(3) + SPACE, 2);
        
        //loop body
        sequence.draw(d);
        
    }
    
}
