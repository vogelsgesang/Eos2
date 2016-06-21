package de.lathanda.eos.gui.flowchart;

import de.lathanda.eos.gui.diagram.Drawing;
import de.lathanda.eos.interpreter.parsetree.IfElse;
import java.awt.Color;
import java.awt.Font;

/**
 * Bedingte Anweisung
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class Alternative extends Unit {
    private final Sequence A; //true
    private final Sequence B; //false
    private final Diamond diam;
    private final String yes = lm.getLabel("Yes");
    private final String no = lm.getLabel("No");

    private final static Font FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);     
    private float yesx;
    private float yesy;     
    private float nox;
    private float noy;     

    private float ABbeginY;
    private float AendY;
    private float BendY;
    private float AcenterX;
    private float BcenterX;
    Alternative(IfElse ifelse) {
        A = new Sequence(ifelse.getThen());
        B = new Sequence(ifelse.getElse());
        diam = new Diamond(ifelse.getLabel());
    }

    @Override
    protected void layout(Drawing d) {
        A.layout(d);
        B.layout(d);
        diam.layout(d);
        d.setFont(FONT);
        
        float sideWidth = Math.max(A.getWidth(), B.getWidth());
        width = Math.max(sideWidth * 2 + SPACEX, diam.getWidth() + sideWidth);

        diam.center(width);
        
        ABbeginY = diam.getHeight() + SPACE;
        A.center(sideWidth);
        B.setOffsetX(width - (sideWidth - B.getWidth()) / 2 - B.getWidth());
        AcenterX = A.getOffsetX() + A.getWidth() / 2;
        BcenterX = B.getOffsetX() + B.getWidth() / 2;
        A.setOffsetY(ABbeginY);
        B.setOffsetY(ABbeginY);
        AendY = A.getHeight() + ABbeginY;
        BendY = B.getHeight() + ABbeginY;
        
        yesx = diam.getX(0) - d.stringWidth(yes) - BORDER;
        yesy = diam.getY(0) - BORDER;
        nox = diam.getX(2) + BORDER;
        noy = diam.getY(2) - BORDER;
        height = diam.getHeight() + SPACE + Math.max(A.getHeight(), B.getHeight()) + SPACE;
    }
    @Override
    protected void drawUnit(Drawing d) {
        //condition diamond
        d.setColor(Color.BLACK);
        d.setFont(FONT);
        diam.draw(d);
               
        //left side arrow
        d.drawString(yes, yesx, yesy);    
        d.drawLine(diam.getX(0), diam.getY(0), AcenterX, diam.getY(0));
        d.drawArrow(AcenterX, diam.getY(0), AcenterX, ABbeginY, 3);
        
        //right side arrow
        d.drawString(no, nox, noy);    
        d.drawLine(diam.getX(2), diam.getY(2), BcenterX, diam.getY(2));
        d.drawArrow(BcenterX, diam.getY(2), BcenterX, ABbeginY, 3);
        
        //left and right side
        A.draw(d);
        B.draw(d);
        
        //rejoin line (arrow is added by surrounding structure)
        d.drawLine(AcenterX, AendY, AcenterX, height);
        d.drawLine(BcenterX, BendY, BcenterX, height);
        d.drawLine(AcenterX, height, BcenterX, height);
    }
}
