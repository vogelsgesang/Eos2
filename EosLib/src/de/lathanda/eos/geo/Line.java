package de.lathanda.eos.geo;

import de.lathanda.eos.base.layout.BalancePoint;

import java.util.LinkedList;

import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.layout.BoundingBox;
import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.base.layout.Transform;

/**
 * Einfache Linie.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Line extends LineFigure {
    private Point a;
    public Line() {
        super();
        a = new Point(10, -10);
    }

    @Override
    protected void drawObject(Picture p) {
        p.drawLine(-a.getX(), -a.getY(),a.getX(), a.getY());
    }      
	@Override
	protected void scaleInternal(double factor) {
		a.scale(factor);
	}

    public void setX1(double x) {
        Box b = this.new Box();
        b.x1 = x;
        b.writeBack();
    }

    public double getX1() {
        return getX() - a.getX() / 2;
    }

    public void setX2(double x) {
        Box b = this.new Box();
        b.x2 = x;
        b.writeBack();
    }

    public double getX2() {
        return getX() + a.getX() / 2;
    }
    public void setY1(double x) {
        Box b = this.new Box();
        b.x1 = x;
        b.writeBack();
    }

    public double getY1() {
        return getX() - a.getY() / 2;
    }

    public void setY2(double x) {
        Box b = this.new Box();
        b.x2 = x;
        b.writeBack();
    }

    public double getY2() {
        return getX() + a.getY() / 2;
    }

    public void setPoint1(double x, double y) {
        Box b = this.new Box();
        b.x1 = x;
        b.y1 = y;
        b.writeBack();
    }

    public void setPoint2(double x, double y) {
        Box b = this.new Box();
        b.x2 = x;
        b.y2 = y;
        b.writeBack();
    }

    public void setPoints(double x1, double y1, double x2, double y2) {
        Box b = this.new Box();
        b.x1 = x1;
        b.y1 = y1;
        b.x2 = x2;
        b.y2 = y2;
        b.writeBack();
    }

    private class Box {
        double x1;
        double y1;
        double x2;
        double y2;
        Box() {
            x1 = getX() - a.getX();
            y1 = getY() - a.getY();
            x2 = getX() + a.getX();
            y2 = getY() + a.getY();
        }
        void writeBack() {
            a = new Point((x2 - x1) /2 , (y2 - y1) /2);
            Line.this.moveTo((x2+x1)/2, (y2+y1)/2);
        }  
    }

    @Override
    protected BalancePoint getBalancePoint() {
        return new BalancePoint(a.getR()*line.getDrawWidth(), getX(), getY()); 
    }    
    @Override
    protected BoundingBox calculateBoundingBox(Transform base, Transform own) {
        Transform t = base.transform(own);
        BoundingBox bound = new BoundingBox();
        bound.add(t.transform(a));
        bound.add(t.transform(-a.getX(), -a.getY()));
        return bound;
    }    
    @Override
	public void getAttributes(LinkedList<Attribut> attributes) {
    	super.getAttributes(attributes);
    	attributes.add(new Attribut("x1", getX1()));
        attributes.add(new Attribut("y1", getY1()));
        attributes.add(new Attribut("x2", getX2()));
        attributes.add(new Attribut("y2", getY2()));
	}     
}
