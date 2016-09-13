package de.lathanda.eos.gui.flowchart;

import de.lathanda.eos.gui.diagram.Drawing;
import de.lathanda.eos.gui.diagram.LoopTimesUnit;
import java.text.MessageFormat;

/**
 * Zählschleife.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class LoopTimes extends ConnectedUnit {
    private final Sequence sequence;
    private final Statement init;
    private final Statement inc;
    private final Diamond diam;
    private final String yes = lm.getLabel("Yes");
    private final String no = lm.getLabel("No");
    private final String var;
    private final String step;
    
    private float yesx;
    private float yesy;     
    private float nox;
    private float noy;     
 
    private float center;
    LoopTimes(LoopTimesUnit n) {
        sequence = new Sequence(n.getSequence());
        var = MessageFormat.format(lm.getLabel("RepeatTimes.Var"), n.getIndexId(), n.getTimes().getLabel());
        diam = new Diamond(MessageFormat.format(lm.getLabel("RepeatTimes.Cond"), n.getIndexId()));
        step = MessageFormat.format(lm.getLabel("RepeatTimes.Step"), n.getIndexId());
        init = new Statement(var);
        inc = new Statement(step);
        font = STANDARD_FONT;
    }
    @Override
	public void layoutUnit(Drawing d) {
        sequence.layout(d);
        init.layout(d);
        inc.layout(d);
        diam.layout(d);

        width = (float)Math.max(
            Math.max(sequence.getWidth(), diam.getWidth()),
            Math.max(inc.getWidth(), init.getWidth())
        ) + 2 * SPACEX;
        center = (width / 2);
        
        //place counting variable initialization
        init.setOffsetX((width - init.getWidth()) / 2);
        
        //build diamond
        float h = init.getHeight() + SPACE;
        diam.setOffsetY(h);
        diam.centerX(width);
        
        yesx = diam.getX(3) + 1.5f*BORDER;
        yesy = diam.getY(3) + d.getAscent()- BORDER;
        nox = diam.getX(0) - d.stringWidth(no) - BORDER;
        noy = diam.getY(0) - BORDER;
        
        //place loop body
        h = diam.getBottom() + SPACE;      
        sequence.setOffsetX((width - sequence.getWidth()) / 2);
        sequence.setOffsetY(h);
        
        //place increment
        h = h + sequence.getHeight() + SPACE;
        inc.setOffsetX((width - inc.getWidth()) / 2);
        inc.setOffsetY(h);
        
        height = h + inc.getHeight() + SPACE + SPACE;
    }

    @Override
	public void drawUnit(Drawing d) {    
        //draw init
        init.draw(d);
        
        //arrow init -> diamond
        d.drawArrow( diam.getX(1), diam.getY(1) - SPACE, diam.getX(1), diam.getY(1), 3);
        
        //condition diamond
        diam.draw(d);
        
        //true arrow
        d.drawString(yes, yesx, yesy);    
        d.drawArrow(diam.getX(3), diam.getY(3), diam.getX(3), diam.getY(3) + SPACE, 3);
        
        //draw loop body
        sequence.draw(d);
        
        //false skip line
        d.drawString(no, nox, noy);    
        d.drawLine(diam.getX(0), diam.getY(0), 0, diam.getY(0));
        d.drawLine(0, diam.getY(0), 0, height);
        d.drawLine(0, height, width / 2, height);
        
        //draw squence -> inc arrow
        float y = inc.getOffsetY();
        d.drawArrow(center, y - SPACE, center, y, 3);
        
        //draw inc
        inc.draw(d);
        
        //jump start arrow
        y = inc.getOffsetY() + inc.getHeight();
        d.drawLine(center, y, center, y + SPACE);
        d.drawLine(center, y + SPACE, width, y + SPACE);
        d.drawLine(width, y + SPACE, width, diam.getY(2));
        d.drawArrow(width, diam.getY(2), diam.getX(2), diam.getY(2), 3);
     
    }
}
