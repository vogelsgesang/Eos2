package de.lathanda.eos.gui.flowchart;

import de.lathanda.eos.gui.diagram.Drawing;
import de.lathanda.eos.gui.diagram.LoopUnit;

/**
 * Schleife mit Endbedingung.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class LoopDoWhile extends ConnectedUnit {
    private final Sequence sequence;
    private final Diamond diam;
    private final String yes = lm.getLabel("Yes");
    private final String no = lm.getLabel("No");

    private float yesx;
    private float yesy;     
    private float nox;
    private float noy;     
    
    LoopDoWhile(LoopUnit lu) {
    	this.needsIncomingArrow = false;
        diam = new Diamond(lu.getLabel());
        sequence = new Sequence(lu.getSequence());
        font = STANDARD_FONT;
    }

    @Override
	public void layoutUnit(Drawing d) {
        sequence.layout(d);
        diam.layout(d);
        
                
        width = (float)Math.max(sequence.getWidth(), diam.getWidth()) + 2 * SPACEX;
        sequence.centerX(width);;
        sequence.setOffsetY(SPACE);
        diam.centerX(width);
        diam.setOffsetY(2 * SPACE + sequence.getHeight());
                
        yesx = diam.getX(0) - d.stringWidth(yes) - BORDER;
        yesy = diam.getY(0) - BORDER;
        nox = diam.getX(3) + BORDER;
        noy = diam.getY(3) + BORDER + d.getAscent();
        
        height =  SPACE + sequence.getHeight() + SPACE + diam.getHeight() + SPACE;
    }

    @Override
	public void drawUnit(Drawing d) {
        //condition diamond
        diam.draw(d);
                      
        //false arrow
        d.drawString(no, nox, noy);    
        d.drawLine(diam.getX(3), diam.getY(3), diam.getX(3), diam.getY(3) + SPACE);
        
        //true jump start arrow
        d.drawString(yes, yesx, yesy);
        d.drawLine(diam.getX(0), diam.getY(0), 0, diam.getY(0));
        d.drawLine(0, diam.getY(0), 0, 0);
        d.drawLine(0, 0, width / 2, 0);
        d.drawArrow(width / 2, 0, width / 2, SPACE, 2);
        
        //body -> diamond            
        d.drawArrow(diam.getX(1), diam.getY(1) - SPACE, diam.getX(1), diam.getY(1),  2);
        
        //loop body
        sequence.draw(d);
        
    }
    
}
