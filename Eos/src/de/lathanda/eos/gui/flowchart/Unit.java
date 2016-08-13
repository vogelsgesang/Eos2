package de.lathanda.eos.gui.flowchart;

import de.lathanda.eos.common.ProgramNode;
import de.lathanda.eos.gui.diagram.AlternativeUnit;
import de.lathanda.eos.gui.diagram.Drawing;
import de.lathanda.eos.gui.diagram.LoopForeverUnit;
import de.lathanda.eos.gui.diagram.LoopTimesUnit;
import de.lathanda.eos.gui.diagram.LoopUnit;
import de.lathanda.eos.spi.LanguageManager;

/**
 * Basisklasse für alle Kontrollflussdiagrammteile.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public abstract class Unit {

    protected static final LanguageManager lm = LanguageManager.getInstance();
    protected float width;
    protected float height;
    private float offsetX = 0;
    private float offsetY = 0;
    protected static final float SPACE = 5;
    protected final static float BORDER = 1;
    protected final static float SPACEX = 5;
    protected boolean needsIncomingArrow = true;
    protected boolean neverEnds = false;
    protected abstract void drawUnit(Drawing d);

    public final void draw(Drawing d) {
        d.pushTransform();
        d.translate(offsetX, offsetY);
        drawUnit(d);
        d.popTransform();
    }

    protected abstract void layout(Drawing d);

    public float getWidth() {
        return width;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public float getOffsetX() {
        return offsetX;
    }
    public float getBottomY() {
    	return offsetY + height;
    }
    public void center(float width) {
        setOffsetX((width - this.width)/2);
    }
    public float getHeight() {
        return height;
    }

    public static Unit create(ProgramNode n) {
        if (n instanceof AlternativeUnit) {
            return new Alternative((AlternativeUnit) n);
        } else if (n instanceof LoopForeverUnit) {
            return new LoopForever((LoopForeverUnit)n);
        } else if (n instanceof LoopTimesUnit) {    
            return new LoopTimes((LoopTimesUnit)n);
        } else if (n instanceof LoopUnit) {
        	LoopUnit lu = (LoopUnit)n;
        	if (lu.isPre()) {
        		return new LoopDoWhile(lu);
        	} else {
        		return new LoopWhile(lu);
        	}            
        } else {
            return new Statement(n);
        }
    }  
    
}
