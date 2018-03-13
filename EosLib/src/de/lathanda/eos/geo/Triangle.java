package de.lathanda.eos.geo;

import de.lathanda.eos.base.layout.BalancePoint;

import java.util.LinkedList;

import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.layout.BoundingBox;
import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.base.layout.Transform;

/**
 * Dreieck
 *
 * @author Peter (Lathanda) Schneider
 */
public class Triangle  extends FilledFigure {
    private static final int A = 0;
    private static final int B = 1;
    private static final int C = 2;
    Point[] points;
    
    public Triangle() {
        points = new Point[3];
        points[A] = new Point(-10,0);
        points[B] = new Point(10,0);
        points[C] = new Point(0,10);
        recenter();
    }
    @Override
    protected void drawObject(Picture p) {
        p.drawPolygon(points);
    }
	@Override
	protected void scaleInternal(double factor) {
        points[A].scale(factor);
        points[B].scale(factor);
        points[C].scale(factor);
	}
    public void setCorners(double x1, double y1, double x2, double y2, double x3, double y3) {
        resetTransformation();
        points[A] = new Point(x1, y1);
        points[B] = new Point(x2, y2);
        points[C] = new Point(x3, y3);        
        recenter();
        fireDataChanged();
    }

    public void mirrorX() {
        points[A].negateX();
        points[B].negateX();
        points[C].negateX();
        fireDataChanged();        
    }

    public void mirrorY() {
        points[A].negateY();
        points[B].negateY();
        points[C].negateY();
        fireDataChanged();        
    }    
 
    @Override
	public Figure copy() {
    	Triangle tri = (Triangle)super.copy();
		tri.points = new Point[3];
		for(int i = 0; i < 3; i++) {
			tri.points[i] = new Point(points[i]);
		}
		return tri;
	}

	@Override
    protected BalancePoint getBalancePoint() {
        double area;
        double xa = points[A].getX();
        double xb = points[B].getX();
        double xc = points[C].getX();
        double ya = points[A].getY();
        double yb = points[B].getY();
        double yc = points[C].getY();
        area = Math.abs((xa-xb)*(ya-yc)-(ya-yb)*(xa-xc));
        return new BalancePoint(area, getX(), getY()); 
    }
    private void recenter() {
        double x = 0;
        double y = 0;
        for(Point p : points) {
            x += p.getX();
            y += p.getY();
        }
        x /= 3d;
        y /= 3d;
        this.moveInternal(x + getX(), y + getY());
        points[A].move(-x, -y);
        points[B].move(-x, -y);
        points[C].move(-x, -y);    
    }
    @Override
    protected BoundingBox calculateBoundingBox(Transform base, Transform own) {
        Transform t = base.transform(own);
        BoundingBox bound = new BoundingBox();
        bound.add(t.transform(points[A]));
        bound.add(t.transform(points[B]));
        bound.add(t.transform(points[C]));
        return bound;
    }    
    @Override
	public void getAttributes(LinkedList<Attribut> attributes) {
    	super.getAttributes(attributes);
    	attributes.add(new Attribut("a", getTransformedPosition(points[A]).toString()));
    	attributes.add(new Attribut("b", getTransformedPosition(points[B]).toString()));
    	attributes.add(new Attribut("c", getTransformedPosition(points[C]).toString()));
	}     
}
