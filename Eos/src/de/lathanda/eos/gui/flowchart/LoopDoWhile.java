package de.lathanda.eos.gui.flowchart;

import de.lathanda.eos.gui.diagram.Drawing;
import de.lathanda.eos.gui.diagram.LoopUnit;
import java.awt.Color;
import java.awt.Font;

/**
 * Schleife mit Endbedingung.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class LoopDoWhile extends Unit {
    private final Sequence sequence;
    private final Diamond diam;
    private final String yes = lm.getLabel("Yes");
    private final String no = lm.getLabel("No");

    private final static Font FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
     
    private float yesx;
    private float yesy;     
    private float nox;
    private float noy;     
    
    LoopDoWhile(LoopUnit lu) {
    	this.needsIncomingArrow = false;
        diam = new Diamond(lu.getLabel());
        sequence = new Sequence(lu.getSequence());
    }

    @Override
    protected void layout(Drawing d) {
        sequence.layout(d);
        diam.layout(d);
        
                
        width = (float)Math.max(sequence.getWidth(), diam.getWidth()) + 2 * SPACEX;
        sequence.center(width);;
        sequence.setOffsetY(SPACE);
        diam.center(width);
        diam.setOffsetY(2 * SPACE + sequence.getHeight());
                
        d.setFont(FONT);
        yesx = diam.getX(0) - d.stringWidth(yes) - BORDER;
        yesy = diam.getY(0) - BORDER;
        nox = diam.getX(3) + BORDER;
        noy = diam.getY(3) + BORDER + d.getAscent();
        
        height =  SPACE + sequence.getHeight() + SPACE + diam.getHeight() + SPACE;
    }

    @Override
    protected void drawUnit(Drawing d) {
        //condition diamond
        diam.draw(d);
        
        d.setColor(Color.BLACK);
        d.setFont(FONT);                       
        //false arrow
        d.drawString(no, nox, noy);    
        d.drawLine(diam.getX(3), diam.getY(3), diam.getX(3), diam.getY(3) + SPACE);
        
        //true jump start arrow
        d.drawString(yes, yesx, yesy);
        d.drawLine(diam.getX(0), diam.getY(0), 0, diam.getY(0));
        d.drawLine(0, diam.getY(0), 0, 0);
        d.drawLine(0, 0, (int)width / 2, 0);
        d.drawArrow(width / 2, 0, width / 2, SPACE, 3);
        
        //body -> diamond            
        d.drawArrow(diam.getX(1), diam.getY(1) - SPACE, diam.getX(1), diam.getY(1),  3);
        
        //loop body
        sequence.draw(d);
        
    }
    
}
