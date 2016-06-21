package de.lathanda.eos.gui.structogram;

import de.lathanda.eos.gui.diagram.Drawing;
import de.lathanda.eos.interpreter.parsetree.IfElse;
import java.awt.Color;
import java.awt.Font;

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
    private final String yes = lm.getLabel("Yes");
    private final String no = lm.getLabel("No");
    private float condy;
    private float condx;
    private final static Font FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    private float textx;
    private float texty;     
    private float yesx;
    private float yesy;     
    private float nox;
    private float noy;     
    Alternative(IfElse ifelse) {
        label = ifelse.getLabel();
        A = new Sequence(ifelse.getThen());
        B = new Sequence(ifelse.getElse());
    }

    @Override
    protected void layout(Drawing d) {
        A.layout(d);
        B.layout(d);
        d.setFont(FONT);
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
    protected void drawUnit(Drawing d) {
        d.setColor(Color.BLACK);
        d.setFont(FONT);
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
        A.setWidth(A.getWidth() + diff);
        B.setWidth(B.getWidth() + diff);
        B.setOffsetX(A.getWidth());
    }   
}
