package de.lathanda.eos.base;

import java.awt.Color;

/**
 * F&uuml;llungseigenschaften.
 *
 * @author Peter (Lathanda) Schneider
 */
public class FillDescriptor {
    private Color color;
    private FillStyle type;
    public FillDescriptor() {
        this(Color.WHITE, FillStyle.FILLED);
    }
    public FillDescriptor(Color color, FillStyle type) {
        this.color = color;
        this.type = type;
    }
    public FillDescriptor(FillDescriptor fill) {
        this.color = fill.color;
        this.type = fill.type;
    }
    public Color getColor() {
        return color;
    }
    public void setColor(Color c) {
        this.color = c;
    }
    public FillStyle getFillStyle() {
        return type;
    }
    public void setFillStyle(FillStyle ft) {
        type = ft;
    }
}
