package de.lathanda.eos.geo;

import de.lathanda.eos.base.FillDescriptor;
import de.lathanda.eos.base.FillStyle;
import de.lathanda.eos.base.Picture;
import java.awt.Color;
import java.util.LinkedList;

/**
 * Basisklasse für alle gefüllten Figuren.
 *
 * @author Peter (Lathanda) Schneider
 */
public abstract class FilledFigure extends LineFigure {
    protected FillDescriptor fill;
    public FilledFigure() {
        super();
        fill = new FillDescriptor();
    }

    @Override
    public Figure copy() {
        FilledFigure filledfigure = (FilledFigure)super.copy();
        filledfigure.fill = new FillDescriptor(fill);
        return filledfigure;
    }

    @Override
    protected void beforeDrawing(Picture p) {
        super.beforeDrawing(p);
        p.setFill(fill);
    }

    public void setFillStyle(FillStyle fillStyle) {
        fill.setFillStyle(fillStyle);
        fireDataChanged();
    }

    public FillStyle getFillStyle() {
        return fill.getFillStyle();
    }

    public void setFillColor(Color color) {
        fill.setColor(color);
        fireDataChanged();
    }

    public Color getFillColor() {
        return fill.getColor();
    }
    public FillDescriptor getFillDescriptor() {
        return fill;
    }
    @Override
	public void getAttributes(LinkedList<Attribut> attributes) {
    	super.getAttributes(attributes);
        attributes.add(new Attribut("fillcolor", fill.getColor()));
        attributes.add(new Attribut("fillstyle", fill.getFillStyle()));
	}       
}
