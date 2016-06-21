package de.lathanda.eos.geo;

import java.util.ArrayList;
import java.util.LinkedList;

import de.lathanda.eos.base.FillStyle;
import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.layout.BalancePoint;
import de.lathanda.eos.base.layout.BoundingBox;
import de.lathanda.eos.base.layout.Transform;
import de.lathanda.eos.base.math.Point;

/**
 * Polygonobjekte werden vom Plotter benutzt.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Polygon extends FilledFigure {
	private ArrayList<Point> points;

	public Polygon() {
		points = new ArrayList<>();
	}

	@Override
	protected void drawObject(Picture p) {
		synchronized (points) {
			if (fill.getFillStyle() != FillStyle.TRANSPARENT) {
				p.drawPolygon(points);
			} else {
				for (int i = 1; i < points.size(); i++) {
					p.drawLine(points.get(i - 1), points.get(i));
				}
			}
		}
	}
	@Override
	protected void scaleInternal(double factor) {
		for (int i = points.size(); i-- > 0;) {
			points.get(i).scale(factor);
		}
	}

	@Override
	protected BalancePoint getBalancePoint() {
		// TODO use triangulation of polygon
		//approximate with bounding box
		BoundingBox approx = getBound();
		Point center = approx.getCenter();
		return new BalancePoint(approx.getArea(), center.getX(), center.getY());		
	}

	public boolean isValid() {
		return points.size() > 1;
	}

	public void addPoint(Point p) {
		synchronized (points) {
			Point copy = new Point(p);
			copy.move(-getX(), -getY());
			points.add(copy);
		}
		fireLayoutChanged();
	}

	@Override
	protected BoundingBox calculateBoundingBox(Transform base, Transform own) {
		Transform t = base.transform(own);
		BoundingBox bound = new BoundingBox();
		for (int i = points.size(); i-- > 0;) {
			bound.add(t.transform(points.get(i)));
		}
		return bound;
	}
    @Override
    public Figure copy() {
    	Polygon poly = (Polygon)super.copy();
    	poly.points = new ArrayList<>();
    	for(Point p : points) {
    		poly.points.add(new Point(p));
    	}
        return poly;
    }
	@Override
	public String toString() {
		return "Polygon{" + points + '}';
	}
    @Override
	public void getAttributes(LinkedList<Attribut> attributes) {
    	super.getAttributes(attributes);    	
    	StringBuilder coords = new StringBuilder();
    	for (Point p : points) {
    		coords.append(p.toString());
    	}
    	attributes.add(new Attribut("points", coords));
	}  
}
