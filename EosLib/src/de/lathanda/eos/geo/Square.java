package de.lathanda.eos.geo;

import de.lathanda.eos.base.layout.BalancePoint;

import java.util.LinkedList;

import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.layout.BoundingBox;
import de.lathanda.eos.base.layout.Transform;

/**
 * Quadrat.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Square extends FilledFigure {
    protected double side;
    public Square() {
        super();
        side  = 20;
    }

    @Override
    protected void drawObject(Picture p) {
        p.drawRect(side, side);
    }
	@Override
	protected void scaleInternal(double factor) {
		side *= factor;
	}

    public void setSide(double side) {
        this.side = side;
        fireLayoutChanged();
    }
    public double getSide() {
        return side;
    }

    public void setLeftTop(double left, double top) {
        moveTo(left + side/2, top + side/2);
    }
    public void setRightBottom(double right, double bottom) {
        moveTo(right - side/2, bottom - side/2);
    }
    @Override
    protected BalancePoint getBalancePoint() {
        return new BalancePoint(side*side, getX(), getY());
    }
    @Override
    protected BoundingBox calculateBoundingBox(Transform base, Transform own) {
        Transform t = base.transform(own);
        BoundingBox bound = new BoundingBox();
        bound.add(t.transform(+ side / 2, + side / 2));
        bound.add(t.transform(- side / 2, + side / 2));
        bound.add(t.transform(- side / 2, - side / 2));
        bound.add(t.transform(+ side / 2, - side / 2));       
        return bound;
    }    
    @Override
	public void getAttributes(LinkedList<Attribut> attributes) {
    	super.getAttributes(attributes);
    	attributes.add(new Attribut("side", side));
	}     
}
