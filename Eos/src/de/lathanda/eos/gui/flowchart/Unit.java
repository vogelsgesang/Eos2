package de.lathanda.eos.gui.flowchart;

import de.lathanda.eos.gui.diagram.Drawing;
import de.lathanda.eos.interpreter.Node;
import de.lathanda.eos.interpreter.parsetree.DoWhile;
import de.lathanda.eos.interpreter.parsetree.IfElse;
import de.lathanda.eos.interpreter.parsetree.RepeatForever;
import de.lathanda.eos.interpreter.parsetree.RepeatTimes;
import de.lathanda.eos.interpreter.parsetree.WhileDo;
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

    public static Unit create(Node n) {
        if (n instanceof IfElse) {
            return new Alternative((IfElse) n);
        } else if (n instanceof RepeatForever) {
            return new LoopForever((RepeatForever)n);
        } else if (n instanceof RepeatTimes) {    
            return new LoopTimes((RepeatTimes)n);
        } else if (n instanceof WhileDo) {
            return new LoopWhile((WhileDo)n);
        } else if (n instanceof DoWhile) {
            return new LoopDoWhile((DoWhile)n);
        } else {
            return new Statement(n);
        }
    }  
    
}
