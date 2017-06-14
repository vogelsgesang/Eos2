package de.lathanda.eos.geo;

import java.util.LinkedList;

import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.layout.BalancePoint;
import de.lathanda.eos.base.layout.BoundingBox;
import de.lathanda.eos.base.layout.Transform;

/**
 * Kreis
 *
 * @author Peter (Lathanda) Schneider
 */
public class Circle extends FilledFigure {
    private double radius;
    public Circle() {
        super();
        radius = 10;
    }
    @Override
    protected void drawObject(Picture p) {
        p.drawEllipse(radius, radius);
    }  
	@Override
	protected void scaleInternal(double factor) {
		radius *= factor;
	}
    public void setRadius(double radius) {
        this.radius = radius;
        fireLayoutChanged();
    }
    public double getRadius() {
        return radius;
    }
    public void setCenter(double x, double y) {
        moveTo(x,y);
    }
    public void setCenterX(double x) {
        setX(x);
    }
    public double getCenterX() {
        return getX();
    }
    public void setCenterY(double y) {
        setY(y);
    }
    public double getCenterY() {
        return getY();
    } 

    @Override
    protected BalancePoint getBalancePoint() {
        return new BalancePoint(radius*radius*Math.PI, getX(), getY()); 
    }

    @Override
    protected BoundingBox calculateBoundingBox(Transform base, Transform own) {
        Transform t = base.transform(own);
        double r = t.transform(radius);
        return new BoundingBox(t.getdx() - r, t.getdx() + r, t.getdy() - r, t.getdy() + r);
    }
    @Override
 	public void getAttributes(LinkedList<Attribut> attributes) {
    	super.getAttributes(attributes);
    	attributes.add(new Attribut("centerx", getCenterX()));
        attributes.add(new Attribut("centery", getCenterY()));         
        attributes.add(new Attribut("radius", radius));
	}   
}
