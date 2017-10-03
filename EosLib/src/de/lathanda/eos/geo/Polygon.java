package de.lathanda.eos.geo;

import java.util.ArrayList;
import java.util.LinkedList;

import de.lathanda.eos.base.FillStyle;
import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.layout.BalancePoint;
import de.lathanda.eos.base.layout.BoundingBox;
import de.lathanda.eos.base.layout.Transform;
import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.base.math.Vector;
import de.lathanda.eos.game.geom.Tesselation;
import de.lathanda.eos.game.geom.tesselation.TesselationFailedException;

/**
 * Polygonobjekte werden vom Plotter benutzt.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Polygon extends FilledFigure {
	private ArrayList<Point> points;
	private LinkedList<? extends Point> bound;
	private double weight = 0;
	public Polygon() {
		points = new ArrayList<>();
		line.setDrawWidth(0.5f);
		updatePolygon();
	}

	@Override
	protected void drawObject(Picture p) {
		synchronized (this) {
			if (fill.getFillStyle() != FillStyle.TRANSPARENT) {
				if (bound != null) {
					p.drawPolygon(bound);
				} else {
					p.drawPolygon(points);
				}
			} else {
				for (int i = 1; i < points.size(); i++) {
					p.drawLine(points.get(i - 1), points.get(i));
				}
			}
		}
	}
	@Override
	protected void scaleInternal(double factor) {
		synchronized (this) {
			for (int i = points.size(); i-- > 0;) {
				points.get(i).scale(factor);
			}
			updatePolygon();
		}
	}
	
	@Override
	protected BalancePoint getBalancePoint() {
		return new BalancePoint(weight, getX(), getY());
	}
	
	public boolean isValid() {
		synchronized (this) {
			return points.size() > 1;
		}
	}

	public void addPoint(Point p) {
		synchronized (this) {
			Point copy = new Point(p);
			copy.move(-getX(), -getY());
			points.add(copy);
			updatePolygon();
		}
		fireLayoutChanged();
	}
	private void updatePolygon() {
		if (points.isEmpty()) return;
		double x = 0;
		double y = 0;
		weight = 0;
		
		if (fill.getFillStyle() != FillStyle.TRANSPARENT) {
			try {
				Tesselation tess = Tesselation.getDefaultTesselation();
				tess.addVertices(points);
				bound = tess.getOuterBorder();
				Point a = points.get(points.size() - 1);
				for (int i = 0; i < points.size(); i++) {
					Point b = points.get(i);
					double w = a.getX() * b.getY() - b.getX() * a.getY();
					weight += w;
					x += (a.getX() + b.getX()) * w;
					y += (a.getY() + b.getY()) * w;
					a = b;
				}
				x /= 6;
				y /= 6;
				weight /= 2;
			} catch (TesselationFailedException tfe) {
				bound = null;
				BoundingBox bb = new BoundingBox();
				for (Point p:points) {
					bb.add(p);
				}
				weight = bb.getArea();
				x = bb.getCenter().getX();
				y = bb.getCenter().getY();				
			}
		} else {
			bound = null;
			Point a = points.get(0);
			for (int i = 1; i < points.size(); i++) {
				Point b = points.get(i); 
				double line_weight = new Vector(a, b).getLength() * line.getDrawWidth();
				x += (a.getX() + b.getX()) * line_weight / 2;
				y += (b.getY() + b.getY()) * line_weight / 2;
				weight += line_weight;
				a = b;
			}
		}
		if (weight > 0) {
			x = x / weight;
			y = y / weight;
		} else {
			x = 0;
			y = 0;
		}

		moveInternal(x, y); 
		for(Point p : points) {
			p.move(-x, -y);
		}
		if (bound != null) {
			for(Point p : bound) {
				p.move(-x, -y);
			}
		}
	}
	@Override
	protected BoundingBox calculateBoundingBox(Transform base, Transform own) {
		Transform t = base.transform(own);
		BoundingBox bound = new BoundingBox();
		synchronized (this) {
			for (Point p:points) {
				bound.add(t.transform(p));
			}
		}
		return bound;
	}
    @Override
    public Figure copy() {
    	synchronized (this) {
    		Polygon poly = (Polygon)super.copy();
    		poly.points = new ArrayList<>();
    		for(Point p : points) {
    			poly.points.add(new Point(p));
    		}
    		poly.updatePolygon();
            return poly;
    	}
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
