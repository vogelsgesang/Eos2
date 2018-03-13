package de.lathanda.eos.geo;

import de.lathanda.eos.base.layout.BalancePoint;

import java.util.LinkedList;

import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.layout.BoundingBox;
import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.base.math.Vector;
import de.lathanda.eos.base.layout.Transform;

/**
 * Einfache Linie.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Line extends LineFigure {
    private Point a;
    private Point b;
    public Line() {
        super();
        a = new Point(-10, 10);
        b = new Point(10, -10);
		line.setDrawWidth(0.5f);        
    }

    @Override
    protected void drawObject(Picture p) {
        p.drawLine(a.getX(), a.getY(), b.getX(), b.getY());
    }      
	@Override
	protected void scaleInternal(double factor) {
		a.scale(factor);
		b.scale(factor);
	}

    public void setX1(double x) {
        TrueLine t = this.new TrueLine();
        t.A.setX(x);
        t.writeBack();
    }

    public double getX1() {
        return getTransformedPosition(a).getX();
    }

    public void setX2(double x) {
        TrueLine t = this.new TrueLine();
        t.B.setX(x);
        t.writeBack();
    }

    public double getX2() {
        return getTransformedPosition(b).getX();
    }
    public void setY1(double y) {
        TrueLine t = this.new TrueLine();
        t.A.setY(y);
        t.writeBack();
    }

    public double getY1() {
        return getTransformedPosition(a).getY();
    }

    public void setY2(double y) {
        TrueLine t = this.new TrueLine();
        t.B.setY(y);
        t.writeBack();
    }

    public double getY2() {
        return getTransformedPosition(b).getY();
    }

    public void setPoint1(double x, double y) {
        TrueLine t = this.new TrueLine();
        t.A = new Point(x, y);
        t.writeBack();
    }

    public void setPoint2(double x, double y) {
        TrueLine t = this.new TrueLine();
        t.B = new Point(x, y);
        t.writeBack();
    }

    public void setPoints(double x1, double y1, double x2, double y2) {
        TrueLine t = this.new TrueLine();
        t.A = new Point(x1, y1);
        t.B = new Point(x2, y2);
        t.writeBack();
    }
    @Override
    public Figure copy() {
        Line line = (Line)super.copy();
        line.a = new Point(a);
        line.b = new Point(b);
        return line;
    }
    private class TrueLine {
    		Point A;
    		Point B;
        TrueLine() {
        		Point A = getTransformedPosition(a);
        		Point B = getTransformedPosition(b);
        }
        void writeBack() {
        	    resetTransformation();
        	    Vector v = new Vector(A, B).multiply(0.5);
            a = new Point(-v.getdx(), -v.getdy());
            b = new Point(v.getdx(), v.getdy());
            Line.this.moveTo((A.getX()+B.getX())/2,(A.getY()+B.getY())/2);
        }  
    }

    @Override
    protected BalancePoint getBalancePoint() {
        return new BalancePoint(2*a.getR()*line.getDrawWidth(), getX(), getY()); 
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
        attributes.add(new Attribut("linecolor", line.getColor()));
        attributes.add(new Attribut("linestyle", line.getLineStyle()));    
        attributes.add(new Attribut("linewidth", line.getDrawWidth()));        
    	attributes.add(new Attribut("x1", getX1()));
        attributes.add(new Attribut("y1", getY1()));
        attributes.add(new Attribut("x2", getX2()));
        attributes.add(new Attribut("y2", getY2()));
	}     
}
