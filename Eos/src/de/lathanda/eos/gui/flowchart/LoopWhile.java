package de.lathanda.eos.gui.flowchart;

import de.lathanda.eos.gui.diagram.Drawing;
import de.lathanda.eos.interpreter.parsetree.WhileDo;
import java.awt.Color;
import java.awt.Font;

/**
 * Bedingte Schleife.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class LoopWhile extends Unit {
    private final Diamond diam;
    private final Sequence sequence;
    private final String yes = lm.getLabel("Yes");
    private final String no = lm.getLabel("No");

    private final static Font FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    private float yesx;
    private float yesy;     
    private float nox;
    private float noy;     
    
    LoopWhile(WhileDo loop) {
        diam = new Diamond(loop.getCondition().getLabel());
        sequence = new Sequence(loop.getSequence());
    }

    @Override
    protected void layout(Drawing d) {
        sequence.layout(d);
        diam.layout(d);

        d.setFont(FONT);
        
        width = Math.max(sequence.getWidth(), diam.getWidth()) + 2 * SPACEX;
        diam.center(width);
        sequence.setOffsetX(SPACEX);
        sequence.setOffsetY(diam.getHeight() + SPACE);
        
        yesx = diam.getX(3) + BORDER;
        yesy = diam.getY(3) + BORDER + d.getAscent();
        nox = diam.getX(0) - d.stringWidth(no) - BORDER;
        noy = diam.getY(0) - BORDER;
        height = diam.getHeight() + SPACE + sequence.getHeight() + SPACE + SPACE;
    }

    @Override
    protected void drawUnit(Drawing d) {
        //condition diamond
        diam.draw(d);
        
        d.setColor(Color.BLACK);
        d.setFont(FONT);
               
        //false skip line
        d.drawString(no, nox, noy);    
        d.drawLine(diam.getX(0), diam.getY(0), 0, diam.getY(0));
        d.drawLine(0, diam.getY(0), 0, height);
        d.drawLine(0, height, width / 2, height);
        
        //jump start arrow
        d.drawLine(width / 2, height - 2 * SPACE, width / 2, height - SPACE);
        d.drawLine(width / 2, height - SPACE, width, height - SPACE);
        d.drawLine(width, height - SPACE, width, diam.getY(2));
        d.drawArrow(width, diam.getY(2), diam.getX(2), diam.getY(2), 3);

        //true arrow
        d.drawString(yes, yesx, yesy);    
        d.drawArrow(diam.getX(3), diam.getY(3), diam.getX(3), diam.getX(3) + SPACE, 3);
        
        //loop body
        sequence.draw(d);
        
    }
    
}
