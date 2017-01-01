package de.lathanda.eos.gui.structogram;

import de.lathanda.eos.gui.diagram.AlternativeUnit;
import de.lathanda.eos.gui.diagram.Drawing;
import de.lathanda.eos.gui.diagram.Unit;

/**
 * Alternativebefehlssequenzen.
 * Bedingte Anweisung
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class Alternative extends Unit {
    private final Sequence A; //true
    private final Sequence B; //false
    private final String label;
    private final String yes = Unit.getLabel("Yes");
    private final String no = Unit.getLabel("No");
    private float condy;
    private float condx;
    private float textx;
    private float texty;     
    private float yesx;
    private float yesy;     
    private float nox;
    private float noy;     
    Alternative(AlternativeUnit au) {
        label = au.getLabel();
        A = new Sequence(au.getThen());
        B = new Sequence(au.getElse());
        font = STANDARD_FONT;
    }

    @Override
	public void layoutUnit(Drawing d) {
        A.layout(d);
        B.layout(d);
        if (A.getWidth() < 2 * d.stringWidth(yes)) {
        	A.setWidth(2 * d.stringWidth(yes));
        }
        if (B.getWidth() < 2 * d.stringWidth(no)) {
        	B.setWidth(2 * d.stringWidth(no));
        }        
        float textwidth = d.stringWidth(label);
        float textheight = d.getHeight();
        if (2 * textwidth > A.getWidth() + B.getWidth()) {
            width = 2 *textwidth;
            float diff = (width - A.getWidth() - B.getWidth())/2;
            A.setWidth(A.getWidth() + diff);
            B.setWidth(B.getWidth() + diff);            
        } else {
            width = A.getWidth() + B.getWidth();
        }      
        condy = (textheight * 2);
        condx = A.getWidth();        
        B.setOffsetX(A.getWidth());
        A.setOffsetY(condy);
        B.setOffsetY(condy);
        texty = d.getAscent();
        textx = condx/2 + width/4 - textwidth/2;
        yesx = BORDER;
        yesy = texty+textheight;
        nox = width - BORDER - d.stringWidth(no);
        noy = yesy;
        height = condy + Math.max(A.getHeight(), B.getHeight());
    }
    @Override
	public void drawUnit(Drawing d) {
        d.drawRect(0, 0, width, height);
        d.drawString(label, textx, texty);    
        d.drawString(yes, yesx, yesy);    
        d.drawString(no, nox, noy);    
        d.drawLine(0, 0, condx, condy);
        d.drawLine(condx, condy, width, 0);
        A.draw(d);
        B.draw(d);
    }

    @Override
    public void setWidth(float width) {
        float diff = (width - this.width)/2;
        this.width = width;
        condx += diff;
        nox += 2*diff;
        textx += diff;
        A.setWidth(A.getWidth() + diff);
        B.setWidth(B.getWidth() + diff);        
        B.setOffsetX(A.getWidth());
    }   
}
