package de.lathanda.eos.gui.structogram;

import de.lathanda.eos.gui.diagram.Drawing;
import de.lathanda.eos.interpreter.Node;
import de.lathanda.eos.interpreter.parsetree.IfElse;
import de.lathanda.eos.spi.LanguageManager;

/**
 * Basisklasse für alle Struktogrammelemente.
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
    protected final static float BORDER = 1;    

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

    public void setWidth(float width) {
        this.width = width;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public float getHeight() {
        return height;
    }

    public static Unit create(Node n) {
        if (n instanceof IfElse) {
            return new Alternative((IfElse) n);
        } else if (n instanceof de.lathanda.eos.interpreter.parsetree.Loop) {
            return new Loop((de.lathanda.eos.interpreter.parsetree.Loop)n);
        } else {
            return new Statement(n);
        }
    }
}