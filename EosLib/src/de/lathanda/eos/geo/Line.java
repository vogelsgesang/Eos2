package de.lathanda.eos.geo;

import de.lathanda.eos.base.layout.BalancePoint;

import java.util.LinkedList;

import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.layout.BoundingBox;
import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.base.math.Vector;
import de.lathanda.eos.base.layout.Transform;
import static java.lang.Math.*;
/**
 * Einfache Linie.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Line extends LineFigure {
    private double length;
    public Line() {
        super();
        // (-10,10) -> (10, -10)
        this.length = sqrt(800);
        transform.setAngle(-PI/2d);
		line.setDrawWidth(0.5f);        
    }

    @Override
    protected void drawObject(Picture p) {
        p.drawLine(-length/2, 0, length/2, 0);
    }      
	@Override
	protected void scaleInternal(double factor) {
		length *= factor;
	}

    public void setX1(double x) {
        TrueLine t = this.new TrueLine();
        t.A.setX(x);
        t.writeBack();
        fireDataChanged();
    }

    public double getX1() {
        TrueLine t = this.new TrueLine();    	
        return t.A.getX();
    }

    public void setX2(double x) {
        TrueLine t = this.new TrueLine();
        t.B.setX(x);
        t.writeBack();
        fireDataChanged();
    }

    public double getX2() {
        TrueLine t = this.new TrueLine();    	
        return t.B.getX();
    }
    public void setY1(double y) {
        TrueLine t = this.new TrueLine();
        t.A.setY(y);
        t.writeBack();
        fireDataChanged();
    }

    public double getY1() {
        TrueLine t = this.new TrueLine();    	
        return t.A.getY();
    }

    public void setY2(double y) {
        TrueLine t = this.new TrueLine();
        t.B.setY(y);
        t.writeBack();
        fireDataChanged();
    }

    public double getY2() {
        TrueLine t = this.new TrueLine();    	
        return t.B.getY();
    }

    public void setPoint1(double x, double y) {
        TrueLine t = this.new TrueLine();
        t.A = new Point(x, y);
        t.writeBack();
        fireDataChanged();
    }

    public void setPoint2(double x, double y) {
        TrueLine t = this.new TrueLine();
        t.B = new Point(x, y);
        t.writeBack();
        fireDataChanged();
    }

    public void setPoints(double x1, double y1, double x2, double y2) {
        TrueLine t = this.new TrueLine();
        t.A = new Point(x1, y1);
        t.B = new Point(x2, y2);
        t.writeBack();
        fireDataChanged();
    }
    @Override
    public Figure copy() {
        Line line = (Line)super.copy();
        return line;
    }
    private class TrueLine {
    	Point A;
    	Point B;
        TrueLine() {
        	A = getTransformedPosition(new Point(-length/2d, 0));
        	B = getTransformedPosition(new Point( length/2d, 0));
        }
        void writeBack() {
        	resetTransformation();
        	Vector v = new Vector(A, B);
        	length = v.getLength();
        	transform = transform.setTranslation((A.getX()+B.getX())/2,(A.getY()+B.getY())/2);
        	transform = transform.rotate(v.getAngle());
        }  
    }

    @Override
    protected BalancePoint getBalancePoint() {
        return new BalancePoint(length*line.getDrawWidth(), getX(), getY()); 
    }    
    @Override
    protected BoundingBox calculateBoundingBox(Transform base, Transform own) {
        Transform t = base.transform(own);
        BoundingBox bound = new BoundingBox();
        bound.add(t.transform(-length/2d, 0));
        bound.add(t.transform( length/2d, 0));
        return bound;
    }    
    @Override
	public void getAttributes(LinkedList<Attribut> attributes) {
    	super.getAttributes(attributes);
    	TrueLine t = this.new TrueLine();
        attributes.add(new Attribut("linecolor", line.getColor()));
        attributes.add(new Attribut("linestyle", line.getLineStyle()));    
        attributes.add(new Attribut("linewidth", line.getDrawWidth()));        
    	attributes.add(new Attribut("length", length));
    	attributes.add(new Attribut("x1", t.A.getX()));
        attributes.add(new Attribut("y1", t.A.getY()));
        attributes.add(new Attribut("x2", t.B.getX()));
        attributes.add(new Attribut("y2", t.B.getY()));
	}     
}
